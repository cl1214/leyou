package com.leyou.common.web;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/item")
public class ItemController {
    @Autowired
    private ItemService itemService;
    @RequestMapping("/{id}")
    public ResponseEntity<String> saveItem(@PathVariable("id") String id){
        if( "1".equals(id)){
            throw new LyException(ExceptionEnum.PRICE_CANNOT_BE_NULL);
        }
        return ResponseEntity.status(200).body("成功");
    }

    @RequestMapping("goods1")
    public String saveGood(HttpServletRequest request, @RequestBody String data1){
        String data = request.getParameter("data");
        System.out.println(data);
        System.out.println("data1:"+data1);
        return "我爱你!";
    }

}
