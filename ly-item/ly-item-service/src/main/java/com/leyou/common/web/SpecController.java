package com.leyou.common.web;

import com.leyou.common.pojo.SpecGroup;
import com.leyou.common.pojo.SpecParam;
import com.leyou.common.service.impl.SpecService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("spec")
public class SpecController {
    @Autowired
    private SpecService specService;
    @RequestMapping("/groups/{cid}")
    public ResponseEntity getSpecGroup(@PathVariable("cid") Long cid){
        List<SpecGroup> specGroups = specService.getSpecGroupsBycids(cid);
        return ResponseEntity.ok(specGroups);
    }
    @RequestMapping("/group")
    public ResponseEntity handleGroup(@RequestBody SpecGroup group ){
        Long cid = group.getCid();
        Long id = group.getId();
        String name = group.getName();
        //新增
        if( id == null && cid != null && name !=null ){
            return addSpecGroup(cid,name);
        }
        //修改
        if( id != null && cid != null && name !=null ){
            return updateSpecGroup(id,cid,name);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("操作失败！");
    }
    public ResponseEntity  addSpecGroup(Long cid,String name){
        specService.addSpecGroup(cid,name);
        return ResponseEntity.status(HttpStatus.CREATED).body("新增成功！");
    }

    public ResponseEntity  updateSpecGroup(Long id, Long cid,String name){
        specService.updateSpecGroup(id,cid,name);
        return ResponseEntity.status(HttpStatus.OK).body("修改成功！");
    }
    @RequestMapping("/group/{id}")
    public ResponseEntity delSpecGroup( @PathVariable  Long id){
        specService.delSpecGroup(id);
        return ResponseEntity.status(HttpStatus.OK).body("删除成功！");
    }
    @GetMapping("params")
    public ResponseEntity getSpecParamByGid(@RequestParam(value = "gid",required = false) Long gid,
                                            @RequestParam(value = "cid",required = false) Long cid,@RequestParam(required = false,value = "searching") Boolean searching,@RequestParam(required = false,value = "generic") Boolean generic){
        List<SpecParam> list = specService.getSpecParamByGid(gid,cid,searching,generic);
        return ResponseEntity.ok().body(list);
    }
    @RequestMapping("/param/{id}")
    public ResponseEntity delParam( @PathVariable("id") Long id){
        //删除
        if(id != null){
            specService.delSpecParam(id);
        }
        return ResponseEntity.ok().body("操作成功");
    }

    @RequestMapping("/param")
    public ResponseEntity param(@RequestBody SpecParam param){
        Long id = param.getId();
        if(id == null){
            specService.addSpecParam(param);
        }else {
            specService.updateSpecParam(param);
        }

        return ResponseEntity.ok().body("操作成功");
    }
    @RequestMapping("/groups")
    public ResponseEntity<List<SpecGroup>> getSpecGroups(@RequestParam Long cid){
        List<SpecGroup> specGroups = specService.getSpecGroups(cid);
        return ResponseEntity.ok(specGroups);
    }
}
