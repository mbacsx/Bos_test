package cn.itcast.bos.dao.take_delivery;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.bos.domain.take_delivery.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

	Order findByOrderNum(String orderNum);

	List<Order> findByOrderType(String type);

}
