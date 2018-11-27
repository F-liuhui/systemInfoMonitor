package com.cnpc.job;
import org.apache.log4j.Logger;
import org.hyperic.sigar.SigarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cnpc.bean.SystemInfo;
import com.cnpc.service.RedisService;
import com.cnpc.utils.PropertiesUtils;
import com.cnpc.utils.SigarUtils;
/**
 * 调度任务类
 * 
 * 2018年10月9日13:16:47
 * 
 * @author admin
 *
 */
//开启调度功能
@EnableScheduling
@Component
public class SystemInfoMonitorJob {
  private static final Logger log=Logger.getLogger(SystemInfoMonitorJob.class);
  @Autowired
  RedisService redisService;
  //调度任务入口（五秒执行一次）
  @Scheduled(cron="0/5 * * * * ?")
  public void job(){
	  log.info("开始统计系统资源使用信息");
	  try{
		  SystemInfo systemInfo=SigarUtils.getSystemInfo();
		  if(systemInfo.getIpAddr()!=null && !systemInfo.getIpAddr().equals("")){
			  redisService.setSystemInfoOne(PropertiesUtils.getHashname("hash.name"), systemInfo.getIpAddr(),JSON.toJSONString(systemInfo));
		  }else{
			  log.warn("获取不到服务器IP，无法记录系统使用信息-------------------------");
		  }
	  }catch(Exception e){
		  if(e instanceof SigarException){
			  log.error("创建sigar对象时出错"+e);
		  }else {
			  log.error("将信息保存到redis缓存库时出错"+e);
		  }
	  }
	  log.info("系统资源使用信息统计结束");
  }
}
