package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.entity.PageResult;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.*;
import com.pinyougou.pojo.TbGoodsExample.Criteria;
import com.pinyougou.pojoGroup.Goods;
import com.pinyougou.sellergoods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private TbGoodsMapper goodsMapper;

    @Autowired

    /**
     * 查询全部
     */
    @Override
    public List<TbGoods> findAll() {
        return goodsMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Autowired
    private TbGoodsDescMapper goodsDescMapper;
    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private TbItemCatMapper itemCatMapper;
    @Autowired
    private TbBrandMapper brandMapper;
    @Autowired
    private TbSellerMapper sellerMapper;

    /**
     * 增加
     */
    @Override
    public void add(Goods goods) {
        //根据商家id获取商家信息
        TbSeller tbSeller = sellerMapper.selectByPrimaryKey(goods.getGoods().getSellerId());
        //根据品牌id获取品牌信息
        TbBrand tbBrand = brandMapper.selectByPrimaryKey(goods.getGoods().getBrandId());
        //获取商品名称
        String goodsName = goods.getGoods().getGoodsName();
        //获取规格模板id
        Long itemCategoryId = goods.getGoods().getCategory3Id();
        //设置商品状态
        goods.getGoods().setAuditStatus("0");
        //存储商品基本信息
        goodsMapper.insert(goods.getGoods());
        //存储商品spu
        goods.getGoodsDesc().setGoodsId(goods.getGoods().getId());
        goodsDescMapper.insert(goods.getGoodsDesc());
        //存储sku
        saveItemList(goods);


    }

    private void setItemValues(TbItem tbItem, Goods goods, TbSeller seller, TbBrand brand) {
        //设置spu品牌名称
        tbItem.setBrand(brand.getName());
        //获取图片url
        List<Map> maps = JSON.parseArray(goods.getGoodsDesc().getItemImages(), Map.class);
        if (maps != null && maps.size() > 0) {
            Object url = maps.get(0).get("url");
            //设置spu图片url
            tbItem.setImage((String) url);
        }
        //设置spu模板id
        tbItem.setCategoryid(goods.getGoods().getCategory3Id());
        TbItemCat tbItemCat = itemCatMapper.selectByPrimaryKey(tbItem.getCategoryid());
        //设置spu模板名称
        tbItem.setCategory(tbItemCat.getName());

        tbItem.setUpdateTime(new Date());
        //设置spu商品id
        tbItem.setGoodsId(goods.getGoods().getId());
        //设置spu商家名称
        tbItem.setSellerId(goods.getGoods().getSellerId());
        //设置spu店家名称
        tbItem.setSeller(seller.getNickName());

    }

    private void saveItemList(Goods goods) {
        TbSeller tbSeller = sellerMapper.selectByPrimaryKey(goods.getGoods().getSellerId());
        //根据品牌id获取品牌信息
        TbBrand tbBrand = brandMapper.selectByPrimaryKey(goods.getGoods().getBrandId());
        //获取商品名称
        String goodsName = goods.getGoods().getGoodsName();
        List<TbItem> itemList = goods.getItemList();

        if (itemList != null && itemList.size() > 0) {
            for (TbItem tbItem : itemList) {
                StringBuffer title = new StringBuffer();
                title.append(tbBrand.getName());
                title.append(goodsName);
                //获取规格集合
                Map<String, Object> map = JSON.parseObject(tbItem.getSpec());
                for (String key : map.keySet()) {
                    title.append(" " + map.get(key));
                }
                //设置spu标题
                tbItem.setTitle(title.toString());
                setItemValues(tbItem, goods, tbSeller, tbBrand);
                tbItem.setCreateTime(new Date());

                //存储spu
                itemMapper.insert(tbItem);
            }
        } else {
            TbItem item = new TbItem();
            item.setTitle(goodsName);
            item.setPrice(goods.getGoods().getPrice());
            item.setNum(9999);
            item.setStatus("1");
            item.setIsDefault("1");
            item.setCreateTime(new Date());
            setItemValues(item, goods, tbSeller, tbBrand);
            itemMapper.insert(item);
        }

    }


    /**
     * 修改
     */
    @Override
    public void update(Goods goods) {

        goodsMapper.updateByPrimaryKey(goods.getGoods());
        goodsDescMapper.updateByPrimaryKey(goods.getGoodsDesc());
        TbItemExample example = new TbItemExample();
        example.createCriteria().andGoodsIdEqualTo(goods.getGoods().getId());
        itemMapper.deleteByExample(example);
        saveItemList(goods);


    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public Goods findOne(Long id) {
        Goods goods = new Goods();
        TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
        TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(id);
        goods.setGoods(tbGoods);
        goods.setGoodsDesc(tbGoodsDesc);
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsIdEqualTo(id);
        List<TbItem> items = itemMapper.selectByExample(example);
        goods.setItemList(items);
        return goods;
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            TbGoods goods = goodsMapper.selectByPrimaryKey(id);
            goods.setIsDelete("1");

            goodsMapper.updateByPrimaryKey(goods);
        }
    }


    @Override
    public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbGoodsExample example = new TbGoodsExample();
        Criteria criteria = example.createCriteria();
        criteria.andIsDeleteIsNull();

        if (goods != null) {
            if (goods.getSellerId() != null && goods.getSellerId().length() > 0) {
//                criteria.andSellerIdLike("%" + goods.getSellerId() + "%");
                criteria.andSellerIdEqualTo(goods.getSellerId());
            }

            if (goods.getGoodsName() != null && goods.getGoodsName().length() > 0) {
                criteria.andGoodsNameLike("%" + goods.getGoodsName() + "%");
            }
            if (goods.getAuditStatus() != null && goods.getAuditStatus().length() > 0) {
                criteria.andAuditStatusLike("%" + goods.getAuditStatus() + "%");
            }
            if (goods.getIsMarketable() != null && goods.getIsMarketable().length() > 0) {
                criteria.andIsMarketableLike("%" + goods.getIsMarketable() + "%");
            }
            if (goods.getCaption() != null && goods.getCaption().length() > 0) {
                criteria.andCaptionLike("%" + goods.getCaption() + "%");
            }
            if (goods.getSmallPic() != null && goods.getSmallPic().length() > 0) {
                criteria.andSmallPicLike("%" + goods.getSmallPic() + "%");
            }
            if (goods.getIsEnableSpec() != null && goods.getIsEnableSpec().length() > 0) {
                criteria.andIsEnableSpecLike("%" + goods.getIsEnableSpec() + "%");
            }
            if (goods.getIsDelete() != null && goods.getIsDelete().length() > 0) {
                criteria.andIsDeleteLike("%" + goods.getIsDelete() + "%");
            }

        }

        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void updateStatus(Long[] ids, String status) {
        for (Long id : ids) {
            TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
            tbGoods.setAuditStatus(status);
            goodsMapper.updateByPrimaryKey(tbGoods);
        }

    }

}
