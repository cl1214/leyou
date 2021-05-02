package com.leyou.common.mapper;

import com.leyou.common.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<Brand> {
    @Insert("insert into tb_category_brand values(#{cid},#{bid})")
    int insertBrandCategory(@Param("bid") Long id, @Param("cid") Integer cid);
     @Select("select b.* from tb_category_brand a  left join tb_brand b on a.brand_id = b.id where a.category_id = #{cid}")
     List<Brand> getBrandsByCid(@Param("cid") Long cid);
}
