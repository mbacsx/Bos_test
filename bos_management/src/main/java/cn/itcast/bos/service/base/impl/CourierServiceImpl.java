package cn.itcast.bos.service.base.impl;

import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.CourierRepository;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.CourierService;

@Service
@Transactional
public class CourierServiceImpl implements CourierService {
	
	@Autowired
	private CourierRepository courierRepository;
	
	@Override
	public void save(Courier courier) {
		Courier persistCourier = courierRepository.findByCourierNum(courier.getCourierNum());
		if (persistCourier == null) {
			courierRepository.save(courier);
		}else {
			try{
				BeanUtils.copyProperties(persistCourier, courier);
			}catch(Exception e){
				e.printStackTrace();
				throw new RuntimeException("保存快递员异常!");
			}
		}
	}

	@Override
	public Page<Courier> pageQuery(Specification<Courier> specification, Pageable pageable) {
		return courierRepository.findAll(specification,pageable);
	}

	@Override
	public void updateDeltag(String[] idsArray, String flag) {
		for (String ids : idsArray) {
			int id = Integer.parseInt(ids);
			courierRepository.updateDeltag(id,flag.toCharArray()[0]);
		}
	}
	
	//修改快递员信息
//	@Override
//	public void update(Courier courier) {
//		Courier c = courierRepository.findOne(courier.getId());
//		c.setName(courier.getName());
//		c.setTelephone(courier.getTelephone());
//		c.setPda(courier.getPda());
//		c.setDeltag(courier.getDeltag());
//		c.setCheckPwd(courier.getCheckPwd());
//		c.setType(courier.getType());
//		c.setCompany(courier.getCompany());
//		c.setVehicleType(courier.getVehicleType());
//		c.setVehicleNum(courier.getVehicleNum());
//	}

	@Override
	public List<Courier> findNoAssciation() {
		Specification<Courier> specification = new Specification<Courier>() {
			
			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.isEmpty(root.get("fixedAreas").as(Set.class));
			}
		};
		return courierRepository.findAll(specification);
	}
}
