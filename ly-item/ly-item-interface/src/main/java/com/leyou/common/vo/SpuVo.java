package com.leyou.common.vo;

import lombok.Data;

import java.util.Date;
@Data
public class SpuVo {
    private Long id;
    private String title;
    private String subTitle;
    private Long cid1;
    private Long cid2;
    private Long cid3;
    private Long brandId;
    private Boolean saleable;
    private Boolean valid;
    private Date createTime;
    private Date lastUpdateTime;
    private String cName;
    private String bName;
}
