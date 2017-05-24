package cn.itcast.bos.web.action;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.web.action.commons.BaseAction;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@SuppressWarnings("all")
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class PromotionAction extends BaseAction<Promotion> {
	private static final long serialVersionUID = 1L;
	
	// 每页显示数量
	private int pageSize;

	@Action(value = "promotion_pageQuery", results = { @Result(type = "json", name = "success") })
	public String pageQuery() {
		PageBean<Promotion> pageBean = WebClient
				.create("http://localhost:8080/bos_management/services/promotionService/pageQuery?page=" + page
						+ "&rows=" + pageSize)
				.accept(MediaType.APPLICATION_JSON).get(PageBean.class);
		ActionContext.getContext().getValueStack().push(pageBean);
		return SUCCESS;
	}

	// 活动详情页面
	@Action(value = "promotion_showDetail")
	public String showDetail() throws IOException, TemplateException {
		// 现判断这个html页面是否存在,存在直接返回
		String htmlRealPath = ServletActionContext.getServletContext().getRealPath("/freemarker");
		File htmlFile = new File(htmlRealPath + "/" + model.getId() + ".html");
		if (!htmlFile.exists()) {
			// 不存在
			Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);
			configuration.setDirectoryForTemplateLoading(new File(ServletActionContext.getServletContext().getRealPath("/WEB-INF/freemarker_templates")));
			// 获取模版对象
			Template template = configuration.getTemplate("promotion_detail.ftl");
			// 动态数据
			Promotion promotion = WebClient.create("http://localhost:8080/bos_management/services/promotionService/promotion/"+model.getId()).accept(MediaType.APPLICATION_JSON).get(Promotion.class);
			// 封装成所需要的map
			Map<String, Object> paramtermap = new HashMap<>();
			paramtermap.put("promotion", promotion);
			// 合并输出
			template.process(paramtermap, new FileWriter(htmlFile));
		}
		
		// 存在
		ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
		FileUtils.copyFile(htmlFile, ServletActionContext.getResponse().getOutputStream());
		return NONE;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
