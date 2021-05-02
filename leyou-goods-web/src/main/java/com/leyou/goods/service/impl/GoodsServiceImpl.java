package com.leyou.goods.service.impl;

import com.leyou.common.pojo.*;
import com.leyou.goods.client.*;
import com.leyou.goods.service.GoodsService;
import com.leyou.goods.utils.ThreadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.*;
import java.util.*;
@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private SpuClient spuClient;
    @Autowired
    private SkuClient skuClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private SpecClient specClient;
    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public Map goodsDetail(Long id) {
        Map map = new HashMap();

        Spu spu = spuClient.getSpuById(id);
        spu.setSkus(skuClient.getSkuBySpuId(id));

        SpuDetail spuDetail = spuClient.getSpuDetailById(id);

        List cids = Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3());
        List cateNames = categoryClient.getNamesByIds(cids);

        List categories = new ArrayList();
        for(int i = 0;i < cateNames.size();i++){
            Map cateMap = new HashMap();
            //cateMap.put(cids.get(i),cateNames.get(i));
            cateMap.put("id",cids.get(i));
            cateMap.put("name",cateNames.get(i));
            categories.add(cateMap);
        }

        Brand brand = brandClient.getBrandById(spu.getBrandId());
        Map brandMap =new HashMap();
        brandMap.put("id",brand.getId());
        brandMap.put("name",brand.getName());

        List<SpecGroup> groups = specClient.getSpecGroups(spu.getCid3());

        List<SpecParam> paramsList = specClient.getSpecParamByGid(null,spu.getCid3(),null,false);
        List params = new ArrayList();
        Map mapParam = new HashMap();
        paramsList.forEach(e -> {

            mapParam.put(e.getId(),e.getName());
            params.add(mapParam);
        });

        map.put("spu",spu);
        map.put("spuDetail",spuDetail);
        map.put("categories",categories);
        map.put("brand",brandMap);
        map.put("groups",groups);
        map.put("params",mapParam);
        return map;
    }


    public void createGoodsHtml(Long id) {
        Map map = this.goodsDetail(id);
        Context context = new Context();
        context.setVariables(map);
        File file = new File("D:\\java\\tools\\nginx\\html\\item\\" + id +".html");
        try(
                PrintWriter writer = new PrintWriter(file);
                ){

            templateEngine.process("item",context,writer);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void asynExcute(Long spuId){
        ThreadUtils.execute(()-> createGoodsHtml(spuId));
    }
}
