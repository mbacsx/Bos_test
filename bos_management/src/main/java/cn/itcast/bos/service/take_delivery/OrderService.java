package cn.itcast.bos.service.take_delivery;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import cn.itcast.bos.domain.take_delivery.Order;

public interface OrderService {
	
	@Path("/order_add")
	@POST
	@Consumes({ "application/xml", "application/json" })
	public void saveOrder(Order order);

	public Order findByOrderNum(String orderNum);

	public List<Order> findByOrderType();

	public void dispatcherOrder(Order model);
}
