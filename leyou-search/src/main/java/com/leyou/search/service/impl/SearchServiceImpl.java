package com.leyou.search.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.common.pojo.*;
import com.leyou.common.vo.PageResult;
import com.leyou.common.vo.SpuVo;
import com.leyou.search.client.*;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.service.SearchService;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private SkuClient skuClient;
    @Autowired
    private SpecClient specClient;
    @Autowired
    private SpuClient spuClient;
    @Autowired
    private GoodsRepository goodsRepository;

    public Goods buildGoods(SpuVo spuVo) throws IOException {
        Goods goods = new Goods();
        try{
            Long id = spuVo.getId();
            //id属性
            goods.setId(id);
            //all字段
            Long cid1 = spuVo.getCid1();
            Long cid2 = spuVo.getCid2();
            Long cid3 = spuVo.getCid3();
            List<String> names = categoryClient.getNamesByIds(Arrays.asList(cid1, cid2, cid3));
            String brandName = "";
            try{
                brandName = brandClient.getBrandById(spuVo.getBrandId()).getName();
            }catch (Exception e){

            }

            goods.setAll(spuVo.getSubTitle()+ " " + brandName  + " "+ StringUtils.join(names,' '));
            //标题
            goods.setSubTitle(spuVo.getSubTitle());
            //品牌
            goods.setBrandId(spuVo.getBrandId());
            //分类
            goods.setCid1(spuVo.getCid1());
            goods.setCid2(spuVo.getCid2());
            goods.setCid3(spuVo.getCid3());
            //生成时间
            goods.setCreateTime(spuVo.getCreateTime());
            //价格
            List<Sku> skus = new ArrayList<>();
            try{
                skus = skuClient.getSkuBySpuId(id);
            }catch (Exception e){

            }
            if(CollectionUtils.isEmpty(skus)){
                return null;
            }
            List<Long> price = skus.stream().map(e ->{
                return e.getPrice();
            }).collect(Collectors.toList());
            goods.setPrice(price);
            //skus

            List<Map<String,Object>> skuList = skus.stream().map(e ->{
                Map<String,Object> map = new HashMap<>();
                map.put("id", e.getId());
                map.put("title", e.getTitle());
                map.put("price", e.getPrice());
                map.put("image", org.apache.commons.lang.StringUtils.isBlank(e.getImages()) ? "" : org.apache.commons.lang.StringUtils.split(e.getImages(), ",")[0]);
                return map;
            }).collect(Collectors.toList());
            goods.setSkus(objectMapper.writeValueAsString(skuList));
            //规格参数
            SpuDetail spuDetail = new SpuDetail();
            try{
                spuDetail = spuClient.getSpuDetailById(id);
            }catch (Exception e){

            }
            Map<String,Object> specMap = new HashMap<>();
            Map<Long, Object> genic = objectMapper.readValue(spuDetail.getGeneric(), new TypeReference<Map<Long, Object>>() {
            });
            Map<String,Object> special = objectMapper.readValue(spuDetail.getSpecial(), new TypeReference<Map<String, Object>>() {
            });
            List<Map<String,Object>> specs = new ArrayList<>();
            Set<Long> keySet = genic.keySet();
            //通用规格参数
            List<SpecParam> specList = new ArrayList<>();
            try{
                specList = specClient.getSpecParamByGid(null, spuVo.getCid3(),true,null);
            }catch (Exception e){

            }
            specList.forEach( e ->{
                Boolean gen = e.getGeneric();
                if(gen){
                    //字符串类型
                    if(!e.getNumeric()){
                        specMap.put(e.getName(),genic.get(e.getId()));
                    }else {
                        specMap.put(e.getName(),getSegStr(e,genic));
                    }
                }else {
                    specMap.put(e.getName(),special.get(e.getId()));
                }
            });
            goods.setSpecs(specMap);
        }catch (Exception e){

        }
        return goods;
    }

    public String getSegStr(SpecParam param,Map<Long,Object> genicMap){
        String result = "";
        String segments = param.getSegments();
        String[] segmentsArr = segments.split(",");
        double value = Double.valueOf(genicMap.get(param.getId()).toString());
        double end = Double.MAX_VALUE;
        for(String fanwei:segmentsArr){
            String[] nums = fanwei.split("-");
            double min = Double.valueOf(nums[0]);
            double max = 0;
            if(nums.length > 1){
                max = Double.valueOf(nums[1]);
            }
           if(value >= min && value < end){
               if(nums.length == 1){
                   //最后一个
                   result = min + param.getUnit() + "以上";
               }else if(min == 0){
                   result = max + param.getUnit() + "以下";

               }else {
                   result = fanwei + param.getUnit();
               }
               break;
           }

        }
        return result;
    }

    @Override
    public SearchResult getGoodsPage(SearchRequest searchResult) {
        String key = searchResult.getKey();
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        QueryBuilder basicBuilder = buildMatchBuilder(searchResult);
        //查询关键字
        nativeSearchQueryBuilder.withQuery(basicBuilder);
        //结果集过滤
        nativeSearchQueryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","skus","subTitle"},null));
        //分页
        nativeSearchQueryBuilder.withPageable(PageRequest.of(searchResult.getPage() - 1,searchResult.getSize()));
        //排序
        Boolean descending = searchResult.getDescending();
        String sortBy = searchResult.getSortBy();
        if(!org.apache.commons.lang.StringUtils.isBlank(sortBy)){
            nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(sortBy).order(descending ? SortOrder.DESC : SortOrder.ASC));
        }
        //聚合
        String categoryAggName = "category"; // 商品分类聚合名称
        String brandAggName = "brand";
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));

        //查询
        AggregatedPage<Goods> search = (AggregatedPage<Goods>) goodsRepository.search(nativeSearchQueryBuilder.build());
        //取聚合结果接
        Aggregation aggregationCate = search.getAggregation(categoryAggName);
        Aggregation aggregationBrand = search.getAggregation(brandAggName);
        SearchResult pageResult = new SearchResult(search.getContent(),Long.valueOf(search.getTotalElements()),search.getTotalPages());
        List<Map<String,Object>> cateList = getCategoriesAgg(aggregationCate);
        //规格参数聚合
        List<Map<String,Object>> specs = new ArrayList<>();
        if(!CollectionUtils.isEmpty(cateList) && cateList.size() == 1){
            specs = getSpecAggregation(Long.valueOf(cateList.get(0).get("id").toString()),basicBuilder);
        }
        pageResult.setCategries(cateList);
        pageResult.setBrands(getBrandsAgg(aggregationBrand));
        pageResult.setSpecs(specs);
        return pageResult;
    }
    private List<Map<String,Object>> getCategoriesAgg(Aggregation aggregation){
        LongTerms terms = (LongTerms) aggregation;
        List<LongTerms.Bucket> buckets = terms.getBuckets();
        List<Map<String, Object>> list = buckets.stream().map(e -> {
            Map<String, Object> map = new HashMap<>();
            List<String> names = categoryClient.getNamesByIds(Arrays.asList(e.getKeyAsNumber().longValue()));
            map.put("id", e.getKeyAsNumber().toString());
            map.put("name", names.get(0));
            return map;
        }).collect(Collectors.toList());
        return list;
    }
    private List<Brand> getBrandsAgg(Aggregation aggregation){
        LongTerms terms = (LongTerms)aggregation;
        List<LongTerms.Bucket> buckets = terms.getBuckets();
        List<Brand> list = new ArrayList<>();
        buckets.forEach(e->{
            long id = e.getKeyAsNumber().longValue();
            try{
                list.add( brandClient.getBrandById(id));
            }catch (Exception ex){
                System.out.println(id);
            }

        });
        return list;
    }

    /**
     * 获取规格参数聚合结果集
     * @return
     */
    public List<Map<String,Object>> getSpecAggregation(Long cid, QueryBuilder basicBuilder){
        List<Map<String,Object>> specs = new ArrayList<>();
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(basicBuilder);
        List<SpecParam> specParams = specClient.getSpecParamByGid(null, cid, true,null);
        specParams.forEach(e ->{
            queryBuilder.addAggregation(AggregationBuilders.terms(e.getName()).field("specs." + e.getName()+".keyword"));
        });
        //遍历
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{},null));
        AggregatedPage<Goods> page = (AggregatedPage<Goods>)goodsRepository.search(queryBuilder.build());
        Map<String, Aggregation> aggMap = page.getAggregations().asMap();
        for(Map.Entry<String, Aggregation> entry : aggMap.entrySet()){
           try{
               Map<String,Object> map = new HashMap<>();
               String key = entry.getKey();
               StringTerms terms = (StringTerms) (entry.getValue());
               List<StringTerms.Bucket> buckets = terms.getBuckets();
               List<String> collect = buckets.stream().map(e -> {
                   return e.getKeyAsString();
               }).collect(Collectors.toList());
               map.put("key",key);
               map.put("options",collect);
               specs.add(map);
           }catch (Exception e){

           }
        }

        return specs;
    }

    public QueryBuilder buildMatchBuilder(SearchRequest searchRequest){
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("all",searchRequest.getKey()).operator(Operator.AND);
        boolQueryBuilder.must(queryBuilder);
        BoolQueryBuilder filter = QueryBuilders.boolQuery();
        Map<String,Object> filters = searchRequest.getFilter();
        for(Map.Entry<String,Object> entry : filters.entrySet()){
            String key = entry.getKey();
            String val = entry.getValue().toString();
            if(StringUtils.equals(key,"分类")){
                key = "cid3";
            }else if(StringUtils.equals(key,"品牌")){
                key = "brandId";
            }else {
                key = "specs." + key + ".keyword";
            }
            filter.must(QueryBuilders.termQuery(key,val));
        }
        boolQueryBuilder.filter(filter);
        return  boolQueryBuilder;
    }
   public void saveGoods(Long id) throws IOException {
       Spu spu = spuClient.getSpuById(id);
       SpuVo spuVo = new SpuVo();
       BeanUtils.copyProperties(spu,spuVo);
       Goods goods = buildGoods(spuVo);
       this.goodsRepository.save(goods);
   }
}
