
import com.leyou.goods.GoodsWebApplication;
import com.leyou.goods.client.BrandClient;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = GoodsWebApplication.class)
@RunWith(SpringRunner.class)
public class Clienttest {
    private BrandClient brandClient;
}
