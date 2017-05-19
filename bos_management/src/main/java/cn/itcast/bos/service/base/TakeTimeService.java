package cn.itcast.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.base.TakeTime;

public interface TakeTimeService {

	List<TakeTime> findAll();

	Page<TakeTime> pageQuery(Pageable pageable);

	void save(TakeTime model);

}
