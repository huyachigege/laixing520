package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.entity.PageResult;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.IBrandService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("brand")
public class BrandController {
    @Reference
    private IBrandService brandService;
    @RequestMapping("findAll")
    public List<TbBrand> findAll(){
        return brandService.findAll();
    }

    @RequestMapping("findPage")
    public PageResult<TbBrand> findPage(int page, int size) {
        return brandService.findPage(page, size);
    }
}
