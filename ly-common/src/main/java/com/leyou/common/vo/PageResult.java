package com.leyou.common.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class PageResult<T> {
    private List<T> items;
    private Long total;
    private Integer totalPage;
    public PageResult(List<T> items,Long total){
        this.items = items;
        this.total = total;
    }
    public PageResult(List<T> items,Long total,Integer totalPage){
        this.items = items;
        this.total = total;
        this.totalPage = totalPage;
    }
}
