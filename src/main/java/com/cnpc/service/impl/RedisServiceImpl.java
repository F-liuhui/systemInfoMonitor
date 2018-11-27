package com.cnpc.service.impl;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import com.cnpc.service.RedisService;

@Service
public class RedisServiceImpl implements RedisService {
	@Autowired
	RedisTemplate<String,String> template;
	@Override
	public Cursor<Entry<Object,Object>> getSystemInfos(String hashName) {
		Cursor<Entry<Object,Object>>  cursor=template.opsForHash().scan(hashName, ScanOptions.NONE);
		return cursor;
	}
	@Override
	public void setSystemInfo(String hashname,Map<String, String> map) {
		template.opsForHash().putAll(hashname, map);
	}
	@Override
	public void setSystemInfoOne(String hashname, String key, String val) {
		
		template.opsForHash().put(hashname, key, val);
	}
	@Override
	public String getSystemInfoForOne(String hashName, String key) {
		return (String) template.opsForHash().get(hashName, key);
	}
}
