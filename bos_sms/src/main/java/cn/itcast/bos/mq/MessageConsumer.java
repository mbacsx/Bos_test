package cn.itcast.bos.mq;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.stereotype.Service;

import cn.itcast.bos.utils.AliDaYuUtils;
/**
 * 消费者
 * @author May
 * 
 */
@Service("messageConsumer")
public class MessageConsumer implements MessageListener {

	@Override
	public void onMessage(Message message) {
		// 获取参数
		MapMessage mapMessage = (MapMessage) message;
		
		// 用户注册
		// 发送短信返回结果值
		try{
			// 模版参数
			String paramString = "{'code':'"+mapMessage.getString("randomNumeric")+"'}";
			String templateCode = "SMS_67740118";
			String result = AliDaYuUtils.sendMessage(mapMessage.getString("telephone"), paramString,templateCode);
			
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
