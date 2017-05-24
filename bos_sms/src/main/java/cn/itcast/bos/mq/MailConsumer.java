package cn.itcast.bos.mq;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.stereotype.Service;

import cn.itcast.bos.utils.MailUtils;
/**
 * mq邮件
 * @author May
 *
 */
@Service("mailConsumer")
public class MailConsumer implements MessageListener {

	@Override
	public void onMessage(Message message) {
		// 获取参数
		MapMessage mapMessage = (MapMessage) message;
		
		try {
			MailUtils.sendMail(mapMessage.getString("email"),mapMessage.getString("content") );
		} catch (JMSException e) {
			e.printStackTrace();
			throw new RuntimeException("邮件发送失败");
		}
	}

}
