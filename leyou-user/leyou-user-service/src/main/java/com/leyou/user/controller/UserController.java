package com.leyou.user.controller;

import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @RequestMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean> checkUser(@PathVariable String data,@PathVariable String type){
        Boolean bol = userService.checkUser(data,type);
        if(bol == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(bol);
    }
    @RequestMapping("code")
    public ResponseEntity<Void> code(String phone){
        Boolean bol = userService.code(phone);
        if(!bol){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @RequestMapping("register")
    public ResponseEntity<Void> regist(@Valid User user,String code){
        String username = user.getUsername();
        String password = user.getPassword();
        String phone = user.getPhone();
        Boolean bol = userService.regist(username,password,phone,code);
        if(!bol){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @RequestMapping("/queryUser")
    public ResponseEntity<User> queryUser(User user){
        User result = userService.queryUser(user);
        if(result == null){
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.ok(result);
    }
}
