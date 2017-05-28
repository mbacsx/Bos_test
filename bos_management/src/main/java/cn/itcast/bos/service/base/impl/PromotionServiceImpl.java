package cn.itcast.bos.service.base.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.PromotionRepository;
import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.service.base.PromotionService;

@Service
@Transactional
public class PromotionServiceImpl implements PromotionService {
	
	@Autowired
	private PromotionRepository promotionRepository;

	@Override
	public void save(Promotion model) {
		promotionRepository.save(model);
	}

	@Override
	public Page<Promotion> pageQuery(Specification<Promotion> specification,Pageable pageable) {
		return promotionRepository.findAll(specification,pageable);
	}
	
	// 前台页面分页展示请求
	@Override
	public PageBean<Promotion> pageQuery(int page, int rows) {
		Pageable pageable = new PageRequest(page-1, rows);
		Page<Promotion> pageData = promotionRepository.findAll(pageable);
		// 封装到pageBean中
		PageBean<Promotion> pageBean = new PageBean<>();
		pageBean.setTotalCount(pageData.getTotalElements());
		pageBean.setPageData(pageData.getContent());
		return pageBean;
	}

	@Override
	public Promotion findPromotion(Integer id) {
		return promotionRepository.findOne(id);
	}
	
	// 定时取消宣传任务
	@Override
	public void updateStatus(Date date) {
		promotionRepository.updateStatus(date);
	}
	
	// 后台批量取消宣传任务
	@Override
	public void updateStatus(String[] idArray) {
		for (String id : idArray) {
			promotionRepository.updateStatus(Integer.parseInt(id));
		}
	}
}
