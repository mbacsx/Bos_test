package cn.itcast.bos.service.transit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.transit.TransitInfo;

public interface TransitInfoService {

	void createTransit(String wayBillIds);

	Page<TransitInfo> findAll(Pageable pageable);

}
