package com.leyou.auth.controller;

import com.leyou.auth.conf.JwtProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {
    @GetMapping("/accredit")
    public ResponseEntity<Void> accredit(String username,String password){

        return ResponseEntity.ok().build();
    }
}
