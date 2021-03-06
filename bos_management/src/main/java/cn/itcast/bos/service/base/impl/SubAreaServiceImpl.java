package cn.itcast.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.SubAreaRepository;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.service.base.SubAreaService;

@Service
@Transactional
public class SubAreaServiceImpl implements SubAreaService {
	
	@Autowired
	private SubAreaRepository subAreaRepository;

	@Override
	@CacheEvict(value="subArea",allEntries=true)
	public void save(SubArea model) {
		subAreaRepository.save(model);
	}

	@Override
	public Page<SubArea> findAll(Specification<SubArea> specification, Pageable pageable) {
		return subAreaRepository.findAll(specification, pageable);
	}

	@Override
	@Cacheable("subArea")
	public List<SubArea> findAll() {
		return subAreaRepository.findAll();
	}
}
