package cn.itcast.bos.web.action;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.web.action.commons.BaseAction;
import cn.itcast.crm.domain.Customer;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class CustomerAction extends BaseAction<Customer> {
	private static final long serialVersionUID = 1L;
	
	// 注入activeMQ的Queue模版
	@Autowired
	@Qualifier("jmsQueueTemplate")
	private JmsTemplate jmsTemplate;
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	// 密码登录
	@Action(value="customer_login",results={@Result(name="success",type="redirect",location="./index.html#/myhome"),
			@Result(name="error",type="redirect",location="./login.html")})
	public String passwordLogin(){
		
		Customer customer = WebClient.create("http://localhost:8088/crm_management/services/customerService/customer/login?telephone="+model.getTelephone()+"&password="+model.getPassword()).accept(MediaType.APPLICATION_JSON).get(Customer.class);
		if (customer == null) {
			return ERROR;
		}else {
			// 保存到session
			ServletActionContext.getRequest().getSession().setAttribute("customer", customer);
			return SUCCESS;
		}
	}
	
	@Action(value = "customer_sendSms")
	public String sendSms() {
		// 生成验证码
		final String randomNumeric = RandomStringUtils.randomNumeric(4);
		System.out.println("验证码" + randomNumeric + ",手机号" + model.getTelephone());
		// 把验证码存入session
		ServletActionContext.getRequest().getSession().setAttribute("checkCode", randomNumeric);
		
		// 调用mq服务,发送一条短信
		jmsTemplate.send("bos_message",new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				MapMessage mapMessage = session.createMapMessage();
				// 传入客户手机号
				mapMessage.setString("telephone", model.getTelephone());
				// 传入随机验证码
				mapMessage.setString("randomNumeric", randomNumeric);
				return mapMessage;
			}
		});
		return NONE;
	}
	
	// 属性驱动接收验证码,用于注册和短信登录
	private String checkCode;

	// 用户注册
	@Action(value = "customer_regist", results = { @Result(name = "input", type = "redirect", location = "./signup.html"),
			@Result(name = "success", type = "redirect", location = "./signup-success.html") })
	public String regist() {
		String check = (String) ServletActionContext.getRequest().getSession().getAttribute("checkCode");
		if (check != null && !StringUtils.equals(check, checkCode)) {
			return INPUT;
		} else {
			// 生成激活码
			String activeCode = RandomStringUtils.randomNumeric(32);
			// 激活码存入redis
			redisTemplate.opsForValue().set(model.getTelephone(), activeCode, 24, TimeUnit.HOURS);
			// 保存客户信息
			WebClient.create("http://localhost:8088/crm_management/services/customerService/regisrcustomer")
					.type(MediaType.APPLICATION_JSON).post(model);
			
			// 生成邮件正文
			final String content = "<h2>来自速运快递的激活邮件,请在24小时内点击下方激活按钮完成激活!</h2><h3><a href='http://localhost:8808/bos_fore/customer_activeMail.action?telephone="+model.getTelephone()+"&activeMailCode="+activeCode+"'>点我激活!</a></h3>";
			
			// 调用mq服务,生成消息队列,发送一封邮件
			jmsTemplate.send("bos_mail",new MessageCreator() {
				
				@Override
				public Message createMessage(Session session) throws JMSException {
					MapMessage mapMessage = session.createMapMessage();
					// 传入客户邮箱
					mapMessage.setString("email", model.getEmail());
					// 传入邮件正文
					mapMessage.setString("content", content);
					return mapMessage;
				}
			});
			return SUCCESS;
		}
	}

	// 属性驱动接收邮箱激活码
	private String activeMailCode;

	// 邮箱激活
	@Action(value = "customer_activeMail", results = { @Result(name = "success", type = "redirect", location = "./login.html") })
	public String activeMail() throws IOException {
		// 设置编码
		ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
		// 从redis中获取激活码
		String mailCode = redisTemplate.opsForValue().get(model.getTelephone());
		// 判定验证码是否有效
		if (activeMailCode == null && !mailCode.equals(activeMailCode) ) {
			// 激活码无效
			ServletActionContext.getResponse().getWriter().println("激活码无效,请登录系统重新绑定邮箱!");
		}else {
			// 激活码有效
			// 为避免重复激活,先进行查询
			Customer customer = WebClient.create("http://localhost:8088/crm_management/services/customerService/telephone/"+model.getTelephone()).accept(MediaType.APPLICATION_JSON).get(Customer.class);
			if (customer.getType() == null || customer.getType() != 1) {
				// 未激活
				// 发送修改请求
				WebClient.create("http://localhost:8088/crm_management/services/customerService/updateType?telephone="+model.getTelephone()).put(null);
				// 删除redis的激活码
				redisTemplate.delete(model.getTelephone());
				return SUCCESS;
			}else {
				// 已经激活过了
				ServletActionContext.getResponse().getWriter().println("账号已激活,无需重复激活!");
			}
			// 删除redis的激活码
			redisTemplate.delete(model.getTelephone());
		}
		return NONE;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	public void setActiveMailCode(String activeMailCode) {
		this.activeMailCode = activeMailCode;
	}
}
