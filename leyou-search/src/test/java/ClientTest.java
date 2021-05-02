import com.leyou.common.vo.PageResult;
import com.leyou.common.vo.SpuVo;
import com.leyou.search.LySearchApplication;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.SkuClient;
import com.leyou.search.client.SpuClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.service.impl.SearchServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaAutoServiceRegistration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest(classes = {LySearchApplication.class})
@RunWith(SpringRunner.class)
public class ClientTest {

    @MockBean
    private EurekaAutoServiceRegistration eurekaAutoServiceRegistration;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private SpuClient spuClient;
    @Autowired
    private SkuClient skuClient;
    @Autowired
    private SearchServiceImpl seachService;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private GoodsRepository goodsRepository;

    @Test
    public void categoryTest(){
        try {
            categoryClient.getNamesByIds(Arrays.asList(1L,2L,3L)).forEach(System.out::println);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Test
    public void createIndex(){
        elasticsearchTemplate.createIndex(Goods.class);
        elasticsearchTemplate.putMapping(Goods.class);
    }
    @Test
    public void importGoods(){
        int rows = 100;
        int page = 1;

        while(rows == 100){
           try{
               List<Goods> goods = new ArrayList<>();
               PageResult<SpuVo> pageResult = spuClient.page(null, null, page, rows);
               List<SpuVo> items = pageResult.getItems();
               items.forEach(e -> {
                   try {
                       Goods good = seachService.buildGoods(e);
                       goods.add(good);
                   } catch (IOException ex) {
                       ex.printStackTrace();
                   }

               });
               goodsRepository.saveAll(goods);
               rows = pageResult.getItems().size();
               page++;
           }catch (Exception e){

           }
        }
    }
    @Test
    public void test1(){
        List<Goods> goods = new ArrayList<>();
        PageResult<SpuVo> pageResult = spuClient.page(null, null, 1, 370);
        List<SpuVo> items = pageResult.getItems();
        items.forEach(e -> {
            try {
                Goods good = seachService.buildGoods(e);
                if(good != null){
                    goods.add(good);
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });
        goodsRepository.saveAll(goods);
    }
    @Test
    public void get1(){
    List list = skuClient.getSkuBySpuId(2l);
    System.out.println(list);
    }
}
