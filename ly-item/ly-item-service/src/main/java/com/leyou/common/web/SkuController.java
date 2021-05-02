package com.leyou.common.web;

import com.leyou.common.pojo.Sku;
import com.leyou.common.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("sku")
@RestController
public class SkuController {
    @Autowired
    private SkuService skuService;
    @RequestMapping("list")
    public ResponseEntity<List<Sku>> getSkuBySpuId(@RequestParam Long id){
        return ResponseEntity.ok(skuService.getSkuBySpuId(id));
    }
}
