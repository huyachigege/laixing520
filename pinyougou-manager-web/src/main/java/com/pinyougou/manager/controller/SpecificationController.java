package com.pinyougou.manager.controller;
import java.util.List;
import java.util.Map;

import com.pinyougou.entity.CRUDResult;
import com.pinyougou.entity.PageResult;
import com.pinyougou.pojoGroup.SpecificationWithOption;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.sellergoods.service.SpecificationService;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/specification")
public class SpecificationController {

	@Reference
	private SpecificationService specificationService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbSpecification> findAll(){			
		return specificationService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult findPage(int page, int rows){			
		return specificationService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param
	 * @return
	 */
	@RequestMapping("/add")
	public CRUDResult add(@RequestBody SpecificationWithOption specificationWithOption){
		try {
			specificationService.add(specificationWithOption);
			return new CRUDResult(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new CRUDResult(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param
	 * @return
	 */
	@RequestMapping("/update")
	public CRUDResult update(@RequestBody SpecificationWithOption specificationWithOption){
		try {
			specificationService.update(specificationWithOption);
			return new CRUDResult(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new CRUDResult(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public SpecificationWithOption findOne(Long id){
		return specificationService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public CRUDResult delete(Long [] ids){
		try {
			specificationService.delete(ids);
			return new CRUDResult(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new CRUDResult(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param specification
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbSpecification specification, int page, int rows  ){
		return specificationService.findPage(specification, page, rows);		
	}
	@RequestMapping("/selectOptionList")
	public List<Map> selectOptionList(){
		return specificationService.selectOptionList();
	};
	
}
