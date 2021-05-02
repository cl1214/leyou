package com.leyou.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "tb_spec_param")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpecParam {
    Long id;
    Long cid;
    Long groupId;
    String name;
    @Column(name = "`numeric`")
    Boolean numeric;
    String unit;
    Boolean generic;
    Boolean searching;
    String segments;
}
