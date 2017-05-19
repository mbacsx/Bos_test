package cn.itcast.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.AreaRepository;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.base.AreaService;

@Service
@Transactional
public class AreaServiceImpl implements AreaService {
	
	@Autowired
	private AreaRepository areaRepository;
	
	@Override
	public void batchImport(List<Area> areas) {
		areaRepository.save(areas);
	}

	@Override
	public Page<Area> pageQuery(Specification<Area> specification, Pageable pageable) {
		return areaRepository.findAll(specification,pageable);
	}

	@Override
	public void save(Area area) {
		areaRepository.save(area);
	}

	@Override
	public List<Area> findAll() {
		return areaRepository.findAll();
	}

}
