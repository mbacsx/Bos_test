package cn.itcast.crm.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.crm.dao.CustomerRepository;
import cn.itcast.crm.domain.Customer;
import cn.itcast.crm.service.CustomerService;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	// 注入DAO
	@Autowired
	private CustomerRepository customerRepository;

	@Override
	// 查询所有未关联客户,fixedId为null的
	public List<Customer> findNoAssociationCustomers() {
		return customerRepository.findByFixedAreaIdIsNull();
	}

	@Override
	// 已经关联到指定定区的客户列表
	public List<Customer> findHasAssociationFixedAreaCustomers(String fixedAreaId) {
		return customerRepository.findByFixedAreaId(fixedAreaId);
	}

	@Override
	// 客户关联定区
	public void associationCustomersToFixedArea(String customerIdStr, String fixedAreaId) {
		// 如果id数组为null就说明没有需要关联的客户,直接解除与当前定区关联的客户
		if(StringUtils.equals(customerIdStr, "null")){
			customerRepository.clearFixedAreaId(fixedAreaId);
			return;
		}
		// 先解除所有客户关联的定区
		customerRepository.clearFixedAreaId(fixedAreaId);
		String[] cusId = customerIdStr.split(",");
		for (String id : cusId) {
			customerRepository.updateFixedAreaId(fixedAreaId, Integer.parseInt(id));
		}
	}

	@Override
	public void regist(Customer model) {
		customerRepository.save(model);
	}

	@Override
	public Customer findByTelephone(String telephone) {
		return customerRepository.findByTelephone(telephone);
	}

	@Override
	public void updateType(String telephone) {
		customerRepository.updateType(telephone);
	}
}
