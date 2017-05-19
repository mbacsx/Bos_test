package cn.itcast.bos.web.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.CourierService;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class CourierAction extends ActionSupport implements ModelDriven<Courier>{
	private static final long serialVersionUID = 1L;
	
	private Courier courier = new Courier();
	
	@Override
	public Courier getModel() {
		return courier;
	}
	
	@Autowired
	private CourierService courierService;
	
	// 添加快递员
	@Action(value="courier_save",results={@Result(name="success",type="redirect",location="./pages/base/courier.html")})
	public String save(){
		courierService.save(courier);
		return SUCCESS;
	}
	
	// 修改快递员
	@Action(value="courier_update",results={@Result(name="success",type="redirect",location="./pages/base/courier.html")})
	public String update(){
		courierService.update(courier);
		return SUCCESS;
	}
	
	// 查询未关联定区的快递员
	@Action(value="courier_findNoAssciation",results={@Result(name="success",type="json")})
	public String findNoAssciation(){
		List<Courier> courierData = courierService.findNoAssciation();
		ActionContext.getContext().getValueStack().push(courierData);
		return SUCCESS;
	}
	
	// 属性驱动
	private int page;
	private int rows;
	
	//分页查询快递员
	@Action(value="courier_pageQuery",results={@Result(name="success",type="json")})
	public String pageQuery(){
		// 封装条件查询对象 Specification
		Specification<Courier> specification = new Specification<Courier>() {
			
			@Override
			// root获取属性字段,CriteriaQuery可以用于简单条件查询，CriteriaBuilder 用于构造复杂条件查询
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// 将所有的条件装进集合
				List<Predicate> list = new ArrayList<>();
				// 判定是否为空
				if(StringUtils.isNotBlank(courier.getCourierNum())){
															// 需给一个数据类型
					// 获得条件一
					Predicate p1 = cb.equal(root.get("courierNum").as(String.class),courier.getCourierNum());
					list.add(p1);
				}
				if(StringUtils.isNotBlank(courier.getCompany())){
					// 获得条件二,模糊查询
					Predicate p2 = cb.like(root.get("company").as(String.class),"%"+courier.getCompany()+"%");
					list.add(p2);
				}
				if(StringUtils.isNotBlank(courier.getType())){
					// 获得条件三,模糊查询
					Predicate p3 = cb.like(root.get("type").as(String.class),"%"+courier.getType()+"%");
					list.add(p3);
				}
				// 多表查询
				// 创建内连接查询对象,courier(root)关联standard
				Join<Object, Object> join = root.join("standard",JoinType.INNER);
				if (courier.getStandard()!=null && StringUtils.isNotBlank(courier.getStandard().getName())) {
					Predicate p4 = cb.like(join.get("name").as(String.class), "%"+courier.getStandard().getName()+"%");
					list.add(p4);
				}
										// 传入一个空数组,相当于泛型的作用
				return cb.and(list.toArray(new Predicate[0]));
			}
		};
		
		Pageable pageable = new PageRequest(page-1, rows);
		Page<Courier> pageData = courierService.pageQuery(specification,pageable);
		Map<String, Object> result = new HashMap<>();
		// 页面接收两条数据,总记录数total,分页数据rows
		result.put("total", pageData.getTotalElements());
		result.put("rows", pageData.getContent());
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}
	
	private String ids;
	private String flag;
	
	@Action(value="courier_updateDeltag",results={@Result(name="success",type="redirect",location="./pages/base/courier.html")})
	public String updateDeltag(){
		String[] idsArray = ids.split(",");
		courierService.updateDeltag(idsArray,flag);
		return SUCCESS;
	}
	
	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
}
