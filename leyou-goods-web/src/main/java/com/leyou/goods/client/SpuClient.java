package com.leyou.goods.client;

import com.leyou.common.api.SpuApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "item-service")
public interface SpuClient extends SpuApi {

}
