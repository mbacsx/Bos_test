package cn.itcast.bos.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

public class MailUtils {
	private static String smtp_host = "smtp.qq.com";
	private static String username = "邮箱"; // 邮箱账户
	private static String password = "邮箱授权码"; // 邮箱授权码

	private static String from = "cmstore@qq.com"; // 使用当前账户

	public static void sendMail( String to, String content) {
		Properties props = new Properties();
		props.put("mail.smtp.host", smtp_host);
		props.put("mail.smtp.port", "25");
		props.setProperty("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.setProperty("mail.smtp.starttls.enable", "true");
		Session session = Session.getInstance(props);
		Message message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(from));
			message.setRecipient(RecipientType.TO, new InternetAddress(to));
			message.setSubject("来自速运快递的激活邮件");
			message.setContent(content, "text/html;charset=utf-8");
			Transport transport = session.getTransport();
			transport.connect(smtp_host, username, password);
			transport.sendMessage(message, message.getAllRecipients());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("邮件发送失败...");
		}
	}
}
