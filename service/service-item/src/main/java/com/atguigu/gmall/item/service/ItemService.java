package com.atguigu.gmall.item.service;

import java.util.Map;

public interface ItemService {
    /**
     * 获取sku详情信息
     * @return skuId
     */
    Map<String ,Object> getBySkuId(Long skuId);
}
