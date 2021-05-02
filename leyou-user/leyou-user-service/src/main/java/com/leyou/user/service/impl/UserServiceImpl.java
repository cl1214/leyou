package com.leyou.user.service.impl;

import com.leyou.common.utils.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import com.leyou.user.utils.CodecUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private AmqpTemplate amqpTemplate;
    static final String KEY_PREFIX = "user:code:phone:";

    @Override
    public Boolean checkUser(String data, String type) {
        User user = new User();
        switch (type){
            case "1" :
                user.setUsername(data);
                break;
            case  "2":
                user.setPhone(data);
        }
        return userMapper.selectCount(user) != 1;
    }

    @Override
    public Boolean code(String phone) {
        String code = NumberUtils.generateCode(6);
        try {
            //发送消息
            Map map = new HashMap();
            map.put("phone",phone);
            map.put("code",code);
            amqpTemplate.convertAndSend("leyou.sms.exchage","sms.verify.code",map);
            //验证码存储进redis
            redisTemplate.opsForValue().set(KEY_PREFIX+phone,code,5, TimeUnit.MINUTES);
            System.out.println(redisTemplate.opsForValue().get(KEY_PREFIX+phone));
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Boolean regist(String username, String password, String phone, String code) {
        String key = KEY_PREFIX + phone;
        //校验验证码
        String codeStr = redisTemplate.opsForValue().get(key);
        if(!StringUtils.equals(codeStr,code)){
            return false;
        }
        User user = new User();
        user.setUsername(username);
        user.setPhone(phone);
        //生成盐
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        //加密
        user.setPassword(CodecUtils.generate(password,salt));
        user.setCreated(new Date());
        //插入
        boolean bol = userMapper.insertSelective(user) == 1;
        //删除redis验证码
        if(bol){
            redisTemplate.delete(key);
        }
        return bol;
    }

    public User queryUser(User user){
        String userName = user.getUsername();
        String password = user.getPassword();
        User user1 = new User();
        user1.setUsername(userName);
        User result = userMapper.selectOne(user1);
        if(result == null){
            return null;
        }
        if(!CodecUtils.generate(password,result.getSalt()).equals(result.getPassword())){
            return null;
        }
        return result;
    }
}
