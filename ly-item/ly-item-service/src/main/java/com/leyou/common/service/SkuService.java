package com.leyou.common.service;

import com.leyou.common.pojo.Sku;

import java.util.List;

public interface SkuService {
    List<Sku> getSkuBySpuId(Long spuId);

}
