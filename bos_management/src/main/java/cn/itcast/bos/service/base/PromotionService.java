package cn.itcast.bos.service.base;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;

public interface PromotionService {

	void save(Promotion model);

	Page<Promotion> pageQuery(Pageable pageable);
	
	// 前台页面分页展示请求
	@Path("/pageQuery")
	@GET
	@Produces({ "application/xml", "application/json" })
	public PageBean<Promotion> pageQuery(@QueryParam("page") int page, @QueryParam("rows") int rows);
	
	// 活动详情数据展示
	@Path("/promotion/{id}")
	@GET
	@Produces({ "application/xml", "application/json" })
	public Promotion findPromotion(@PathParam("id") Integer id);
	
	// 修改活动过期状态
	void updateStatus(Date date);
}
