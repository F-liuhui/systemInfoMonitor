package com.cnpc.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cnpc.service.RedisService;
import com.cnpc.utils.PropertiesUtils;
/**
 * 
 * 提供Rest服务的控制类
 * 
 * @author liuhui
 *
 */
@RestController
@RequestMapping("/getSystemInfo")
public class ManagerController {
	
	private static final Logger log=Logger.getLogger(ManagerController.class);
	//注入service
	@Autowired
	RedisService redisService;
	
	//获取全部信息
	@RequestMapping(value="/map",method=RequestMethod.GET)
	public Map<String,String> getSystemInfo(){
		Map<String,String> map=new HashMap<String, String>();
		Cursor<Map.Entry<Object,Object>>  cursor=redisService.getSystemInfos(PropertiesUtils.getHashname("hash.name"));
		if(cursor!=null && !cursor.isClosed()){
			while (cursor.hasNext()) {
				Map.Entry<Object,Object> mapEntry=cursor.next();
				map.put((String)mapEntry.getKey(),(String)mapEntry.getValue());
			}
			try {
				cursor.close();
			} catch (IOException e) {
				log.error("关闭springboot的cursor时出错"+e);
			}
		}
		
		return map;
	}
	//获取单个信息
	@RequestMapping(value="/one/{key:.+}",method=RequestMethod.GET)
	public String getSystemInfoOne(@PathVariable("key") String key){
		String  result=redisService.getSystemInfoForOne(PropertiesUtils.getHashname("hash.name"), key);
		if(result==null){
			result="";
			log.warn("找不到key为： "+key+" 的键，无法提供数据服务");
		}
		return result;
	}
}
