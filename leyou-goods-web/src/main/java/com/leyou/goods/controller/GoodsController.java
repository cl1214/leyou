package com.leyou.goods.controller;

import com.leyou.goods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/item")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;
    @RequestMapping("{id}.html")
    public String goodsDetail(Model model,@PathVariable Long id) throws IOException {
        Map map = goodsService.goodsDetail(id);
        model.addAllAttributes(map);
        //页面静态化
        goodsService.asynExcute(id);
        return "item";
    }
}
