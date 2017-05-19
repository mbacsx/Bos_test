package cn.itcast.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.itcast.bos.domain.base.SubArea;

public interface SubAreaRepository extends JpaRepository<SubArea, String>,JpaSpecificationExecutor<SubArea> {
	
}
