package cn.itcast.bos.service.base.impl;

import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.TakeTimeRepository;
import cn.itcast.bos.domain.base.TakeTime;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.base.TakeTimeService;

@Service
@Transactional
public class TakeTimeServiceImpl implements TakeTimeService {
	
	@Autowired
	private TakeTimeRepository takeTimeRepository;

	@Override
	public List<TakeTime> findAll() {
		return takeTimeRepository.findAll();
	}

	@Override
	public Page<TakeTime> pageQuery(Pageable pageable) {
		return takeTimeRepository.findAll(pageable);
	}

	@Override
	public void save(TakeTime model) {
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		model.setOperatingTime(new Date());// 操作时间
		model.setOperator(user.getNickname());// 操作人
		model.setOperatingCompany(user.getStation());// 单位
		takeTimeRepository.save(model);
	}
}
