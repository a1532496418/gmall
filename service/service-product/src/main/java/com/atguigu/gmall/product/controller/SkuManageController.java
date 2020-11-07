package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.product.service.ManageService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "sku 数据接口")
@RequestMapping("admin/product")
public class SkuManageController {
    @Autowired
    ManageService manageService;
    //http://api.gmall.com/admin/product/saveSkuInfo
    @PostMapping("saveSkuInfo")
    public Result saveSkuInfo(@RequestBody SkuInfo skuInfo){
        manageService.saveSkuInfo(skuInfo);
        return Result.ok();
    }
    //http://api.gmall.com/admin/product/list/1/10
    @GetMapping("list/{page}/{limit}")
    public Result getSkuInfoList (@PathVariable Long page,
                                  @PathVariable Long limit){
        //有分页的时候需要创建一个Page
        Page<SkuInfo> skuInfoPage = new Page<>(page,limit);
        //调用服务层方法
        IPage<SkuInfo> skuInfoIPage=manageService.getPage(skuInfoPage);
        return Result.ok(skuInfoIPage);
    }
    //  http://api.gmall.com/admin/product/onSale/{skuId}
    @GetMapping("onSale/{skuId}")
    public Result onSale(@PathVariable Long skuId){
        //上架
        manageService.onSale(skuId);
        return Result.ok();
    }
    @GetMapping("cancelSale/{skuId}")
    public Result cancelSale(@PathVariable Long skuId){
        //下架
        manageService.cancelSale(skuId);
        return Result.ok();
    }
}
