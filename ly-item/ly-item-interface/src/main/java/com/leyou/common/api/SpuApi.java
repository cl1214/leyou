package com.leyou.common.api;

import com.leyou.common.pojo.Spu;
import com.leyou.common.pojo.SpuDetail;
import com.leyou.common.vo.PageResult;
import com.leyou.common.vo.SpuVo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/spu")
public interface SpuApi {
    @RequestMapping("detail/{spuId}")
     SpuDetail getSpuDetailById(@PathVariable Long spuId);
    @GetMapping("page")
    PageResult<SpuVo> page(@RequestParam(required = false) String key, @RequestParam(required = false) Boolean saleable,
                           @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "5") Integer row);
    @RequestMapping("/{id}")
    Spu getSpuById(@PathVariable Long id);
}
