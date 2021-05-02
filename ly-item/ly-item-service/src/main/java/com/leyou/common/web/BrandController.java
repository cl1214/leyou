package com.leyou.common.web;

import com.leyou.common.vo.PageResult;
import com.leyou.common.pojo.Brand;
import com.leyou.common.service.impl.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("brand")
public class BrandController {
    @Autowired
    private BrandService brandService;
    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> getBrandList(@RequestParam(required = false,value = "key") String key,
                                                          @RequestParam(defaultValue = "1",value = "page") Integer page,
                                                          @RequestParam(defaultValue = "5",value = "rows") Integer rows,
                                                          @RequestParam(required = false,value = "sortBy") String sortBy,
                                                          @RequestParam(defaultValue = "false",value = "desc") Boolean desc){
        PageResult<Brand> pageResult = brandService.getBrandList(key,page,rows,sortBy,desc);
        return ResponseEntity.ok(pageResult);
    }
    @PostMapping
    public ResponseEntity<Void> insertBrand(Brand brand, @RequestParam("cids") List<Integer> cids){
        brandService.insrtBrand(brand,cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @RequestMapping("/cid/{cid}")
    public ResponseEntity<List<Brand>> getBrandsByCid(@PathVariable Long cid){
        return ResponseEntity.ok(brandService.getBrandsByCid(cid));
    }
    @RequestMapping("/id")
    public ResponseEntity<Brand> getBrandById(Long id){
        Brand brand = brandService.getBrandById(id);
        return ResponseEntity.ok(brand);
    }
}
