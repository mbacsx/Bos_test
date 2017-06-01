package cn.itcast.bos.web.action.take_delivery;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.service.take_delivery.OrderService;
import cn.itcast.bos.web.action.commons.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class OrderAction extends BaseAction<Order> {
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private OrderService orderService;
	
	@Action(value="order_findByOrderNum",results={@Result(name="success",type="json")})
	public String findByOrderNum(){
		Order order = orderService.findByOrderNum(model.getOrderNum());
		Map<String, Object> result = new HashMap<>();
		if (order == null) {
			result.put("success", false);
		}else {
			result.put("success", true);
			result.put("orderData", order);
		}
		// 压栈
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}
}
