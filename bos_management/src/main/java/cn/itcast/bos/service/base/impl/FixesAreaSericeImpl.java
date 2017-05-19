package cn.itcast.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.CourierRepository;
import cn.itcast.bos.dao.base.FixedAreaRepository;
import cn.itcast.bos.dao.base.TakeTimeRepository;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.domain.base.TakeTime;
import cn.itcast.bos.service.base.FixedAreaService;

@Service
@Transactional
public class FixesAreaSericeImpl implements FixedAreaService {
	
	@Autowired
	private FixedAreaRepository fixedAreaRepository;

	@Override
	public void save(FixedArea model) {
		fixedAreaRepository.save(model);
	}

	@Override
	public Page<FixedArea> findAll(Specification<FixedArea> specification, Pageable pageable) {
		return fixedAreaRepository.findAll(specification, pageable);
	}
	
	// 注入快递员和收派标准的dao
	@Autowired
	private CourierRepository courierRepository;
	@Autowired
	private TakeTimeRepository takeTimeRepository;
	
	// 定区关联快递员
	@Override
	public void associationCourierToFixedArea(FixedArea model, Integer courierId, Integer takeTimeId) {
		FixedArea fixedArea = fixedAreaRepository.findOne(model.getId());
		Courier courier = courierRepository.findOne(courierId);
		TakeTime takeTime = takeTimeRepository.findOne(takeTimeId);
		// 主键维护权在快递员
		courier.setTakeTime(takeTime);
		// 主键维护权在定区
		fixedArea.getCouriers().add(courier);
	}
	
	// 查询定定区关联的快递员
	@Override
	public List<Courier> showFixedAreaAndCourier(FixedArea model) {
		return fixedAreaRepository.queryFixedAreaAndCourier(model.getId());
	}

	@Override
	public List<FixedArea> findAll() {
		return fixedAreaRepository.findAll();
	}
	
	// 查询定区关联的分区
	@Override
	public List<SubArea> querySubArea(FixedArea model) {
		return fixedAreaRepository.querySubArea(model.getId());
	}

}
