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
		// 发送短信
		String result;
		try {
			// 发送结果值
			result = AliDaYuUtils.sendMessage(mapMessage.getString("telephone"), mapMessage.getString("randomNumeric"));
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
