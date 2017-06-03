package cn.itcast.bos.web.action.take_delivery;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.service.take_delivery.WaybillService;
import cn.itcast.bos.web.action.commons.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class WaybillAction extends BaseAction<WayBill> {
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger.getLogger(WayBill.class);

	@Autowired
	private WaybillService waybillService;

	@Action(value = "waybill_save", results = { @Result(name = "success", type = "json") })
	public String save() {
		// 传递成功与否信息
		Map<String, Object> result = new HashMap<>();
		try {
			// 去除没有id的order对象
			if (model.getOrder() != null && (model.getOrder().getId() == null || model.getOrder().getId() == 0)) {
				model.setOrder(null);
			}
			waybillService.save(model);
			result.put("success", true);
			result.put("msg", "运单保存成功");
			LOGGER.info("运单保存成功,运单号:" + model.getWayBillNum());
		} catch (Exception e) {
			e.printStackTrace();

			result.put("success", false);
			result.put("msg", "运单保存失败");
			LOGGER.error("运单保存失败,运单号:" + model.getWayBillNum(), e);
		}
		// 压栈
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}

	// 分页查询
	@Action(value = "waybill_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {

		// 按id倒序查询
		Pageable pageable = new PageRequest(page - 1, rows, new Sort(new Sort.Order(Sort.Direction.DESC, "id")));
		Page<WayBill> pageData = waybillService.pageQuery(model,pageable);
		pushPageDataToValueStack(pageData);
		return SUCCESS;
	}

	@Action(value = "waybill_findByWayBillNum", results = { @Result(name = "success", type = "json") })
	public String findByWayBillNum() {
		WayBill wayBill = waybillService.findByWayBillNum(model.getWayBillNum());
		Map<String, Object> result = new HashMap<>();
		if (wayBill == null) {
			result.put("success", false);
		} else {
			result.put("success", true);
			result.put("waybillData", wayBill);
		}
		// 压栈
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}
}
