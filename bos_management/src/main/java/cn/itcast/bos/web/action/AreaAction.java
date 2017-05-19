package cn.itcast.bos.web.action;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.base.AreaService;
import cn.itcast.bos.utils.PinYin4jUtils;
import cn.itcast.bos.web.action.commons.BaseAction;

@SuppressWarnings("all")
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class AreaAction extends BaseAction<Area> {
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private AreaService areaService;
	
	// 添加修改区域
	@Action(value="area_save",results={@Result(name="success",type="redirect",location="./pages/base/area.html")})
	public String save(){
		areaService.save(model);
		return SUCCESS;
	}
	
	// 查询全部区域
	@Action(value="area_findAll",results={@Result(name="success",type="json")})
	public String findAll(){
		List<Area> areaData = areaService.findAll();
		ActionContext.getContext().getValueStack().push(areaData);
		return SUCCESS;
	}
	
	// 分页查询全部定区
	@Action(value="area_pageQuery",results={@Result(name="success",type="json")})
	public String pageQuery(){
		Specification<Area> specification = new Specification<Area>() {
			
			@Override
			// root获取属性字段,CriteriaQuery可以用于简单条件查询，CriteriaBuilder 用于构造复杂条件查询
			public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				// 封装条件查询对象 Specification
				if(StringUtils.isNotBlank(model.getProvince())){
					Predicate p1 = cb.like(root.get("province").as(String.class), "%"+model.getProvince()+"%");
					list.add(p1);
				}
				if(StringUtils.isNotBlank(model.getCity())){
					Predicate p2 = cb.like(root.get("city").as(String.class), "%"+model.getCity()+"%");
					list.add(p2);
				}
				if(StringUtils.isNotBlank(model.getDistrict())){
					Predicate p3 = cb.like(root.get("district").as(String.class), "%"+model.getDistrict()+"%");
					list.add(p3);
				}
				return cb.and(list.toArray(new Predicate[0]));
			}
		};
		
		Pageable pageable = new PageRequest(page-1, rows);
		Page<Area> pageData = areaService.pageQuery(specification,pageable);
		// 继承自baseAction,封装分页查询数据方法
		pushPageDataToValueStack(pageData);
		return SUCCESS;
	}
	
	// 上传的文件
	private File file;
	// 上传的文件名
	private String fileFileName;
	
	@Action(value="area_batchImport",results={@Result(name="success",type="redirect",location="./pages/base/area.html")})
	public String batchImport() throws Exception{
		List<Area> areas = new ArrayList<>();
		
		Sheet sheet = null;
		if (fileFileName.endsWith(".xls")) {
			// 基于xls格式解析
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
			sheet = hssfWorkbook.getSheetAt(0);
		}else if(fileFileName.endsWith(".xlsx")) {
			// 基于xlsx格式解析
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(file));
			// 读取整页数据
			sheet = xssfWorkbook.getSheetAt(0);
		}
	
		// 读取sheet的每一行
		for (Row row : sheet) {
			// 跳过第一行的数据
			if (row.getRowNum()==0) {
				continue;
			}
			// 跳过空行
			// 获取第一个单元格,判定第一个单元格是否为空
			if (row.getCell(0)==null || StringUtils.isBlank(row.getCell(0).getStringCellValue())) {
				continue;
			}
			Area area = new Area();
			area.setId(row.getCell(0).getStringCellValue());
			area.setProvince(row.getCell(1).getStringCellValue());
			area.setCity(row.getCell(2).getStringCellValue());
			area.setDistrict(row.getCell(3).getStringCellValue());
			area.setPostcode(row.getCell(4).getStringCellValue());
			// 去掉省市区最后一个字
			String province = area.getProvince();
			String city = area.getCity();
			String district = area.getDistrict();
			province = province.substring(0, province.length()-1);
			city = city.substring(0, city.length()-1);
			district = district.substring(0, district.length()-1);
			// 简码,省市区首字母的大写
			String[] headArray;
			if (city.equals("北京")||city.equals("天津")||city.equals("重庆")||city.equals("上海")) {
				headArray = PinYin4jUtils.getHeadByString(city+district);
			}else {
				headArray = PinYin4jUtils.getHeadByString(province+city+district);
			}
			StringBuilder sb = new StringBuilder();
			for (String headStr : headArray) {
				sb.append(headStr);
			}
			area.setShortcode(sb.toString());
			// 城市编码
			String cityCode = PinYin4jUtils.hanziToPinyin(city, "");
			area.setCitycode(cityCode);
			areas.add(area);
		}
		areaService.batchImport(areas);
		return SUCCESS;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}
}
