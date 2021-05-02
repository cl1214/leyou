package com.leyou.sms.configration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@ConfigurationProperties(prefix = "leyou.sms")
@Data
public class SmsProperties {
    private  String accessKeyId;
    private  String accessKeySecret;
    private  String signName;
    private  String verifyCodeTemplate;

}
