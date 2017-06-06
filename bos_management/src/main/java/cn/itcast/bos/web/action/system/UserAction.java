package cn.itcast.bos.web.action.system;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.UserService;
import cn.itcast.bos.web.action.commons.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class UserAction extends BaseAction<User> {
	private static final long serialVersionUID = 1L;

	@Autowired
	private UserService userService;

	private String[] roleIds;

	@Action(value = "user_save", results = {
			@Result(name = "success", type = "redirect", location = "/pages/system/userlist.html") })
	public String save() {
		userService.save(model,roleIds);
		return SUCCESS;
	}

	@Action(value = "user_list", results = { @Result(name = "success", type = "json") })
	public String userList() {
		List<User> userData = userService.findAll();
		ActionContext.getContext().getValueStack().push(userData);
		return SUCCESS;
	}

	@Action(value = "user_login", results = { @Result(name = "success", type = "redirect", location = "index.jsp"),
			@Result(name = "login", type = "redirect", location = "login.html") })
	public String login() {
		// 基于shiro实现
		Subject subject = SecurityUtils.getSubject();
		// 用户名和密码信息
		UsernamePasswordToken token = new UsernamePasswordToken(model.getUsername(), model.getPassword());
		try {
			// 登陆成功,保存到session
			subject.login(token);
			// 保存请求的ip
			String ip = getIpAddress(ServletActionContext.getRequest());
			subject.getSession().setAttribute("ip", ip);
			return SUCCESS;
		} catch (Exception e) {
			return LOGIN;
		}
	}

	@Action(value = "user_logout", results = { @Result(name = "success", type = "redirect", location = "login.html") })
	public String logout() {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return SUCCESS;
	}

	public void setRoleIds(String[] roleIds) {
		this.roleIds = roleIds;
	}

	// 可获取反向代理的ip
	public String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}
