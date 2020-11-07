package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ManageService {
    /**
     * 查询所有的一级分类
     * @return
     */
    List<BaseCategory1> getCategory1();

    /**
     * 根据一级分类Id查询二级分类数据
     * @param category1Id
     * @return
     */
    List<BaseCategory2> getCategory2(Long category1Id);

    /**
     * 根据二级分类Id查询三级分类数据
     * @param category2Id
     * @return
     */
    List<BaseCategory3> getCategory3(Long category2Id);


    /**
     *获取到平台属性数据集合
     * @param category1Id
     * @param category2Id
     * @param category3Id
     * @return
     */
    List<BaseAttrInfo> getAttrInfoList(Long category1Id,Long category2Id,Long category3Id);

    /**
     * 保存平台属性数据
     * @param baseAttrInfo
     */
    void saveAttrInfo(BaseAttrInfo baseAttrInfo);

    /**
     * 根据属性id查询平台属性值集合数据
     * @param attrId
     * @return
     */
    List<BaseAttrValue> getAttrValueList(Long attrId);

    //根据平台属性id查询平台属性对象
    BaseAttrInfo getBaseAttrInfo(Long attrId);


    //  springmvc 对象传值  按照三级分类Id带分页的查询
    IPage<SpuInfo> getSpuInfoPage(Page<SpuInfo> page, SpuInfo spuInfo);

    /**
     * 查询所有的销售属性数据
     * @return
     */
    List<BaseSaleAttr> getBaseSaleAttrList();

    /**
     * 保存商品数据
     * @param spuInfo
     */
    void saveSpuInfo(SpuInfo spuInfo);

    /**
     * 根据spuId 查询spuImageList
     * @param spuId
     * @return
     */
    List<SpuImage> getSpuImageList(Long spuId);

    /**
     * 根据spuId 查询销售属性集合
     * @param spuId
     * @return
     */
    List<SpuSaleAttr> getSpuSaleAttrList(Long spuId);

    /**
     * 保存sku数据
     * @param skuInfo
     */
    void saveSkuInfo(SkuInfo skuInfo);

    /**
     * 带分页的查询所有sku数据
     * @param page
     * @return
     */
    IPage<SkuInfo> getPage(Page<SkuInfo> page);
    //上架
    void onSale(Long skuId);
    //下架
    void cancelSale(Long skuId);
}
