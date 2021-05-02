package com.leyou.goods.client;

import com.leyou.common.api.SpecApi;
import org.springframework.cloud.openfeign.FeignClient;


@FeignClient(value = "item-service")
public interface SpecClient extends SpecApi {
}
