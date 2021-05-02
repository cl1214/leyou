package com.leyou.search.pojo;

import lombok.Data;
import org.elasticsearch.percolator.PercolatorFieldMapper;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
@Document(indexName = "goods",type = "docs",shards = 1,replicas = 0)
@Data
public class Goods {
    @Id
    private Long id;
    @Field(type = FieldType.Text,analyzer = "ik_max_word" )
    private String all;
    @Field(type = FieldType.Keyword,index = false)
    private String subTitle;
    private Long brandId;
    private Long cid1;
    private Long cid2;
    private Long cid3;
    private Date createTime;
    private List<Long> price;
    @Field(type = FieldType.Keyword,index = false)
    private String skus;
    private Map<String,Object> specs;

}
