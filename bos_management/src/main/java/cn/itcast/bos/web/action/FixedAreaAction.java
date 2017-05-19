package cn.itcast.bos.web.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
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

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.service.base.FixedAreaService;
import cn.itcast.bos.web.action.commons.BaseAction;
import cn.itcast.crm.domain.Customer;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class FixedAreaAction extends BaseAction<FixedArea> {
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private FixedAreaService fixedAreaService;
	
	// 添加修改定区
	@Action(value="fixedArea_save",results={@Result(name="success",type="redirect",location="./pages/base/fixed_area.html")})
	public String save(){
		fixedAreaService.save(model);
		return SUCCESS;
	}
	
	// 查询全部定区
	@Action(value="fixedArea_findAll",results={@Result(name="success",type="json")})
	public String findAll(){
		List<FixedArea> areaData = fixedAreaService.findAll();
		ActionContext.getContext().getValueStack().push(areaData);
		return SUCCESS;
	}
	
	// 定区分页条件查询
	@Action(value="fixedArea_pageQuery",results={@Result(name="success",type="json")})
	public String pageQuery(){
		Specification<FixedArea> specification = new Specification<FixedArea>() {
			
			@Override
			public Predicate toPredicate(Root<FixedArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				if (StringUtils.isNotBlank(model.getId())) {
					Predicate p1 = cb.equal(root.get("id"), model.getId());
					list.add(p1);
				}
				if (StringUtils.isNotBlank(model.getCompany())) {
					Predicate p2 = cb.like(root.get("company").as(String.class), "%"+model.getId()+"%");
					list.add(p2);
				}
				return cb.and(list.toArray(new Predicate[0]));
			}
		};
		Pageable pageable = new PageRequest(page-1, rows);
		Page<FixedArea> pageData = fixedAreaService.findAll(specification,pageable);
		pushPageDataToValueStack(pageData);
		return SUCCESS;
	}

	// 查询定区关联快递员,多表查询,多对多
	@Action(value="fixedArea_showFixedAreaAndCourier",results={@Result(name="success",type="json")})
	public String showFixedAreaAndCourier(){
		List<Courier> fixedAreaAndCourier = fixedAreaService.showFixedAreaAndCourier(model);
		ActionContext.getContext().getValueStack().push(fixedAreaAndCourier);
		return SUCCESS;
	}
	
	// 查询未关联定区的客户
	@Action(value="fixedArea_findNoassociationcustomers",results={@Result(name="success",type="json")})
	public String findNoassociationcustomers(){
		Collection<? extends Customer> collection = WebClient.create("http://localhost:8088/crm_management/services/customerService/noassociationcustomers")
				.accept(MediaType.APPLICATION_JSON).getCollection(Customer.class);
		ActionContext.getContext().getValueStack().push(collection);
		return SUCCESS;
	}
	
	// 查询已关联定区的客户
	@Action(value="fixedArea_findHasAssociationFixedAreaCustomers",results={@Result(name="success",type="json")})
	public String findHasAssociationFixedAreaCustomers(){
		Collection<? extends Customer> collection = WebClient.create("http://localhost:8088/crm_management/services/customerService/associationfixedareacustomers/"+model.getId())
				.accept(MediaType.APPLICATION_JSON).getCollection(Customer.class);
		ActionContext.getContext().getValueStack().push(collection);
		return SUCCESS;
	}
	
	// 查询已关联定区的分区
	@Action(value="fixedArea_querySubArea",results={@Result(name="success",type="json")})
	public String querySubArea(){
		List<SubArea> subData = fixedAreaService.querySubArea(model);
		ActionContext.getContext().getValueStack().push(subData);
		return SUCCESS;
	}
	
	// 属性驱动接收已选择客户的数组
	private String[] customerIds;
	
	// 客户关联定区
	@Action(value="decidedzone_assigncustomerstodecidedzone",results={@Result(name="success",type="redirect",location="./pages/base/fixed_area.html")})
	public String assigncustomerstodecidedzone(){
		String customerIdStr = StringUtils.join(customerIds,",");
		WebClient.create("http://localhost:8088/crm_management/services/customerService/associationcustomerstofixedarea?customerIdStr="+customerIdStr+"&fixedAreaId="+model.getId()).put(null);
		return SUCCESS;
	}
	
	public void setCustomerIds(String[] customerIds) {
		this.customerIds = customerIds;
	}

	// 属性驱动,接收快递员id和收派时间id
	private Integer courierId;
	private Integer takeTimeId;
	
	//定区关联快递员
	@Action(value="fixedArea_associationCourierToFixedArea",results={@Result(name="success",type="redirect",location="./pages/base/fixed_area.html")})
	public String associationCourierToFixedArea(){
		fixedAreaService.associationCourierToFixedArea(model,courierId,takeTimeId);
		return SUCCESS;
	}
	
	public void setCourierId(Integer courierId) {
		this.courierId = courierId;
	}

	public void setTakeTimeId(Integer takeTimeId) {
		this.takeTimeId = takeTimeId;
	}
}
