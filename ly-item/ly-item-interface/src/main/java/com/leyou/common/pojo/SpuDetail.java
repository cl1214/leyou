package com.leyou.common.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
@Data
@NoArgsConstructor
@Table(name = "tb_spu_detail")
public class SpuDetail {
    @Id
    private Long spuId;
    private String description;
    private String specifications;
    private String specTemplate;
    private String packingList;
    private String afterService;
    private String generic;
    private String special;

    private void setGenericSpec(String generic){
        this.generic =generic;
    }
    public String getGenericSpec(){
        return generic;
    }
    private void setSpecialSpec(String special){
        this.special =special;
    }
    public String getSpecialSpec(){
        return special;
    }
}
