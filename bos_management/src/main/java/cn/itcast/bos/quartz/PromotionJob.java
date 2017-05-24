package cn.itcast.bos.quartz;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.bos.service.base.PromotionService;

/**
 *  定时关闭活动任务
 * @author May
 *
 */
public class PromotionJob implements Job {
	
	@Autowired
	private PromotionService promotionService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("执行活动到期时间检查...");
		// 每分钟传一个当前时间过去,如果结束时间小于当前时间就修改状态
		promotionService.updateStatus(new Date());
	}

}
