package com.leyou.common.service.impl;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.mapper.SpecGroupMapper;
import com.leyou.common.mapper.SpecParamMapper;
import com.leyou.common.pojo.SpecGroup;
import com.leyou.common.pojo.SpecParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SpecService {
    @Autowired
    private SpecGroupMapper groupMapper;
    @Autowired
    private SpecParamMapper paramMapper;
    public List<SpecGroup> getSpecGroupsBycids(Long cid){
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        List<SpecGroup> list = groupMapper.select(specGroup);
        return list;
    }
    public void addSpecGroup(Long cid,String name){
        SpecGroup group = new SpecGroup();
        group.setCid(cid);
        group.setName(name);
        int count = groupMapper.insert(group);
        if(count == 0){
            throw new LyException(ExceptionEnum.GRUOP_ADD_FAILED);
        }
    }
    public void updateSpecGroup(Long id,Long cid,String name){
        SpecGroup group = new SpecGroup();
        group.setId(id);
        group.setCid(cid);
        group.setName(name);
        int count = groupMapper.updateByPrimaryKey(group);
        if(count == 0){
            throw new LyException(ExceptionEnum.GRUOP_UPDATE_FAILED);
        }
    }
    public void delSpecGroup(Long id){
        SpecGroup group = new SpecGroup();
        group.setId(id);
        int count = groupMapper.delete(group);
        if(count == 0){
            throw new LyException(ExceptionEnum.GRUOP_DEL_FAILED);
        }
    }
    public List<SpecParam> getSpecParamByGid(Long gid,Long cid,Boolean searching,Boolean generic){
        SpecParam param = new SpecParam();
        param.setGroupId(gid);
        param.setCid(cid);
        param.setSearching(searching);
        param.setGeneric(generic);
        List<SpecParam> list = paramMapper.select(param);
        if(CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnum.SPECPARAM_NOT_FOUND);
        }
        return list;
    }

    public void addSpecParam(SpecParam param){
      try{
          int count = paramMapper.insert(param);
          if(count == 0){
              throw new LyException(ExceptionEnum.SPECPARAM_ADD_FAILED);
          }
      }catch (Exception e){
          throw new LyException(ExceptionEnum.SPECPARAM_ADD_FAILED);
      }
    }
    public void updateSpecParam(SpecParam param){
        try{
            int count = paramMapper.updateByPrimaryKey(param);
            System.out.println(count);
        }catch (Exception e){
            e.printStackTrace();
            throw new LyException(ExceptionEnum.SPECPARAM_UPDATE_FAILED);

        }
    }
    public void delSpecParam(Long id){
        SpecParam param = new SpecParam();
        param.setId(id);
        try{
            int count = paramMapper.delete(param);
            if(count == 0 ){
                throw new LyException(ExceptionEnum.SPECPARAM_DELETE_FAILED);
            }
        }catch (Exception e){
            throw new LyException(ExceptionEnum.SPECPARAM_DELETE_FAILED);
        }
    }
    public List<SpecGroup> getSpecGroups(Long cid){
        List<SpecGroup> groups = getSpecGroupsBycids(cid);
        groups.forEach(e ->{
            List<SpecParam> params = getSpecParamByGid(e.getId(),null,null,null);
            e.setParams(params);
        });
        return groups;
    }
}
