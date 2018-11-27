package com.cnpc.service;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.data.redis.core.Cursor;
/**
 * service接口
 * 
 * @author admin
 *
 */
public interface RedisService {
    /**
     * 获取redis hash中所有数据
     * 
     * lh
     * 
     * 2018年10月9日13:13:53
     * 
     * 
     * @param hashName
     * @return
     */
	public Cursor<Entry<Object,Object>> getSystemInfos(String hashName);
	
	/**
	 * 获取redis hash中某个Key的值
	 * 
	 * lh
	 * 
	 * 
	 * 2018年10月9日13:14:38
	 * 
	 * @param hashName
	 * @param key
	 * @return
	 */
	public String getSystemInfoForOne(String hashName,String key);
	/**
	 * 设置redis hash 的值（批量设置）
	 * 
	 * lh
	 * 
	 * 2018年10月9日13:15:26
	 * 
	 * @param hashname
	 * @param map
	 */
	public void setSystemInfo(String hashname,Map<String,String> map);
	
	/**
	 * 设置redis hash 的值
	 * 
	 * lh
	 * 
	 * 2018年10月9日13:15:26
	 * 
	 * @param hashname
	 * @param map
	 */
	public void setSystemInfoOne(String hashname,String key,String val);
}
