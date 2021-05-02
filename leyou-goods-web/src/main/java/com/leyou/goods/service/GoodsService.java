package com.leyou.goods.service;

import java.io.IOException;
import java.util.Map;
 public interface GoodsService {
     Map goodsDetail(Long id);
     void asynExcute(Long id) throws IOException;
}
