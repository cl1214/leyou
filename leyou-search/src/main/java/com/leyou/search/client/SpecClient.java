package com.leyou.search.client;

import com.leyou.common.api.SpecApi;
import com.leyou.common.pojo.SpecParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;


@FeignClient(value = "item-service")
public interface SpecClient extends SpecApi {
}
