package cn.itcast.bos.web.action.transit;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.transit.DeliveryInfo;
import cn.itcast.bos.service.transit.DeliveryInfoService;
import cn.itcast.bos.web.action.commons.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class DeliveryInfoAction extends BaseAction<DeliveryInfo> {
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private DeliveryInfoService deliveryInfoService;
	
	private String transitInfoId;
	
	@Action(value="deliveryInfo_save",results={@Result(name="success",type="redirect",location="./pages/transit/transitinfo.html")})
	public String save(){
		deliveryInfoService.save(transitInfoId,model);
		return SUCCESS;
	}

	public void setTransitInfoId(String transitInfoId) {
		this.transitInfoId = transitInfoId;
	}
}