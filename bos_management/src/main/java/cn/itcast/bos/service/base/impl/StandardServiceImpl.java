package cn.itcast.bos.service.base.impl;

import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.StandardRepository;
import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.base.StandardService;

@Service
@Transactional
public class StandardServiceImpl implements StandardService {
	
	@Autowired
	private StandardRepository standardRespository;
	
	@Override
	@CacheEvict(value="standard",allEntries=true)
	public void save(Standard standard) {
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		standard.setOperatingTime(new Date());// 操作时间
		standard.setOperator(user.getNickname());// 操作人
		standard.setOperatingCompany(user.getStation());// 单位
		standardRespository.save(standard);
	}

	@Override
	@Cacheable(value="standard",key="#pageable.pageNumber+'_'+#pageable.pageSize")
	public Page<Standard> pageQuery(Pageable pageable) {
		return standardRespository.findAll(pageable);
	}

	@Override
	@Cacheable("standard")
	public List<Standard> findAll() {
		return standardRespository.findAll();
	}
	
}
