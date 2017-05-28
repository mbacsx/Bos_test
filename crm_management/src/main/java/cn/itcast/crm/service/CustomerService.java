package cn.itcast.crm.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import cn.itcast.crm.domain.Customer;

/**
 * 客户操作
 * 
 * @author itcast
 *
 */
public interface CustomerService {

	// 查询所有未关联客户列表
	@Path("/noassociationcustomers")
	@GET
	@Produces({ "application/xml", "application/json" })
	public List<Customer> findNoAssociationCustomers();

	// 已经关联到指定定区的客户列表
	@Path("/associationfixedareacustomers/{fixedareaid}")
	@GET
	@Produces({ "application/xml", "application/json" })
	public List<Customer> findHasAssociationFixedAreaCustomers(@PathParam("fixedareaid") String fixedAreaId);

	// 将客户关联到定区上 ， 将所有客户id 拼成字符串 1,2,3
	@Path("/associationcustomerstofixedarea")
	@PUT
	public void associationCustomersToFixedArea(@QueryParam("customerIdStr") String customerIdStr,
			@QueryParam("fixedAreaId") String fixedAreaId);

	// 注册客户
	@Path("/regisrcustomer")
	@POST
	@Consumes({ "application/xml", "application/json" })
	public void regist(Customer model);
	
	// 通过手机号查询激活状态/登录
	@Path("/telephone/{telephone}")
	@GET
	@Consumes({ "application/xml", "application/json" })
	public Customer findByTelephone(@PathParam("telephone") String telephone);
	
	// 修改激活状态
	@Path("/updateType")
	@PUT
	public void updateType(@QueryParam("telephone") String telephone);
	
	// 用户登录
	@Path("/customer/login")
	@GET
	@Consumes({ "application/xml", "application/json" })
	public Customer login(@QueryParam("telephone") String telephone,@QueryParam("password") String password);
	
	// 通过匹配客户的详细地址匹配到定区
	@Path("/customer/findFixedAreaIdByAddress")
	@GET
	@Consumes({ "application/xml", "application/json" })
	public String findFixedAreaIdByAddress(@QueryParam("sendAddress") String sendAress);
}
