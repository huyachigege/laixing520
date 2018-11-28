package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.entity.CRUDResult;
import com.pinyougou.entity.PageResult;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojoGroup.Goods;
import com.pinyougou.sellergoods.service.GoodsService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Reference
    private GoodsService goodsService;

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbGoods> findAll() {
        return goodsService.findAll();
    }


    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(int page, int rows) {
        return goodsService.findPage(page, rows);
    }

    /**
     * 增加
     *
     * @param goods
     * @return
     */
    @RequestMapping("/add")
    public CRUDResult add(@RequestBody Goods goods) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        goods.getGoods().setSellerId(name);
        try {
            goodsService.add(goods);
            return new CRUDResult(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new CRUDResult(false, "增加失败");
        }
    }

    /**
     * 修改
     *
     * @param goods
     * @return
     */
    @RequestMapping("/update")
    public CRUDResult update(@RequestBody Goods goods) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Goods goods1 = goodsService.findOne(goods.getGoods().getId());
        if (!name.equals(goods1.getGoods().getSellerId())||!name.equals(goods.getGoods().getSellerId())) {
            return new CRUDResult(false, "非法操作");
        }
        try {
            goodsService.update(goods);
            return new CRUDResult(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new CRUDResult(false, "修改失败");
        }
    }

    /**
     * 获取实体
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public Goods findOne(Long id) {
        return goodsService.findOne(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    public CRUDResult delete(Long[] ids) {
        try {
            goodsService.delete(ids);
            return new CRUDResult(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new CRUDResult(false, "删除失败");
        }
    }

    /**
     * 查询+分页
     *
     * @param goods
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/search")
    public PageResult search(@RequestBody TbGoods goods, int page, int rows) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        goods.setSellerId(name);
        return goodsService.findPage(goods, page, rows);
    }


}
