package cn.itcast.bos.utils;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

public class AliDaYuUtils {
	
	public static String sendMessage(String telephone, String checkCode) throws ApiException{
		TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "23845724", "fb5f670d23be16d24a4e1c8201114560");
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		
		req.setExtend("123456");
		req.setSmsType("normal");
		req.setSmsFreeSignName("速运快递");
		req.setSmsParamString("{'code':'"+checkCode+"'}");
		req.setRecNum(telephone);
		req.setSmsTemplateCode("SMS_67740118");
		AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
		// 发送短信,返回结果值
		return rsp.getBody();
	}
}
