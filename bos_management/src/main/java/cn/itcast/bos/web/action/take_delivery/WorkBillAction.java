package cn.itcast.bos.web.action.take_delivery;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.take_delivery.WorkBill;
import cn.itcast.bos.service.take_delivery.WorkBillService;
import cn.itcast.bos.web.action.commons.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class WorkBillAction extends BaseAction<WorkBill> {
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private WorkBillService workBillService;
	
	@Action(value="workbill_list",results={@Result(name="success",type="json")})
	public String workbillList(){
		List<WorkBill> workbillData = workBillService.findAll();
		ActionContext.getContext().getValueStack().push(workbillData);
		return SUCCESS;
	}
}
