package cn.itcast.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.base.Standard;

public interface StandardRepository extends JpaRepository<Standard, Integer> {

	List<Standard> findByName(String string);
	
	@Query(value="from Standard where id = ?",nativeQuery=false)//false代表使用jpql,true到表示使用sql
	Standard queryId(Integer id);

}
