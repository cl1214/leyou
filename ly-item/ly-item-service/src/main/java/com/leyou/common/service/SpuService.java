package com.leyou.common.service;

import com.leyou.common.pojo.Spu;
import com.leyou.common.vo.PageResult;
import com.leyou.common.pojo.SpuDetail;
import com.leyou.common.vo.SpuVo;

public interface SpuService {
    PageResult<SpuVo> page(String key, Boolean saleable, Integer page, Integer row);
    SpuDetail getSpuDetailById(Long spuId);
    Spu getSpuById(Long id);
}
