package cn.itcast.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.SubArea;

public interface FixedAreaService {

	void save(FixedArea model);

	Page<FixedArea> findAll(Specification<FixedArea> specification, Pageable pageable);

	void associationCourierToFixedArea(FixedArea model, Integer courierId, Integer takeTimeId);

	List<Courier> showFixedAreaAndCourier(FixedArea model);

	List<FixedArea> findAll();

	List<SubArea> querySubArea(FixedArea model);
	
}
