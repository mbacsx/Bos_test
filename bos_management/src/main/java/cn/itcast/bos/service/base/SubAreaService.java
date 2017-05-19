package cn.itcast.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.base.SubArea;

public interface SubAreaService {

	void save(SubArea model);

	Page<SubArea> findAll(Specification<SubArea> specification, Pageable pageable);

	List<SubArea> findAll();

}
