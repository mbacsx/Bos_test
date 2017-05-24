package cn.itcast.bos.web.action.take_delivery;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.service.base.PromotionService;
import cn.itcast.bos.web.action.commons.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class PromotionAction extends BaseAction<Promotion> {
	private static final long serialVersionUID = 1L;

	private File titleImgFile;
	private String titleImgFileFileName;

	@Autowired
	private PromotionService promotionService;

	// 分页查询
	@Action(value="promotion_pageQuery",results={@Result(name="success",type="json")})
	public String pageQuery(){
		Pageable pageable = new PageRequest(page-1, rows);
		Page<Promotion> pageData = promotionService.pageQuery(pageable);
		pushPageDataToValueStack(pageData);
		return SUCCESS;
	}
	
	@Action(value = "promotion_save", results = { @Result(name = "success", type = "redirect", location = "./pages/take_delivery/promotion.html") })
	public String save() throws IOException {
		// 宣传图 上传、在数据表保存 宣传图路径
		String savePath = ServletActionContext.getServletContext().getRealPath("/upload/");
		String saveUrl = ServletActionContext.getRequest().getContextPath() + "/upload/";

		// 生成随机图片名
		UUID uuid = UUID.randomUUID();
		String ext = titleImgFileFileName.substring(titleImgFileFileName.lastIndexOf("."));
		String randomFileName = uuid + ext;

		// 保存图片 (绝对路径)
		File destFile = new File(savePath + "/" + randomFileName);
		System.out.println(destFile.getAbsolutePath());
		FileUtils.copyFile(titleImgFile, destFile);

		// 将保存路径 相对工程web访问路径，保存model中
		model.setTitleImg(saveUrl + randomFileName);

		// 调用业务层，完成活动任务数据保存
		promotionService.save(model);

		return SUCCESS;
	}

	public void setTitleImgFile(File titleImgFile) {
		this.titleImgFile = titleImgFile;
	}

	public void setTitleImgFileFileName(String titleImgFileFileName) {
		this.titleImgFileFileName = titleImgFileFileName;
	}
}
