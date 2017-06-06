package cn.itcast.bos.web.action.system;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.service.system.RoleService;
import cn.itcast.bos.web.action.commons.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class RoleAction extends BaseAction<Role> {
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private RoleService roleService;
	
	@Action(value="role_list",results={@Result(name="success",type="json")})
	public String roleList(){
		List<Role> roleData = roleService.findAll();
		ActionContext.getContext().getValueStack().push(roleData);
		return SUCCESS;
	}
	
	private String[] permissionIds;
	private String menuIds;
	
	@Action(value="role_save",results={@Result(name="success",type="redirect",location="/pages/system/role.html")})
	public String save(){
		roleService.save(model,permissionIds,menuIds);
		return SUCCESS;
	}

	public void setPermissionIds(String[] permissionIds) {
		this.permissionIds = permissionIds;
	}

	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}
}
