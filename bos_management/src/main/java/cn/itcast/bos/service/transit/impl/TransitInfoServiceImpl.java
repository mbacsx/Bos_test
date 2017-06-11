package cn.itcast.bos.service.transit.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.index.WayBillIndexRepository;
import cn.itcast.bos.dao.take_delivery.WayBillRepository;
import cn.itcast.bos.dao.transit.TransitInfoRepository;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.service.transit.TransitInfoService;

@Service
@Transactional
public class TransitInfoServiceImpl implements TransitInfoService {

	@Autowired
	private TransitInfoRepository transitInfoRepository;

	@Autowired
	private WayBillRepository wayBillRepository;
	
	@Autowired
	private WayBillIndexRepository wayBillIndexRepository;

	@Override
	public void createTransit(String wayBillIds) {
		if (StringUtils.isNotBlank(wayBillIds)) {
			String[] ids = wayBillIds.split(",");
			for (String id : ids) {
				WayBill wayBill = wayBillRepository.findOne(Integer.parseInt(id));
				if (wayBill.getSignStatus() == 1) {// 待发货
					TransitInfo transitInfo = new TransitInfo();
					transitInfo.setWayBill(wayBill);
					transitInfo.setStatus("出入库中转");
					transitInfoRepository.save(transitInfo);
					 // 更改运单状态
					wayBill.setSignStatus(2); // 出库
					wayBillIndexRepository.save(wayBill); //更新索引库
				}
			}
		}
	}

	@Override
	public Page<TransitInfo> findAll(Pageable pageable) {
		return transitInfoRepository.findAll(pageable);
	}

}
