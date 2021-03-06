package cn.itcast.bos.utils;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

public class AliDaYuUtils {
	
	public static String sendMessage(String telephone, String paramString,String templateCode) throws ApiException{
		TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "阿里大于账号", "阿里大于授权码");
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		
		req.setExtend("123456");
		req.setSmsType("normal");
		req.setSmsFreeSignName("速运快递");
		// 模版参数
		req.setSmsParamString(paramString);
		// 收件人号码
		req.setRecNum(telephone);
		// 模版编号
		req.setSmsTemplateCode(templateCode);
		AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
		// 发送短信,返回结果值
		return rsp.getBody();
	}
}
