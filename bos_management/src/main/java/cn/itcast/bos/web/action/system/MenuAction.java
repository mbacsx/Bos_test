package cn.itcast.bos.web.action.system;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.MenuService;
import cn.itcast.bos.web.action.commons.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class MenuAction extends BaseAction<Menu>{
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private MenuService menuService;
	
	// 动态菜单的展示
	@Action(value="menu_dynamicMenu",results={@Result(name="success",type="json")})
	public String dynamicMenu(){
		// 从session取出当前登陆的用户信息
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		List<Menu> menuData = menuService.findByUser(user);
		ActionContext.getContext().getValueStack().push(menuData);
		return SUCCESS;
	}
	
	@Action(value="menu_list",results={@Result(name="success",type="json")})
	public String menuList(){
		List<Menu> menuData = menuService.findAll();
		ActionContext.getContext().getValueStack().push(menuData);
		return SUCCESS;
	}
	
	@Action(value="menu_save",results={@Result(name="success",type="redirect",location="/pages/system/menu.html")})
	public String save(){
		menuService.save(model);
		return SUCCESS;
	}
}
