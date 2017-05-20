package cn.itcast.bos.web.action;

import java.io.UnsupportedEncodingException;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.utils.MailUtils;
import cn.itcast.bos.web.action.commons.BaseAction;
import cn.itcast.crm.domain.Customer;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class CustomerAction extends BaseAction<Customer> {
	private static final long serialVersionUID = 1L;

	@Action(value = "customer_sendSms")
	public String sendSms() throws UnsupportedEncodingException {
		// 生成验证码
		String randomNumeric = RandomStringUtils.randomNumeric(4);
		System.out.println("验证码" + randomNumeric);
		// 把验证码存入session
		ServletActionContext.getRequest().getSession().setAttribute("checkCode", randomNumeric);
		// 编辑短信内容
		//String msg = "尊敬的用户您好,本次短信验证码为[" + randomNumeric + "],打死都不要告诉别人!";
		// 发送短信
		//String result = SmsUtils.sendSmsByHTTP(model.getTelephone(), msg);
		String result = "000/xxx";
		if (result.startsWith("000")) {
			return NONE;
		}else {
			throw new RuntimeException("短信发送失败");
		}
	}

	// 属性驱动接收验证码
	private String checkCode;

	// 用户注册
	@Action(value = "customer_regist", results = { @Result(name = "input", type = "redirect", location = "./signup.html"),
			@Result(name = "success", type = "redirect", location = "./signup-success.html") })
	public String regist() {
		String check = (String) ServletActionContext.getRequest().getSession().getAttribute("checkCode");
		if (check != null && !StringUtils.equals(check, checkCode)) {
			return INPUT;
		} else {
			WebClient.create("http://localhost:8088/crm_management/services/customerService/regisrcustomer")
					.type(MediaType.APPLICATION_JSON).post(model);
			String content = "<h2>来自速运快递的激活邮件:请点击下方按钮激活!</h2><h3><a href='http://localhost:8808/bos_fore/customer_activeMail.action?mailCode="+RandomStringUtils.randomNumeric(9)+"'>点我激活!</a></h3>";
			MailUtils.sendMail(model.getEmail(),content );
			return SUCCESS;
		}
	}

	// 属性驱动接收邮箱激活码
	private String mailCode;

	// 邮箱激活
	@Action(value = "customer_activeMail", results = { @Result(name = "success", type = "redirect", location = "./login.html") })
	public String activeMail() {
		System.out.println(mailCode);
		return SUCCESS;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	public void setMailCode(String mailCode) {
		this.mailCode = mailCode;
	}
}
