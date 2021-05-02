package com.leyou.goods.listener;

import com.leyou.goods.service.GoodsService;
import com.leyou.goods.service.impl.GoodsServiceImpl;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GoodsListener {
    @Autowired
    private GoodsServiceImpl goodsService;
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "leyou.item.web.save.queue",durable = "true"),
            exchange = @Exchange(value = "leyou.item.exchange",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = {"item.insert","item.update"}
    ))
    public void save(Long id){
        if(id != null){
            System.out.println(id);
            goodsService.createGoodsHtml(id);
        }
    }
}
