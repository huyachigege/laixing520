package com.pinyougou.sellergoods.service.impl;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.pinyougou.entity.PageResult;
import com.pinyougou.mapper.TbSpecificationOptionMapper;
import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.pojo.TbSpecificationOptionExample;
import com.pinyougou.pojoGroup.SpecificationWithOption;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbSpecificationMapper;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationExample;
import com.pinyougou.pojo.TbSpecificationExample.Criteria;
import com.pinyougou.sellergoods.service.SpecificationService;


/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class SpecificationServiceImpl implements SpecificationService {

	@Autowired
	private TbSpecificationMapper specificationMapper;
	@Autowired
	private TbSpecificationOptionMapper specificationOptionMapper;

	/**
	 * 查询全部
	 */
	@Override
	public List<TbSpecification> findAll() {
		return specificationMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbSpecification> page=   (Page<TbSpecification>) specificationMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(SpecificationWithOption specificationWithOption) {
		TbSpecification specification = specificationWithOption.getSpecification();
		specificationMapper.insert(specification);
		Long id = specification.getId();
		List<TbSpecificationOption> specificationOptionList = specificationWithOption.getSpecificationOptionList();
		try {
			for (TbSpecificationOption tbSpecificationOption : specificationOptionList) {
				tbSpecificationOption.setId(id);
				specificationOptionMapper.insert(tbSpecificationOption);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * 修改
	 * @param specificationWithOption
	 */
	@Override
	public void update(SpecificationWithOption specificationWithOption){
		TbSpecification specification = specificationWithOption.getSpecification();
		List<TbSpecificationOption> specificationOptionList = specificationWithOption.getSpecificationOptionList();
		specificationMapper.updateByPrimaryKey(specification);
		Long specId = specification.getId();
		TbSpecificationOptionExample example = new TbSpecificationOptionExample();
		TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
		criteria.andSpecIdEqualTo(specId);
		specificationOptionMapper.deleteByExample(example);

		for (TbSpecificationOption tbSpecificationOption : specificationOptionList) {
			tbSpecificationOption.setSpecId(specId);
			specificationOptionMapper.insert(tbSpecificationOption);
		}
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public SpecificationWithOption findOne(Long id){
		SpecificationWithOption specificationWithOption = new SpecificationWithOption();
		specificationWithOption.setSpecification(specificationMapper.selectByPrimaryKey(id));
		TbSpecificationOptionExample example = new TbSpecificationOptionExample();
		TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
		criteria.andSpecIdEqualTo(id);
		specificationWithOption.setSpecificationOptionList(specificationOptionMapper.selectByExample(example));
		return specificationWithOption;
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		TbSpecificationOptionExample example = new TbSpecificationOptionExample();
		TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
		criteria.andSpecIdIn(Arrays.asList(ids));
		specificationOptionMapper.deleteByExample(example);
		for(Long id:ids){
			specificationMapper.deleteByPrimaryKey(id);
		}
	}
	
	
		@Override
	public PageResult findPage(TbSpecification specification, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbSpecificationExample example=new TbSpecificationExample();
		Criteria criteria = example.createCriteria();
		
		if(specification!=null){			
						if(specification.getSpecName()!=null && specification.getSpecName().length()>0){
				criteria.andSpecNameLike("%"+specification.getSpecName()+"%");
			}
	
		}
		
		Page<TbSpecification> page= (Page<TbSpecification>)specificationMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	public List<Map> selectOptionList() {
		return specificationMapper.selectOptionList();
	}

}
