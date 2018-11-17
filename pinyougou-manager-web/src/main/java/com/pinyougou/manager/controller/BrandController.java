package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.entity.InsertResult;
import com.pinyougou.entity.PageResult;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.IBrandService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("brand")
public class BrandController {
    @Reference
    private IBrandService brandService;

    @RequestMapping("findAll")
    public PageResult<TbBrand> findAll() {
        return brandService.findAll();
    }

    @RequestMapping("findPage")
    public PageResult<TbBrand> findPage(int page, int size) {
        return brandService.findPage(page, size);
    }

    @RequestMapping("add")
    public InsertResult add(@RequestBody TbBrand brand) {
        try {
            brandService.add(brand);
            return new InsertResult(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new InsertResult(false, "添加失败");
        }

    }
    @RequestMapping("findOne")
    public TbBrand findOne(Long id) {
        return brandService.findOne(id);

    }
    @RequestMapping("update")
    public InsertResult update(@RequestBody TbBrand brand) {
        try {
            brandService.update(brand);
            return new InsertResult(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new InsertResult(false, "修改失败");
        }

    }
    @RequestMapping("delete")
    public InsertResult delete(Long[] ids) {
        try {
            brandService.delete(ids);
            return new InsertResult(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new InsertResult(false, "删除失败");
        }

    }
    @RequestMapping("search")
    public PageResult<TbBrand> search(@RequestBody TbBrand brand,int page, int size) {
        return brandService.findPage(brand,page, size);
    }

}
