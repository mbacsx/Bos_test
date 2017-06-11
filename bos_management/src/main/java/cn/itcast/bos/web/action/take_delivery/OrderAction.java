package cn.itcast.bos.web.action.take_delivery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.base.Area;
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
	
	// 查询需要人工分单的订单
	@Action(value="order_findByOrderType",results={@Result(name="success",type="json")})
	public String findByOrderType(){
		List<Order> orderData = orderService.findByOrderType();
		ActionContext.getContext().getValueStack().push(orderData);
		return SUCCESS;
	}
	
	// 人工分单
	@Action(value="order_dispatcher",results={@Result(name="success",type="redirect",location="./pages/take_delivery/dispatcher.html")})
	public String dispatcherOrder(){
		orderService.dispatcherOrder(model);
		return SUCCESS;
	}
	
	private String sendAreaInfo;
	private String recAreaInfo;
	
	// 电话下单
	@Action(value="order_save",results={@Result(name="success",type="redirect",location="./pages/take_delivery/order.html")})
	public String save(){
		// 寄件人区域
		Area sendArea = new Area();
		String[] sendAreaData = sendAreaInfo.split("/");
		sendArea.setProvince(sendAreaData[0]);
		sendArea.setCity(sendAreaData[1]);
		sendArea.setDistrict(sendAreaData[2]);
		// 收件人区域
		Area recArea = new Area();
		String[] recAreaData = recAreaInfo.split("/");
		recArea.setProvince(recAreaData[0]);
		recArea.setCity(recAreaData[1]);
		recArea.setDistrict(recAreaData[2]);
		
		model.setSendArea(sendArea);
		model.setRecArea(recArea);
		
		orderService.saveOrder(model);
		return SUCCESS;
	}

	public void setSendAreaInfo(String sendAreaInfo) {
		this.sendAreaInfo = sendAreaInfo;
	}

	public void setRecAreaInfo(String recAreaInfo) {
		this.recAreaInfo = recAreaInfo;
	}
}
