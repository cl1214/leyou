package com.leyou.common.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.common.mapper.BrandMapper;
import com.leyou.common.pojo.Brand;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandService {
    @Autowired
    private BrandMapper brandMapper;

    public PageResult<Brand> getBrandList(String key,int page,int rows,String ordeBy,Boolean desc){
        PageResult<Brand> pageResult = new PageResult<>();
        //开启分页
        PageHelper.startPage(page,rows);
        //拼接查询条件
        Example example = new Example(Brand.class);
        example.createCriteria().orLike("name","%"+key+"%").orEqualTo("letter",key.toUpperCase());
        if(!StringUtils.isBlank(ordeBy)){
            example.setOrderByClause(ordeBy + " "+ (desc?"desc":"asc"));
        }
        List<Brand> brands = brandMapper.selectByExample(example);
        PageInfo<Brand> pageInfo = new PageInfo<>(brands);
        if(CollectionUtils.isEmpty(brands)){
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        pageResult.setItems(brands);
        pageResult.setTotal(pageInfo.getTotal());
        return  pageResult;
    }
    @Transactional
    public void insrtBrand(Brand brand,List<Integer> cids){
        int count = -1;
        count = brandMapper.insert(brand);
        if(count <=0 ){
            throw new LyException(ExceptionEnum.BRAND_INSERT_FAIlED);
        }
        //新增品牌分类中间表关系
        for(Integer cid : cids){
            int count1 = brandMapper.insertBrandCategory(brand.getId(),cid);
            if(count1 <=0 ){
                throw new LyException(ExceptionEnum.BRAND_INSERT_FAIlED);
            }

        }

    }
    public Brand getBrandById(Long id){
        Brand brand =  brandMapper.selectByPrimaryKey(id);
        if(brand == null){
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return brand;
    }
    public List<Brand> getBrandsByCid(Long cid){
        List<Brand> list = brandMapper.getBrandsByCid(cid);
        if(CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return list;
    }
}
