package com.leyou.common.service.impl;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.mapper.SkuMapper;
import com.leyou.common.mapper.SpuDetailMapper;
import com.leyou.common.mapper.SpuMapper;
import com.leyou.common.mapper.StockMapper;
import com.leyou.common.pojo.Sku;
import com.leyou.common.pojo.Spu;
import com.leyou.common.pojo.SpuDetail;
import com.leyou.common.pojo.Stock;
import com.leyou.common.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Override
    @Transactional
    public void saveGood(Spu spu) {
        //保存spu
        spu.setSaleable(true);
        spu.setValid(true);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(new Date());
        int count1 = spuMapper.insert(spu);
        if(count1 == 0){
            throw new LyException(ExceptionEnum.GOOD_ADD_FAILED);
        }
        //保存spu_detail
        SpuDetail spuDetail = spu.getSpuDetail();
        spuDetail.setSpuId(spu.getId());
        int count2 = spuDetailMapper.insert(spuDetail);
        if(count2 == 0){
            throw new LyException(ExceptionEnum.GOOD_ADD_FAILED);
        }
        //保存sku
        List<Sku> skuList = spu.getSkus();
        skuList.forEach(e -> {
            e.setSpuId(spu.getId());
            e.setCreateTime(new Date());
            e.setLastUpdateTime(new Date());
        });
        int count3 = skuMapper.insertList(skuList);
        if(count3 == 0){
            throw new LyException(ExceptionEnum.GOOD_ADD_FAILED);
        }
        //保存库存
        List<Stock> stockList = skuList.stream().map(e -> {
            Stock stock = new Stock();
            stock.setSkuId(e.getId());
            stock.setStock(e.getStock());
            return stock;
        }).collect(Collectors.toList());
        int count4 = stockMapper.insertList(stockList);
        if(count4 == 0){
            throw new LyException(ExceptionEnum.GOOD_ADD_FAILED);
        }
       sendMessage("insert",spu.getId());
    }

    public void sendMessage(String type,Long spuId){
        try {
            System.out.println("id:" + spuId);
            amqpTemplate.convertAndSend("item."+type,spuId);

        }catch (Exception e){

        }
    }

    @Transactional
    @Override
    public void updateGood(Spu spu) {
        //更新spu
        spu.setLastUpdateTime(new Date());
        int count1 = spuMapper.updateByPrimaryKey(spu);
        if(count1 == 0){
            throw new LyException(ExceptionEnum.GOOD_UPDATE_FAILED);
        }

        //更新spuDetail
        int count2 = spuDetailMapper.updateByPrimaryKey(spu.getSpuDetail());
        if(count2 == 0){
            throw new LyException(ExceptionEnum.GOOD_UPDATE_FAILED);
        }

        //删除sku
        Sku sku = new Sku();
        sku.setSpuId(spu.getId());

        //查询sku
        List<Sku> skuList = skuMapper.select(sku);

        //删除sku
        int count3 = skuMapper.delete(sku);
        if(count3 == 0){
            throw new LyException(ExceptionEnum.GOOD_UPDATE_FAILED);
        }
        List<Long> skuIds = skuList.stream().map(e  ->{
            return e.getId();
        }).collect(Collectors.toList());

        //删除stock
        Example example = new Example(Stock.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("skuId",skuIds);
        int count4 = stockMapper.deleteByExample(example);
        if(count4 != skuIds.size()){
            throw new LyException(ExceptionEnum.GOOD_UPDATE_FAILED);
        }

        //新增sku
        List<Sku> skusList = spu.getSkus();
        skusList.forEach( e ->{
            e.setSpuId(spu.getId());
            e.setCreateTime(new Date());
            e.setLastUpdateTime(new Date());
        });
        int coun5 = skuMapper.insertList(skusList);
        if(coun5 != skusList.size()){
            throw new LyException(ExceptionEnum.GOOD_UPDATE_FAILED);
        }

        //新增stock
        List<Stock> stockList = skusList.stream().map(e ->{
            Stock stock = new Stock();
            stock.setSkuId(e.getId());
            stock.setStock(e.getStock());
            return stock;
        }).collect(Collectors.toList());
        int count6 = stockMapper.insertList(stockList);
        if(count6 != stockList.size()){
            throw new LyException(ExceptionEnum.GOOD_UPDATE_FAILED);
        }
        sendMessage("update",spu.getId());
    }
}
