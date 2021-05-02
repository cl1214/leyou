package com.leyou.common.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.common.mapper.SpuDetailMapper;
import com.leyou.common.mapper.SpuMapper;
import com.leyou.common.pojo.Brand;
import com.leyou.common.pojo.Spu;
import com.leyou.common.pojo.SpuDetail;
import com.leyou.common.service.SpuService;
import com.leyou.common.vo.SpuVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpuServiceImpl implements SpuService {
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;
    @Override
    public PageResult<SpuVo> page(String key, Boolean saleable, Integer page, Integer row) {
        //分页
        PageHelper.startPage(page,row);
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        //判断条件
        if(!StringUtils.isBlank(key)){
            criteria.andLike("title","%"+key+"%");
        }
        if(saleable != null){
            criteria.andEqualTo("saleable",saleable);
        }
        Page<Spu> pageInfo = (Page<Spu>) spuMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(pageInfo)){
            throw new LyException(ExceptionEnum.SPU_NOT_FOUND);
        }
        List<SpuVo> result = pageInfo.getResult().stream().map(spu -> {
            SpuVo spuVo = new SpuVo();
            BeanUtils.copyProperties(spu, spuVo);
            Long cid1 = spuVo.getCid1();
            Long cid2 = spuVo.getCid2();
            Long cid3 = spuVo.getCid3();
            Long brandId = spuVo.getBrandId();
            List<String> cateNames = categoryService.getCategorysByCids((Arrays.asList(cid1, cid2, cid3))).stream().map(category -> {
                String cateName = category.getName();
                return cateName;
            }).collect(Collectors.toList());
            String cName = StringUtils.join(cateNames, "/");

            //设置品牌名称
            Brand brand = brandService.getBrandById(brandId);
            spuVo.setBName(brand.getName());
            spuVo.setCName(cName);
            return spuVo;
        }).collect(Collectors.toList());
        PageResult<SpuVo> spuPageResult = new PageResult<>();
        spuPageResult.setTotal(pageInfo.getTotal());
        spuPageResult.setItems(result);

        return spuPageResult;
    }

    @Override
    public SpuDetail getSpuDetailById(Long spuId) {
        SpuDetail spuDetail = spuDetailMapper.selectByPrimaryKey(spuId);
        if(spuDetail == null){
            throw  new LyException(ExceptionEnum.SPU_NOT_FOUND);
        }
        return spuDetail;
    }

    @Override
    public Spu getSpuById(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if(spu == null){
            throw new LyException(ExceptionEnum.SPU_NOT_FOUND);
        }
        return spu;
    }
}
