package com.leyou.common.api;

import com.leyou.common.pojo.Brand;
import org.springframework.web.bind.annotation.*;


@RequestMapping("brand")
public interface BrandApi {
     @RequestMapping("/id")
     Brand getBrandById(@RequestParam Long id);
}
