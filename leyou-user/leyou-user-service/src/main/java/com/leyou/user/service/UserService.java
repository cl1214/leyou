package com.leyou.user.service;

import com.leyou.user.pojo.User;
import com.sun.org.apache.xpath.internal.operations.Bool;

public interface UserService {
    Boolean checkUser(String date,String type);
    Boolean code(String phone);
    Boolean regist(String username,String password,String phone,String code);
    User queryUser(User user);
}
