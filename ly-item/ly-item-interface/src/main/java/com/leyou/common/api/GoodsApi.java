package com.leyou.common.api;

import com.leyou.common.pojo.SpuDetail;
import com.leyou.common.vo.PageResult;
import com.leyou.common.vo.SpuVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("spu")
public interface GoodsApi {

}
