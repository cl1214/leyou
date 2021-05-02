package com.leyou.common.api;

import com.leyou.common.pojo.Sku;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@RequestMapping("sku")
public interface SkuApi {
    @RequestMapping("list")
    List<Sku> getSkuBySpuId(@RequestParam Long id);

}
