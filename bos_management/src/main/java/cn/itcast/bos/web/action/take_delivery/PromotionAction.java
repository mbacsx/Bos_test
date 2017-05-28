package cn.itcast.bos.web.action.take_delivery;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
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
import org.springframework.data.jpa.domain.Specification;
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
	
	private String ids;
	
	// 宣传互动批量取消
	@Action(value="promotion_updateStatus",results={@Result(name="success",type="redirect",location="./pages/take_delivery/promotion.html")})
	public String updateStatus(){
		String[] idArray = ids.split(",");
		promotionService.updateStatus(idArray);
		return SUCCESS;
	}
	
	// 分页条件查询
	@Action(value="promotion_pageQuery",results={@Result(name="success",type="json")})
	public String pageQuery(){
		Specification<Promotion> specification = new Specification<Promotion>() {
			
			@Override
			public Predicate toPredicate(Root<Promotion> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				if (StringUtils.isNotBlank(model.getTitle())) {
					Predicate p1 = cb.like(root.get("title").as(String.class), "%"+model.getTitle()+"%");
					list.add(p1);
				}
				if (model.getStartDate()!=null) {
					Predicate p2 = cb.equal(root.get("startDate").as(Date.class), model.getStartDate());
					list.add(p2);
				}
				if (model.getEndDate()!=null) {
					Predicate p3 = cb.equal(root.get("endDate").as(Date.class), model.getEndDate());
					list.add(p3);
				}
				return cb.and(list.toArray(new Predicate[0]));
			}
		};
		
		Pageable pageable = new PageRequest(page-1, rows);
		Page<Promotion> pageData = promotionService.pageQuery(specification,pageable);
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

	public void setIds(String ids) {
		this.ids = ids;
	}
}
