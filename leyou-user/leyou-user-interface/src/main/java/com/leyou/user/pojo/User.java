package com.leyou.user.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import java.util.Date;
@Data
@NoArgsConstructor
@Table(name = "tb_user")
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Length(min = 6,max = 30,message = "用户名在6-30位之间")
    private String username;
    @JsonIgnore
    @Length(min = 6,max = 30,message = "密码在6-30位之间")
    private String password;
    @JsonIgnore
    private String salt;
    @Pattern(regexp = "^1[35678]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    private Date created;
}
