package cn.itcast.bos.service.take_delivery.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.AreaRepository;
import cn.itcast.bos.dao.base.FixedAreaRepository;
import cn.itcast.bos.dao.take_delivery.OrderRepository;
import cn.itcast.bos.dao.take_delivery.WorkBillRepository;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.domain.take_delivery.WorkBill;
import cn.itcast.bos.service.take_delivery.OrderService;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private FixedAreaRepository fixedAreaRepository;
	
	@Autowired
	private AreaRepository areaRepository;

	@Autowired
	private WorkBillRepository workBillRepository;

	// 注入activeMQ模版
	@Autowired
	@Qualifier("jmsQueueTemplate")
	private JmsTemplate jmsTemplate;

	@Override
	public void saveOrder(Order order) {
		// 设置订单id
		order.setOrderNum(UUID.randomUUID().toString());
		// 设置订单时间
		order.setOrderTime(new Date());
		// 状态,1为待取件
		order.setStatus("1");
		
		// 瞬时态对象需查询
		Area sendAreaData = areaRepository.findByProvinceAndCityAndDistrict(order.getSendArea().getProvince(),order.getSendArea().getCity(),order.getSendArea().getDistrict());
		Area recAreaData = areaRepository.findByProvinceAndCityAndDistrict(order.getRecArea().getProvince(),order.getRecArea().getCity(),order.getRecArea().getDistrict());
		order.setSendArea(sendAreaData);
		order.setRecArea(recAreaData);

		/**
		 * 基于crm客户地址库来自动分单
		 */
		String fixedAreaId = WebClient
				.create("http://localhost:8088/crm_management/services/customerService/customer/findFixedAreaIdByAddress?sendAddress="
						+ order.getSendAddress())
				.accept(MediaType.APPLICATION_JSON).get(String.class);
		if (fixedAreaId != null) {
			// 找到定区
			FixedArea fixedArea = fixedAreaRepository.findOne(fixedAreaId);
			// 找到快递员
			Iterator<Courier> iterator = fixedArea.getCouriers().iterator();
			// 是否有下一个
			if (iterator.hasNext()) {
				Courier courier = iterator.next();
				// 自动分单成功
				System.out.println("基于crm地址库来自动分单成功...");
				// 关联快递员
				order.setCourier(courier);
				// 设置分单类型
				order.setOrderType("1");
				// 保存
				orderRepository.save(order);
				// 生成工单
				generateWorkBill(order);
				return;
			}
		}

		/**
		 * 基于匹配分区关键字找到定区再找到快递员
		 */
		// 关键字
		for (SubArea subarea : sendAreaData.getSubareas()) {
			// 如果寄件人地址包含分区关键字
			if (order.getSendAddress().contains(subarea.getKeyWords())) {
				Iterator<Courier> it = subarea.getFixedArea().getCouriers().iterator();
				// 是否有下一个
				if (it.hasNext()) {
					Courier courier = it.next();
					// 自动分单成功
					System.out.println("基于匹配分区关键字来自动分单成功...");
					// 关联快递员
					order.setCourier(courier);
					// 设置分单类型
					order.setOrderType("1");
					// 保存
					orderRepository.save(order);
					// 生成工单
					generateWorkBill(order);
					return;
				}
			}
		}
		// 辅助关键字
		for (SubArea subarea : sendAreaData.getSubareas()) {
			// 如果寄件人地址包含分区关键字
			if (order.getSendAddress().contains(subarea.getAssistKeyWords())) {
				Iterator<Courier> it = subarea.getFixedArea().getCouriers().iterator();
				// 是否有下一个
				if (it.hasNext()) {
					Courier courier = it.next();
					// 自动分单成功
					System.out.println("基于匹配分区关键字来自动分单成功...");
					// 关联快递员
					order.setCourier(courier);
					// 设置分单类型
					order.setOrderType("1");
					// 保存
					orderRepository.save(order);
					// 生成工单
					generateWorkBill(order);
					return;
				}
			}
		}
		
		/**
		 * 人工分单
		 */
		// 设置分单类型
		order.setOrderType("2");
		// 保存
		orderRepository.save(order);
	}

	// 生成工单,发送短信
	private void generateWorkBill(final Order order) {
		WorkBill bill = new WorkBill();
		bill.setType("新");
		bill.setPickstate("新单");
		bill.setBuildtime(new Date());
		bill.setRemark(order.getRemark());
		final String smsNumber = RandomStringUtils.randomNumeric(4);
		bill.setSmsNumber(smsNumber);// 短信序号
		bill.setOrder(order);
		bill.setCourier(order.getCourier());
		workBillRepository.save(bill);

		// 发送短信
		jmsTemplate.send("bos_courier", new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				MapMessage mapMessage = session.createMapMessage();
				// 快递员手机号
				mapMessage.setString("telephone", order.getCourier().getTelephone());
				// 短信序号
				mapMessage.setString("smsNumber", smsNumber);
				// 取件地址
				mapMessage.setString("sendAddress", order.getSendAddress());
				// 联系人
				mapMessage.setString("sendName", order.getSendName());
				// 联系电话
				mapMessage.setString("sendPhone", order.getSendMobile());
				// 给快递员的悄悄话
				mapMessage.setString("sendMobileMsg", order.getSendMobileMsg());
				return mapMessage;
			}
		});
	}

}
