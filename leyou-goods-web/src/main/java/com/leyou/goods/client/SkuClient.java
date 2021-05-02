package com.leyou.goods.client;

import com.leyou.common.api.SkuApi;
import org.springframework.cloud.openfeign.FeignClient;


@FeignClient(value = "item-service")
public interface SkuClient extends SkuApi {
}
