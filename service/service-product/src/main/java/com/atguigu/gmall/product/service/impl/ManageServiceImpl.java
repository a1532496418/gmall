package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.product.mapper.*;
import com.atguigu.gmall.product.service.ManageService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


import java.util.List;
@Service
public class ManageServiceImpl implements ManageService {
    //注入mapper
    @Autowired
    private BaseCategory1Mapper baseCategory1Mapper;
    @Autowired
    private BaseCategory2Mapper baseCategory2Mapper;
    @Autowired
    private BaseCategory3Mapper baseCategory3Mapper;
    @Autowired
    private BaseAttrInfoMapper baseAttrInfoMapper;
    @Autowired
    private BaseAttrValueMapper baseAttrValueMapper;
    @Autowired
    private SpuInfoMapper spuInfoMapper;
    @Autowired
    private BaseSaleAttrMapper baseSaleAttrMapper;
    @Autowired
    private SpuImageMapper spuImageMapper;
    @Autowired
    private SpuSaleAttrMapper  spuSaleAttrMapper;
    @Autowired
    private SpuSaleAttrValueMapper spuSaleAttrValueMapper;
    @Autowired
    private SkuInfoMapper skuInfoMapper;
    @Autowired
    private SkuImageMapper skuImageMapper;
    @Autowired
    private SkuAttrValueMapper skuAttrValueMapper;
    @Autowired
    private SkuSaleAttrValueMapper skuSaleAttrValueMapper;

    @Override
    public List<BaseCategory1> getCategory1() {
        //select*from base_category1
        return baseCategory1Mapper.selectList(null);
    }

    @Override
    public List<BaseCategory2> getCategory2(Long category1Id) {
        //select*from base_category2 where category1_id=category1Id
        //查询哪个实体类或者数据库表
        //构建查询条件
        QueryWrapper<BaseCategory2> wrapper = new QueryWrapper<>();
        wrapper.eq("category1_id",category1Id);
        return baseCategory2Mapper.selectList(wrapper);
    }

    @Override
    public List<BaseCategory3> getCategory3(Long category2Id) {
        //  select * from base_category3 where category2_id = category2Id;
        QueryWrapper<BaseCategory3> wrapper = new QueryWrapper<>();
        wrapper.eq("category2_id",category2Id);
        return baseCategory3Mapper.selectList(wrapper);
    }

    /**
     * sku01：Apple iPhone 11 (A2223) 128GB 黑色 移动联通电信4G手机 双卡双待
     * 当前这个商品 有个平台属性 操作系统 有个属性值 IOS
     * 页面根据分类Id查询平台属性，没有属性值
     * 但是，需要通过这个方法将平台属性，以及平台属性值一起查出来！
     * 提示：在制作sku的时候，我们需要选择平台属性，以及平台属性的值！
     * @param category1Id
     * @param category2Id
     * @param category3Id
     * @return
     */
    @Override
    public List<BaseAttrInfo> getAttrInfoList(Long category1Id, Long category2Id, Long category3Id) {
        //select*from base_attr_info where category_id=?category1Id category2Id category3Id
        //判断：mybatis动态sql if（）
        return baseAttrInfoMapper.selectBaseAttrInfoList(category1Id,category2Id,category3Id);
    }
    @Transactional
    @Override
    public void saveAttrInfo(BaseAttrInfo baseAttrInfo) {
    /*
    base_attr_info
    base_attr_value
    什么时候是修改，什么时候是保存
    修改的时候id不为空，保存的时候id为空
     */
            //id是主键
            //update base_attr_info set attr_name=baseAttrInfo.getAttrName() where id = baseAttrInfo.getId()
        if(baseAttrInfo.getId()!=null){
            baseAttrInfoMapper.updateById(baseAttrInfo);
        }else {
            //保存
            //baseAttrInfo.getId=null
            //base_attr_info insert() 当执行完成insert后，id会自动获取到
            baseAttrInfoMapper.insert(baseAttrInfo);
        }
        //先delete，跟attrId相关的属性值，再insert
        //delete from base_attr_value where attr_id=baseAttrInfo.getId();
        baseAttrValueMapper.delete(new QueryWrapper<BaseAttrValue>().eq("attr_id",baseAttrInfo.getId()));

        //int i=1/0
        //baseAttrInfo.getId()!=null
        //base_attr_value
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        //判断集合不为空
        if(!CollectionUtils.isEmpty(attrValueList)){
            //循环遍历
            for (BaseAttrValue baseAttrValue : attrValueList) {
                //插入数据
                //因为前台传递数据的时候，并没有将attr_id传递，但是我们可以通过业务指的当前数据知道是谁
                //attr_id=baseAttrInfo.getId();
                baseAttrValue.setAttrId(baseAttrInfo.getId());
                baseAttrValueMapper.insert(baseAttrValue);
            }
        }
    }

    @Override
    public List<BaseAttrValue> getAttrValueList(Long attrId) {
        //attrId=base_attr_value.attr_id=base_attr_info.id
        //select*from base_attr_value where attr_id = attrId
        return baseAttrValueMapper.selectList(new QueryWrapper<BaseAttrValue>().eq("attr_id",attrId));

    }

