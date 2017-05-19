package cn.itcast.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.TakeTimeRepository;
import cn.itcast.bos.domain.base.TakeTime;
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
		takeTimeRepository.save(model);
	}
}
