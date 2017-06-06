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

import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.service.system.PermissionService;
import cn.itcast.bos.web.action.commons.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class PermissionAction extends BaseAction<Permission> {
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private PermissionService permissionService;
	
	@Action(value="permission_list",results={@Result(name="success",type="json")})
	public String permissionList(){
		List<Permission> permissionData = permissionService.findAll();
		ActionContext.getContext().getValueStack().push(permissionData);
		return SUCCESS;
	}
	
	@Action(value="permission_save",results={@Result(name="success",type="redirect",location="/pages/system/permission.html")})
	public String save(){
		permissionService.save(model);
		return SUCCESS;
	}
}
