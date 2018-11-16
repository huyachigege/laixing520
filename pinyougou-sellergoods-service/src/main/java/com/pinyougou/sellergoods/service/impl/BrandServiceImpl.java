package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.entity.PageResult;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class BrandServiceImpl implements IBrandService {
    @Autowired
    private TbBrandMapper brandMapper;


    @Override
    public List<TbBrand> findAll() {
        return brandMapper.selectByExample(null);
    }

    @Override
    public PageResult<TbBrand> findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        PageInfo page =  new PageInfo(brandMapper.selectByExample(null));
        return new PageResult<>(page.getTotal(),page.getList());
    }

}
