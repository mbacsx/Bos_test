package cn.itcast.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.SubArea;

public interface FixedAreaRepository extends JpaRepository<FixedArea, String>, JpaSpecificationExecutor<FixedArea> {

	@Query(value = "select distinct c from Courier c inner join fetch c.fixedAreas f where f.id = ? ")
	List<Courier> queryFixedAreaAndCourier(String id);

	@Query(value = "from SubArea s inner join fetch s.fixedArea f where f.id = ?")
	List<SubArea> querySubArea(String id);
}
