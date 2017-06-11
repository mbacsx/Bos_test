package cn.itcast.bos.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.bos.service.take_delivery.WaybillService;

public class WaybillIndexJob implements Job {
	
	@Autowired
	private WaybillService waybillService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("同步索引库...");
		waybillService.synIndex();
	}

}
