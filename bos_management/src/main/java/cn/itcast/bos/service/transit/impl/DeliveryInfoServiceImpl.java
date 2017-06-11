package cn.itcast.bos.service.transit.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.transit.DeliveryInfoRepository;
import cn.itcast.bos.dao.transit.TransitInfoRepository;
import cn.itcast.bos.domain.transit.DeliveryInfo;
import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.service.transit.DeliveryInfoService;

@Service
@Transactional
public class DeliveryInfoServiceImpl implements DeliveryInfoService {
	
	@Autowired
	private DeliveryInfoRepository deliveryInfoRepository;
	
	@Autowired
	private TransitInfoRepository transitInfoRepository;
	
	@Override
	public void save(String transitInfoId, DeliveryInfo model) {
		deliveryInfoRepository.save(model);
		
		TransitInfo transitInfo = transitInfoRepository.findOne(Integer.parseInt(transitInfoId));
		transitInfo.setStatus("派送中");// 设置运单状态
		transitInfo.setDeliveryInfo(model);// 关联配送
	}

}
