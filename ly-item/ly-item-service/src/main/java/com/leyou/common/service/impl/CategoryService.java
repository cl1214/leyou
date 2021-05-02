package com.leyou.common.service.impl;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.mapper.CategoryMapper;
import com.leyou.common.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategoryMapper mapper;
    public List<Category> queryCategoryListByPid(Long pid){
        Category category = new Category();
        category.setParentId(pid);
        List<Category> list = mapper.select(category);
        if(CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return list;
    }
    public List<Category> getCategorysByCids(List<Long> cids){
        return mapper.selectByIdList(cids);
    }

    public List<String> getNamesByIds(List<Long> ids){
        List<Category> list = this.getCategorysByCids(ids);
        if(CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        List<String> namesList = list.stream().map(e -> {
            String name = e.getName();
            return name;
        }).collect(Collectors.toList());
        return namesList;
    }
}
