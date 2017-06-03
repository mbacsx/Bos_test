package cn.itcast.bos.service.take_delivery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.take_delivery.WayBill;

public interface WaybillService {

	void save(WayBill model);

	Page<WayBill> pageQuery(WayBill wayBill,Pageable pageable);

	WayBill findByWayBillNum(String wayBillNum);
	
}
