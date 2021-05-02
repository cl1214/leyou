package com.leyou.search.pojo;

import com.leyou.common.pojo.Brand;
import com.leyou.common.vo.PageResult;
import lombok.Data;

import java.util.List;
import java.util.Map;
@Data
public class SearchResult extends PageResult<Goods>{
    private List<Map<String,Object>> categries;
    private List<Brand> brands;
    private List<Map<String,Object>> specs;
    public SearchResult(List<Goods> items,Long total,Integer totalPage){
        super(items,total,totalPage);
    }

}
