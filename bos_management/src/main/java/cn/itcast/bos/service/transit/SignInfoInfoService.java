package cn.itcast.bos.service.transit;

import cn.itcast.bos.domain.transit.SignInfo;

public interface SignInfoInfoService {

	void save(String transitInfoId, SignInfo model);

}
