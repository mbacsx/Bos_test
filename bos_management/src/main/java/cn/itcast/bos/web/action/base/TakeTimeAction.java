package cn.itcast.bos.web.action.base;

import java.util.List;

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

import cn.itcast.bos.domain.base.TakeTime;
import cn.itcast.bos.service.base.TakeTimeService;
import cn.itcast.bos.web.action.commons.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class TakeTimeAction extends BaseAction<TakeTime> {
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private TakeTimeService takeTimeService;
	
	@Action(value="takeTime_findAll",results={@Result(name="success",type="json")})
	public String findAll(){
		List<TakeTime> timeData = takeTimeService.findAll();
		ActionContext.getContext().getValueStack().push(timeData);
		return SUCCESS;
	}
	
	@Action(value="takeTime_pageQuery",results={@Result(name="success",type="json")})
	public String pageQuery(){
		Pageable pageable = new PageRequest(page-1, rows);
		Page<TakeTime> pageData = takeTimeService.pageQuery(pageable);
		pushPageDataToValueStack(pageData);
		return SUCCESS;
	}
	
	@Action(value="takeTime_save",results={@Result(name="success",type="redirect",location="./pages/base/take_time.html")})
	public String save(){
		takeTimeService.save(model);
		return SUCCESS;
		
	}
}
