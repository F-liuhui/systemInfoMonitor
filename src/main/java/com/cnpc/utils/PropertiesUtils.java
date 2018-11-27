package com.cnpc.utils;

import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;

/***
 * 
 * properties文件的处理类
 * 
 * @author admin
 *
 */
public class PropertiesUtils {
	
    private static final Logger logger=Logger.getLogger(PropertiesUtils.class);
    
    
	public  static  String getHashname(String key){
		String returnValue="";
		if(key!=null && !key.equals("")){
			Properties p=System.getProperties();
			try {
				p.load(PropertiesUtils.class.getClassLoader().getResourceAsStream("info.properties"));
				returnValue=p.getProperty(key);
			} catch (IOException e) {
				logger.error("读取文件时出错，文件位置可能不正确"+e);
				e.printStackTrace();
			}
		}
		return returnValue.trim();
	}
	
	public  static  String getHashKey(String key){
		String returnValue="";
		if(key!=null && !key.equals("")){
			Properties p=System.getProperties();
			try {
				p.load(PropertiesUtils.class.getClassLoader().getResourceAsStream("application.properties"));
				returnValue=p.getProperty(key);
			} catch (IOException e) {
				logger.error("读取文件时出错，文件位置可能不正确"+e);
				e.printStackTrace();
			}
		}
		return returnValue.trim();
	}
	
}
