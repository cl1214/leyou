import com.leyou.sms.LeyouSmsApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LeyouSmsApplication.class)
public class RedisTest {
    @Autowired
    private StringRedisTemplate redisTemplate;
   /* @Autowired
    private  RedisTemplate redisTemplate;*/
    @Test
    public void test1(){
        redisTemplate.opsForValue().set("key","cl");
        System.out.println(redisTemplate.opsForValue().get("key"));
    }

    @Test
    public void test2(){
        redisTemplate.opsForValue().set("key2","cl2",5, TimeUnit.SECONDS);
    }
   @Test
    public void test3(){
       BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps("user");
       hashOps.put("name","clll");
       hashOps.put("age","1999");

        for(Map.Entry entry :hashOps.entries().entrySet()) {
            System.out.println(entry.getKey() + ":"+entry.getValue());

        }

   }

}
