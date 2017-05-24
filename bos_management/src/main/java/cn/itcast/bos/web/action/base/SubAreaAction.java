package cn.itcast.bos.web.action.base;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
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

import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.service.base.SubAreaService;
import cn.itcast.bos.utils.ExportExcelUtils;
import cn.itcast.bos.web.action.commons.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class SubAreaAction extends BaseAction<SubArea> {
	private static final long serialVersionUID = 1L;

	@Autowired
	private SubAreaService subAreaService;

	// 分区的修改与保存
	@Action(value = "subArea_save", results = {
			@Result(name = "success", type = "redirect", location = "./pages/base/sub_area.html") })
	public String save() {
		subAreaService.save(model);
		return SUCCESS;
	}

	// 属性驱动,接收定区编码
	private String fixedAreaId;

	// 分页条件查询
	@Action(value = "subArea_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		Specification<SubArea> specification = new Specification<SubArea>() {

			@Override
			public Predicate toPredicate(Root<SubArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				// 关键字
				if (StringUtils.isNotBlank(model.getKeyWords())) {
					Predicate p1 = cb.like(root.get("keyWords").as(String.class), "%" + model.getKeyWords() + "%");
					list.add(p1);
				}
				// 通过区域查询
				Join<Object, Object> joinArea = root.join("area", JoinType.INNER);
				if (model.getArea() != null && StringUtils.isNotBlank(model.getArea().getProvince())) {
					Predicate p2 = cb.like(joinArea.get("province").as(String.class),
							"%" + model.getArea().getProvince() + "%");
					list.add(p2);
				}
				if (model.getArea() != null && StringUtils.isNotBlank(model.getArea().getCity())) {
					Predicate p3 = cb.like(joinArea.get("city").as(String.class),
							"%" + model.getArea().getCity() + "%");
					list.add(p3);
				}
				if (model.getArea() != null && StringUtils.isNotBlank(model.getArea().getDistrict())) {
					Predicate p4 = cb.like(joinArea.get("district").as(String.class),
							"%" + model.getArea().getDistrict() + "%");
					list.add(p4);
				}
				// 通过定区查询
				Join<Object, Object> joinFixedArea = root.join("fixedArea", JoinType.INNER);
				if (StringUtils.isNotBlank(fixedAreaId)) {
					Predicate p5 = cb.equal(joinFixedArea.get("id"), fixedAreaId);
					list.add(p5);
				}
				return cb.and(list.toArray(new Predicate[0]));
			}
		};
		Pageable pageable = new PageRequest(page - 1, rows);
		Page<SubArea> pageData = subAreaService.findAll(specification, pageable);
		pushPageDataToValueStack(pageData);
		return SUCCESS;
	}

	// 分区数据导出
	// 属性驱动
	private String path;
	private String fileName;
	private String format;
	// 分区数据导出
	@Action(value="subArea_export" , results = { @Result(name = "success", type = "redirect",location="./pages/base/sub_area.html") })
	public String export(){
		List<SubArea> list = subAreaService.findAll();
		// 导出路径
		String exportPath = path+fileName+format;
		if (format.equals(".xls")) {
			ExportExcelUtils.exportExcelForXls(list, exportPath);
		}else {
			ExportExcelUtils.exportExcelForXlsx(list, exportPath);
		}
		return SUCCESS;
	}

	public void setFixedAreaId(String fixedAreaId) {
		this.fixedAreaId = fixedAreaId;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setFormat(String format) {
		this.format = format;
	}
}
