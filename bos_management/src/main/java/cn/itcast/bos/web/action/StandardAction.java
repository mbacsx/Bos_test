package cn.itcast.bos.web.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.StandardService;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class StandardAction extends ActionSupport implements ModelDriven<Standard> {
	private static final long serialVersionUID = 1L;

	private Standard standard = new Standard();

	@Override
	public Standard getModel() {
		return standard;
	}

	@Autowired
	private StandardService standardService;

	@Action(value = "standard_save", results = {
			@Result(name = "success", type = "redirect", location = "./pages/base/standard.html") })
	public String save() {
		standardService.save(standard);
		return SUCCESS;
	}

	// 属性驱动
	private int page;
	private int rows;

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	// 分页查询
	@Action(value = "standard_pageQuery", results = {@Result(name="success",type="json")})
	public String pageQuery() {
		//分页查询从0开始,所以page要-1
		Pageable pageable = new PageRequest(page-1, rows);
		Page<Standard> pageData = standardService.pageQuery(pageable);
		Map<String, Object> result = new HashMap<>();
		// 页面接收两条数据,总记录数total,分页数据rows
		result.put("total", pageData.getTotalElements());
		result.put("rows", pageData.getContent());
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}
	
	// 查询全部
	@Action(value="standard_findAll",results={@Result(name="success",type="json")})
	public String findAll(){
		List<Standard> allStandard = standardService.findAll();
		ActionContext.getContext().getValueStack().push(allStandard);
		return SUCCESS;
	}
}
