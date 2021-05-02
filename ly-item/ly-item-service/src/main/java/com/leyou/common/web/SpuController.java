package com.leyou.common.web;

import com.leyou.common.pojo.Spu;
import com.leyou.common.vo.PageResult;
import com.leyou.common.pojo.SpuDetail;
import com.leyou.common.service.SpuService;
import com.leyou.common.vo.SpuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/spu")
public class SpuController {
    @Autowired
    private SpuService spuService;
    @GetMapping("page")
    public ResponseEntity<PageResult<SpuVo>> page(@RequestParam(required = false) String key, @RequestParam(required = false) Boolean saleable,
                                                  @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "5") Integer row){
        return ResponseEntity.ok(spuService.page(key,saleable,page,row));

    }
    @RequestMapping("detail/{spuId}")
    public ResponseEntity<SpuDetail> getSpuDetailById(@PathVariable Long spuId){
        SpuDetail detail = spuService.getSpuDetailById(spuId);
        return ResponseEntity.ok(detail);
    }
    @RequestMapping("/{id}")
    public ResponseEntity<Spu> getSpuById(@PathVariable Long id){
        Spu spu = spuService.getSpuById(id);
        return ResponseEntity.ok(spu);
    }
}
