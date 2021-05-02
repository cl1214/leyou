package com.leyou.common.web;

import com.leyou.common.pojo.Spu;
import com.leyou.common.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoodsController {
    @Autowired
    private GoodsService goodsService;
    @RequestMapping("goods")
    public ResponseEntity<Void> Goods(@RequestBody Spu spu){
        Long id = spu.getId();
        if(id == null){
            saveGoods(spu);
        }else {
            goodsService.updateGood(spu);
        }
        return ResponseEntity.ok().build();
    }
    public void saveGoods(Spu spu){
        goodsService.saveGood(spu);
    }
}
