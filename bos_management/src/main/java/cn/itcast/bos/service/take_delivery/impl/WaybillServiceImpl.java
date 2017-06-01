package cn.itcast.bos.service.take_delivery.impl;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.take_delivery.WayBillRepository;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.service.take_delivery.WaybillService;

@Service
@Transactional
public class WaybillServiceImpl implements WaybillService {
	
	@Autowired
	private WayBillRepository wayBillRepository;
	
	@Override
	public void save(WayBill model) {
		WayBill persistWayBill = wayBillRepository.findByWayBillNum(model.getWayBillNum());
		
		if (persistWayBill == null) {
			// 运单不存在
			wayBillRepository.save(model);
		}else {
			try {
				// 运单存在
				Integer id = persistWayBill.getId();
				// 新的属性复制到原来的属性上
				BeanUtils.copyProperties(persistWayBill, model);
				// 持久态
				persistWayBill.setId(id);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("运单保存失败!");
			}
		}
	}

	@Override
	public Page<WayBill> pageQuery(Pageable pageable) {
		return wayBillRepository.findAll(pageable);
	}

	@Override
	public WayBill findByWayBillNum(String wayBillNum) {
		return wayBillRepository.findByWayBillNum(wayBillNum);
	}
}
