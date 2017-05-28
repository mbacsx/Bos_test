package cn.itcast.bos.dao.base;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.take_delivery.Promotion;

public interface PromotionRepository extends JpaRepository<Promotion, Integer>,JpaSpecificationExecutor<Promotion> {
	
	@Query("update Promotion set status = '2' where endDate < ? and status = '1' ")
	@Modifying
	void updateStatus(Date date);
	
	// 后台批量取消宣传任务
	@Query("update Promotion set status = '2' where id = ?")
	@Modifying
	void updateStatus(int parseInt);

}
