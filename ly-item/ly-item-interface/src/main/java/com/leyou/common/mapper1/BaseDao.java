package com.leyou.common.mapper1;


import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;
@RegisterMapper
public interface BaseDao<T> extends Mapper<T>, InsertListMapper<T>, SelectByIdListMapper<T,Long> {
}
