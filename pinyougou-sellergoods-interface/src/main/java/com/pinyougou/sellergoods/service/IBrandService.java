package com.pinyougou.sellergoods.service;

import com.pinyougou.entity.PageResult;
import com.pinyougou.pojo.TbBrand;

import java.util.List;

public interface IBrandService {
    public PageResult<TbBrand> findAll();


    public PageResult<TbBrand> findPage(int pageNum, int pageSize);

    void add(TbBrand brand);

    TbBrand findOne(Long id);

    void update(TbBrand brand);

    void delete(Long[] ids);

    PageResult<TbBrand> findPage(TbBrand brand, int pageNum, int pageSize);

}
