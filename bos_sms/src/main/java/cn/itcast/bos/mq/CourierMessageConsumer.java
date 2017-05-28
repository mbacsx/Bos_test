package cn.itcast.bos.mq;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.stereotype.Service;

import cn.itcast.bos.utils.AliDaYuUtils;

@Service("courierMessageConsumer")
public class CourierMessageConsumer implements MessageListener {

	@Override
	public void onMessage(Message message) {
		// 获取参数
		MapMessage mapMessage = (MapMessage) message;
		try {
			// 快递员取件
			String telephone = mapMessage.getString("telephone");
			
			// 短信编号
			String smsNumber = mapMessage.getString("smsNumber");
			// 取件地址
			String sendAddress = mapMessage.getString("sendAddress");
			// 联系人
			String sendName = mapMessage.getString("sendName");
			// 联系方式
			String sendPhone = mapMessage.getString("sendPhone");
			// 对快递员说的话
			String sendMobileMsg = mapMessage.getString("sendMobileMsg");
			
			// 短信模版参数
			String paramString = "{'math':'"+smsNumber+"','address':'"+sendAddress+"','name':'"+sendName+"','phone':'"+sendPhone+"','message':'"+sendMobileMsg+"'}";
			// 短信模版编号
			String templateCode = "SMS_69020443";
			
			String result = AliDaYuUtils.sendMessage(telephone, paramString,templateCode);
			System.out.println(result);
			
			if (result.contains("\"success\":true")) {
				// 发送成功
			} else {
				throw new RuntimeException("短信发送失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
