package cn.itcast.bos.realm;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.PermissionService;
import cn.itcast.bos.service.system.RoleService;
import cn.itcast.bos.service.system.UserService;

//@Service("bosRealm")
public class BosRealm extends AuthorizingRealm {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private PermissionService permissionService;
	
	@Override
	// 认证
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		System.out.println("认证...");
		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
		User user = userService.findByUsername(usernamePasswordToken.getUsername());
		
		if (user == null) {
			// 用户不存在
			return null;
		}else {
			// 用户存在
			// 保存到session
			SecurityUtils.getSubject().getSession().setAttribute("user", user);;
			return new SimpleAuthenticationInfo(user,user.getPassword(),getName());
		}
	}

	@Override
	// 授权
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
		System.out.println("授权管理...");
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		// 根据当前登录用户查询对应角色与权限
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		
		// 查询用户对应的角色
		List<Role> roles = roleService.findByUser(user);
		for (Role role : roles) {
			simpleAuthorizationInfo.addRole(role.getKeyword());
		}
		// 查询用户对应的权限
		List<Permission> permissions = permissionService.findByUser(user);
		for (Permission permission : permissions) {
			simpleAuthorizationInfo.addStringPermission(permission.getKeyword());
		}
		return simpleAuthorizationInfo;
	}

}
