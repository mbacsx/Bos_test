package cn.itcast.bos.service.take_delivery.impl;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder.Operator;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.itcast.bos.dao.index.WayBillIndexRepository;
import cn.itcast.bos.dao.take_delivery.WayBillRepository;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.service.take_delivery.WaybillService;

@Service
@Transactional
public class WaybillServiceImpl implements WaybillService {

	@Autowired
	private WayBillRepository wayBillRepository;
	
	// elasticsearch
	@Autowired
	private WayBillIndexRepository wayBillIndexRepository;

	@Override
	public void save(WayBill model) {
		WayBill persistWayBill = wayBillRepository.findByWayBillNum(model.getWayBillNum());

		if (persistWayBill == null) {
			// 运单不存在
			wayBillRepository.save(model);
			wayBillIndexRepository.save(model);
		} else {
			try {
				// 运单存在
				Integer id = persistWayBill.getId();
				// 新的属性复制到原来的属性上
				BeanUtils.copyProperties(persistWayBill, model);
				// 持久态
				persistWayBill.setId(id);
				wayBillIndexRepository.save(persistWayBill);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("运单保存失败!");
			}
		}
	}

	@Override
	public Page<WayBill> pageQuery(WayBill wayBill, Pageable pageable) {
		if (StringUtils.isBlank(wayBill.getWayBillNum()) 
				&& StringUtils.isBlank(wayBill.getSendAddress())
				&& StringUtils.isBlank(wayBill.getRecAddress()) 
				&& StringUtils.isBlank(wayBill.getSendProNum())
				&& (wayBill.getSignStatus() == null || wayBill.getSignStatus() == 0)) {
			// 无条件查询
			return wayBillRepository.findAll(pageable);
		}else {
			// 查询条件
			// must 条件必须成立 and
			// must not 条件必须不成立 not
			// should 条件可以成立 or
			BoolQueryBuilder query = new BoolQueryBuilder();
			if (StringUtils.isNoneBlank(wayBill.getWayBillNum())) {
				QueryBuilder termQuery = new TermQueryBuilder("wayBillNum", wayBill.getWayBillNum());
				query.must(termQuery);
			}
			if (StringUtils.isNoneBlank(wayBill.getSendAddress())) {
				// 在词条范围,用模糊查询
				QueryBuilder wildQuery = new WildcardQueryBuilder("sendAddress", "*"+wayBill.getSendAddress()+"*");
				// 超过词条大小,多词条查询
				QueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(wayBill.getSendAddress()).field("sendAddress").defaultOperator(Operator.AND);
				
				// 两种情况满足一个即可,用or
				BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
				boolQueryBuilder.should(wildQuery);
				boolQueryBuilder.should(queryStringQueryBuilder);
				query.must(boolQueryBuilder);
			}
			if (StringUtils.isNoneBlank(wayBill.getRecAddress())) {
				// 在词条范围,用模糊查询
				QueryBuilder wildQuery = new WildcardQueryBuilder("recAddress", "*"+wayBill.getRecAddress()+"*");
				// 超过词条大小,多词条查询
				QueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(wayBill.getRecAddress()).field("recAddress").defaultOperator(Operator.AND);

				// 两种情况满足一个即可,用or
				BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
				boolQueryBuilder.should(wildQuery);
				boolQueryBuilder.should(queryStringQueryBuilder);
				query.must(boolQueryBuilder);
			}
			if (StringUtils.isNoneBlank(wayBill.getSendProNum())) {
				QueryBuilder termQuery = new TermQueryBuilder("sendProNum", wayBill.getSendProNum());
				query.must(termQuery);
			}
			if (wayBill.getSignStatus() != null && wayBill.getSignStatus() != 0) {
				QueryBuilder termQuery = new TermQueryBuilder("signStatus", wayBill.getSignStatus());
				query.must(termQuery);
			}
			SearchQuery searchQuery = new NativeSearchQuery(query);
			searchQuery.setPageable(pageable);
			return wayBillIndexRepository.search(searchQuery);
		}
	}

	@Override
	public WayBill findByWayBillNum(String wayBillNum) {
		return wayBillRepository.findByWayBillNum(wayBillNum);
	}
}
