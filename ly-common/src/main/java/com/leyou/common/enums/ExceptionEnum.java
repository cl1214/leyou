package com.leyou.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum  ExceptionEnum {
    PRICE_CANNOT_BE_NULL(400,"价格不能为空"),
    CATEGORY_NOT_FOUND(404,"商品分类未查到"),
    BRAND_NOT_FOUND(404,"商品品牌未查到"),
    BRAND_INSERT_FAIlED(500,"品牌新增失败"),
    FILE_UPLOAD_FAIlED(500,"文件上传失败"),
    FILE_TYPE_UNMATH(500,"文件类型不匹配"),
    FILE_NOT_IMAGE(500,"无效的图片文件"),
    GRUOP_ADD_FAILED(500,"规格参数组添加失败"),
    GRUOP_UPDATE_FAILED(500,"规格参数组修改失败"),
    GRUOP_DEL_FAILED(500,"规格参数组删除失败"),
    SPECPARAM_NOT_FOUND(404,"规格参数未查到"),
    SPECPARAM_ADD_FAILED(500,"新增规格参数失败"),
    SPECPARAM_UPDATE_FAILED(500,"修改规格参数失败"),
    SPECPARAM_DELETE_FAILED(500,"删除规格参数失败"),
    SPU_NOT_FOUND(404,"商品查询失败!"),
    GOOD_ADD_FAILED(500,"商品新增失败!"),
    GOOD_UPDATE_FAILED(500,"修改商品失败!"),
    ;
    private int code;
    private String msg;
}