    @Override
    public BaseAttrInfo getBaseAttrInfo(Long attrId) {
        BaseAttrInfo baseAttrInfo = baseAttrInfoMapper.selectById(attrId);
        //判断平台属性不为空！
        if(baseAttrInfo!=null){
            //保证业务性，必须在属性里面取到属性值，没有属性的时候不能取到属性值
            //baseAttrInfo没有属性值集合字段
            //给attrValueList赋值！因为控制器要调用平台属性值集合
            //select*from base_attr_value where attr_id = attrId
            baseAttrInfo.setAttrValueList(this.getAttrValueList(attrId));
        }
        return baseAttrInfo;
    }

    @Override
    public IPage<SpuInfo> getSpuInfoPage(Page<SpuInfo> page, SpuInfo spuInfo) {
        //构建查询条件
        //mysql 默认排序
        QueryWrapper<SpuInfo> spuInfoQueryWrapper = new QueryWrapper<>();
        spuInfoQueryWrapper.eq("category3_id",spuInfo.getCategory3Id());
        spuInfoQueryWrapper.orderByDesc("id");
        return spuInfoMapper.selectPage(page,spuInfoQueryWrapper);
    }

    @Override
    public List<BaseSaleAttr> getBaseSaleAttrList() {
       return baseSaleAttrMapper.selectList(null);
    }

    //事务
    @Transactional
    @Override
    public void saveSpuInfo(SpuInfo spuInfo) {
        //spuInfo 商品表
        spuInfoMapper.insert(spuInfo);
        //spuImage 商品图片表
        List<SpuImage> spuImageList = spuInfo.getSpuImageList();
        //判空
        if(spuImageList!=null&&spuImageList.size()>0){
            //循环遍历
            for (SpuImage spuImage : spuImageList) {
                //插入数据
                //因为前台传递数据的时候，并没有将spu_id传递，但是我们可以通过业务指的当前数据知道是谁
                //spu_id=baseAttrInfo.getId();
                spuImage.setSpuId(spuInfo.getId());
                spuImageMapper.insert(spuImage);
            }
        }
            //spuSaleAttr销售属性表
        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
        //判空
        if(spuSaleAttrList!=null&&spuSaleAttrList.size()>0){
            //循环遍历
            for (SpuSaleAttr spuSaleAttr : spuSaleAttrList) {
                spuSaleAttr.setSpuId(spuInfo.getId());
                spuSaleAttrMapper.insert(spuSaleAttr);

                //spuSaleAttrValue销售属性值
                List<SpuSaleAttrValue> spuSaleAttrValueList = spuSaleAttr.getSpuSaleAttrValueList();
                //判空
                if(spuSaleAttrValueList!=null&&spuSaleAttrValueList.size()>0){
                    for (SpuSaleAttrValue spuSaleAttrValue : spuSaleAttrValueList) {
                        spuSaleAttrValue.setSpuId(spuInfo.getId());
                        spuSaleAttrValue.setSaleAttrName(spuSaleAttr.getSaleAttrName());
                        spuSaleAttrValueMapper.insert(spuSaleAttrValue);
                    }
                }

            }
        }
    }
    //  select  * from spu_image where spu_id = 17
    @Override
    public List<SpuImage> getSpuImageList(Long spuId) {
        QueryWrapper<SpuImage> spuImageQueryWrapper = new QueryWrapper<>();
        spuImageQueryWrapper.eq("spu_id",spuId);
        return spuImageMapper.selectList(spuImageQueryWrapper);
    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrList(Long spuId) {
        return spuSaleAttrMapper.selectSpuSaleAttrList(spuId);
    }

    @Override
    @Transactional
    public void saveSkuInfo(SkuInfo skuInfo) {
      //sku_info,sku_image,sku_attr_value,sku_sale_attr_value

        //sku info
        skuInfoMapper.insert(skuInfo);
        //sku image
        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        if(!CollectionUtils.isEmpty(skuImageList)){
            for (SkuImage skuImage : skuImageList) {
                skuImage.setSkuId(skuInfo.getId());
                skuImageMapper.insert(skuImage);
            }
        }
        //sku_attr_value
        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        if(!CollectionUtils.isEmpty(skuAttrValueList)){
            for (SkuAttrValue skuAttrValue : skuAttrValueList) {
                skuAttrValue.setSkuId(skuInfo.getId());
                skuAttrValueMapper.insert(skuAttrValue);
            }
        }
        //sku_sale_attr_value
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        if(!CollectionUtils.isEmpty(skuSaleAttrValueList)){
            for (SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValueList) {
                skuSaleAttrValue.setSkuId(skuInfo.getId());
                skuSaleAttrValue.setSpuId(skuInfo.getSpuId());
                skuSaleAttrValueMapper.insert(skuSaleAttrValue);
            }
        }

    }

    @Override
    public IPage<SkuInfo> getPage(Page<SkuInfo> page) {
        QueryWrapper<SkuInfo> skuInfoQueryWrapper = new QueryWrapper<>();
        skuInfoQueryWrapper.orderByDesc("id");
        return skuInfoMapper.selectPage(page,skuInfoQueryWrapper);
    }

    @Override
    public void onSale(Long skuId) {
        //  update sku_info set is_sale = 1 where id = skuId
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setId(skuId);
        skuInfo.setIsSale(1);
        skuInfoMapper.updateById(skuInfo);
    }

    @Override
    public void cancelSale(Long skuId) {
        //  update sku_info set is_sale = 0 where id=skuId
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setId(skuId);
        skuInfo.setIsSale(0);
        skuInfoMapper.updateById(skuInfo);
    }


}
