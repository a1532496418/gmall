package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseAttrValue;
import com.atguigu.gmall.model.product.SpuInfo;
import com.atguigu.gmall.product.service.ManageService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "后台数据接口测试")
@RestController
@RequestMapping("admin/product")
//@CrossOrigin
public class BaseManagerController {
    @Autowired
    private ManageService manageService;
    //如果不确定，可以写@RequestMapping（）
    @GetMapping("getCategory1")
    public Result getCategory1(){
        return Result.ok(manageService.getCategory1());
    }
    //根据一级分类id，查询二级分类数据集合
    @GetMapping("getCategory2/{category1Id}")
    public Result getCategory2(@PathVariable Long category1Id){
        return Result.ok(manageService.getCategory2(category1Id));
    }
    //根据二级分类id，查询三级分类数据集合
    @GetMapping("getCategory3/{category2Id}")
    public Result getCategory3(@PathVariable Long category2Id){
        return Result.ok(manageService.getCategory3(category2Id));
    }
    //获取平台属性数据集合
    @GetMapping("attrInfoList/{category1Id}/{category2Id}/{category3Id}")
    public Result attrInfoList(@PathVariable Long category1Id,
                               @PathVariable Long category2Id,
                               @PathVariable Long category3Id){
        return Result.ok(manageService.getAttrInfoList(category1Id,category2Id,category3Id));
    }
    //保存平台属性(修改)
    @PostMapping("saveAttrInfo")
    public Result saveAttrInfo(@RequestBody BaseAttrInfo baseAttrInfo){
        //调用服务层方法来保存
        //前台传递是json数据，需要将他转化为对象
        manageService.saveAttrInfo(baseAttrInfo);
        //返回
        return Result.ok();
    }
    //根据属性Id查询平台属性值集合数据
    @GetMapping("getAttrValueList/{attrId}")
    public Result getAttrValueList(@PathVariable Long attrId){
        //attrId=base_attr_value.attr_id=base_attr_info.id
        //select*from base_attr_value where attr_id = attrId
        //最好是先查询baseAttrInfo,再从这里获取到Value的集合
        BaseAttrInfo baseAttrInfo = manageService.getBaseAttrInfo(attrId);
        //List<BaseAttrValue> baseAttrValueList = manageService.getAttrValueList(attrId);
        return Result.ok(baseAttrInfo.getAttrValueList());
    }


}
