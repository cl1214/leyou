package com.leyou.common.api;

import com.leyou.common.pojo.SpecGroup;
import com.leyou.common.pojo.SpecParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("spec")
public interface SpecApi {
    @GetMapping("params")
    List<SpecParam> getSpecParamByGid(@RequestParam(value = "gid",required = false) Long gid,
                                      @RequestParam(value = "cid",required = false) Long cid,@RequestParam(required = false,value = "searching") Boolean searching,@RequestParam(required = false,value = "generic") Boolean generic);
    @RequestMapping("/groups")
     List<SpecGroup> getSpecGroups(@RequestParam Long cid);
}
