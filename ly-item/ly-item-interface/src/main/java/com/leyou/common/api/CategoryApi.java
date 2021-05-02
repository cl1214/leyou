package com.leyou.common.api;

import com.leyou.common.pojo.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/category")
public interface CategoryApi {

    @GetMapping("/list")
    List<Category> queryCategoryListByPid(@RequestParam("pid") Long pid);
    @RequestMapping("names")
    List<String> getNamesByIds(@RequestBody List<Long> ids);

}
