package cn.itcast.bos.service.system;

import cn.itcast.bos.domain.system.User;

public interface UserService {
	
	User findByUsername(String username);

}
