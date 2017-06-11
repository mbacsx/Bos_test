package cn.itcast.bos.service.transit.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.index.WayBillIndexRepository;
import cn.itcast.bos.dao.transit.SignInfoInfoRepository;
import cn.itcast.bos.dao.transit.TransitInfoRepository;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.domain.transit.SignInfo;
import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.service.transit.SignInfoInfoService;

@Service
@Transactional
public class SignInfoInfoServiceImpl implements SignInfoInfoService {
	
	@Autowired
	private SignInfoInfoRepository signInfoInfoRepository;
	
	@Autowired
	private TransitInfoRepository transitInfoRepository;
	
	@Autowired
	private WayBillIndexRepository wayBillIndexRepository;
	
	@Override
	public void save(String transitInfoId, SignInfo model) {
		signInfoInfoRepository.save(model);
		
		TransitInfo transitInfo = transitInfoRepository.findOne(Integer.parseInt(transitInfoId));
		transitInfo.setSignInfo(model);// 关联配送
		
		if (model.getSignType().equals("正常")) {
			transitInfo.setStatus("已签收");// 设置运单状态
			WayBill wayBill = transitInfo.getWayBill();
			wayBill.setSignStatus(3);// 运单状态设置为已签收
			wayBillIndexRepository.save(wayBill);// 更新索引
			
		}else {
			transitInfo.setStatus("异常");// 设置运单状态
			WayBill wayBill = transitInfo.getWayBill();
			wayBill.setSignStatus(4);// 运单状态设置为异常
			wayBillIndexRepository.save(wayBill);// 更新索引库
		}
	}

}
