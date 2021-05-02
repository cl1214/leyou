package com.leyou.common.web;

import com.leyou.common.pojo.Category;
import com.leyou.common.service.impl.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/list")
    public ResponseEntity<List<Category>> queryCategoryListByPid(@RequestParam("pid") Long pid){
        return  ResponseEntity.ok(categoryService.queryCategoryListByPid(pid));
    }
    @RequestMapping("names")
    public ResponseEntity<List<String>> getNamesByIds(@RequestBody  List<Long> ids){
        List<String> namesList = categoryService.getNamesByIds(ids);
        return ResponseEntity.ok(namesList);
    }

}
