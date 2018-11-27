package com.cnpc.bean;

import java.io.Serializable;

public class SystemInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	/**
	 * 基础信息
	 */
	 //获取当前用户名
	 private String username;
	 //获取主机名
	 private String computeName;
	 //获取主机域名
	 private String computeDomain;
	 //IP地址
	 private String ipAddr;
	 //系统名
	 private String osname;
	 //系统版本号
     private String osVersion;
     //JVM可以使用的总内存
     private long  jvmTotalMemory;
 	 //JVM可以使用的剩余内存
 	 private long jvmFreeMemory;
     //JVM可以使用的CPU处理器个数
 	 private int availableProcessors;
 	 //JVM版本号
 	 private String javaVersion;
 	 //用户的家目录
 	 private String userHome;
 	 //工作目录
 	 private String userDir;
 	 
 	 /**
 	  * CPU信息
 	  */
 	 //CPU核数
 	 private int cpuCount; 
	 //CUP总的Mhz
	 private long cpuTotalMhz;
	 //当前用户使用率
	 private String cpuUserRate;
	 //当前系统使用率
	 private String cpuSysRate;
	 //当前等待率
	 private String cpuWaitRate;
	 //当前错误率
	 private String cpuNiceRate;
	 //当前空闲率
	 private String cpuLeisureRate;
	 //总的使用率
	 private String cpuTotalRate;
	 
	 /**
	  * 内存信息
	  */
	//内存总量
	 private long memTotal;
	//当前内存使用量
	 private long memUsed;;
	//当前内存剩余量
	 private long memFree;
	 
	 /**
	  * 文件系统信息
	  */
	// 文件系统总大小
	 private long fileTotal;
	// 文件系统剩余大小
	 private long fileFree;
	// 文件系统可用大小
	 private long fileAvail;
	// 文件系统已经使用量
	 private long fileUse;
	 
	 /**
	  * 网络IO信息
	  */
	// 发送的总包裹数
	 private long rxNetTotalPackeg;
	// 接收的总包裹数
	 private long TxNetTotalPackeg;
	// 发送的总字节数
	 private long rxNetTotalFlux;
	// 接收的总字节数
	 private long TxNetTotalFlux;
	// 发送数据包时的错误数 
	 private long rxNetErrorPackeg;
	// 接收数据包时的错误数 
	 private long TxNetErrorPackeg;
	// 发送时丢弃的包数 
	 private long rxNetDropPackeg;
	// 接收时丢弃的包数 
	 private long TxNetDropPackeg;
	 
	/**
	 * 数据采集时间
	 */
	 private String collectDate;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getComputeName() {
		return computeName;
	}

	public void setComputeName(String computeName) {
		this.computeName = computeName;
	}

	public String getComputeDomain() {
		return computeDomain;
	}

	public void setComputeDomain(String computeDomain) {
		this.computeDomain = computeDomain;
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public String getOsname() {
		return osname;
	}

	public void setOsname(String osname) {
		this.osname = osname;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public long getJvmTotalMemory() {
		return jvmTotalMemory;
	}

	public void setJvmTotalMemory(long jvmTotalMemory) {
		this.jvmTotalMemory = jvmTotalMemory;
	}

	public long getJvmFreeMemory() {
		return jvmFreeMemory;
	}

	public void setJvmFreeMemory(long jvmFreeMemory) {
		this.jvmFreeMemory = jvmFreeMemory;
	}

	public int getAvailableProcessors() {
		return availableProcessors;
	}

	public void setAvailableProcessors(int availableProcessors) {
		this.availableProcessors = availableProcessors;
	}

	public String getJavaVersion() {
		return javaVersion;
	}

	public void setJavaVersion(String javaVersion) {
		this.javaVersion = javaVersion;
	}

	public String getUserHome() {
		return userHome;
	}

	public void setUserHome(String userHome) {
		this.userHome = userHome;
	}

	public String getUserDir() {
		return userDir;
	}

	public void setUserDir(String userDir) {
		this.userDir = userDir;
	}

	public int getCpuCount() {
		return cpuCount;
	}

	public void setCpuCount(int cpuCount) {
		this.cpuCount = cpuCount;
	}

	public long getCpuTotalMhz() {
		return cpuTotalMhz;
	}

	public void setCpuTotalMhz(long cpuTotalMhz) {
		this.cpuTotalMhz = cpuTotalMhz;
	}

	public String getCpuUserRate() {
		return cpuUserRate;
	}

	public void setCpuUserRate(String cpuUserRate) {
		this.cpuUserRate = cpuUserRate;
	}

	public String getCpuSysRate() {
		return cpuSysRate;
	}

	public void setCpuSysRate(String cpuSysRate) {
		this.cpuSysRate = cpuSysRate;
	}

	public String getCpuWaitRate() {
		return cpuWaitRate;
	}

	public void setCpuWaitRate(String cpuWaitRate) {
		this.cpuWaitRate = cpuWaitRate;
	}

	public String getCpuNiceRate() {
		return cpuNiceRate;
	}

	public void setCpuNiceRate(String cpuNiceRate) {
		this.cpuNiceRate = cpuNiceRate;
	}

	public String getCpuLeisureRate() {
		return cpuLeisureRate;
	}

	public void setCpuLeisureRate(String cpuLeisureRate) {
		this.cpuLeisureRate = cpuLeisureRate;
	}

	public String getCpuTotalRate() {
		return cpuTotalRate;
	}

	public void setCpuTotalRate(String cpuTotalRate) {
		this.cpuTotalRate = cpuTotalRate;
	}

	public long getMemTotal() {
		return memTotal;
	}

	public void setMemTotal(long memTotal) {
		this.memTotal = memTotal;
	}

	public long getMemUsed() {
		return memUsed;
	}

	public void setMemUsed(long memUsed) {
		this.memUsed = memUsed;
	}

	public long getMemFree() {
		return memFree;
	}

	public void setMemFree(long memFree) {
		this.memFree = memFree;
	}

	public long getFileTotal() {
		return fileTotal;
	}

	public void setFileTotal(long fileTotal) {
		this.fileTotal = fileTotal;
	}

	public long getFileFree() {
		return fileFree;
	}

	public void setFileFree(long fileFree) {
		this.fileFree = fileFree;
	}

	public long getFileAvail() {
		return fileAvail;
	}

	public void setFileAvail(long fileAvail) {
		this.fileAvail = fileAvail;
	}

	public long getFileUse() {
		return fileUse;
	}

	public void setFileUse(long fileUse) {
		this.fileUse = fileUse;
	}

	public long getRxNetTotalPackeg() {
		return rxNetTotalPackeg;
	}

	public void setRxNetTotalPackeg(long rxNetTotalPackeg) {
		this.rxNetTotalPackeg = rxNetTotalPackeg;
	}

	public long getTxNetTotalPackeg() {
		return TxNetTotalPackeg;
	}

	public void setTxNetTotalPackeg(long txNetTotalPackeg) {
		TxNetTotalPackeg = txNetTotalPackeg;
	}

	public long getRxNetTotalFlux() {
		return rxNetTotalFlux;
	}

	public void setRxNetTotalFlux(long rxNetTotalFlux) {
		this.rxNetTotalFlux = rxNetTotalFlux;
	}

	public long getTxNetTotalFlux() {
		return TxNetTotalFlux;
	}

	public void setTxNetTotalFlux(long txNetTotalFlux) {
		TxNetTotalFlux = txNetTotalFlux;
	}

	public long getRxNetErrorPackeg() {
		return rxNetErrorPackeg;
	}

	public void setRxNetErrorPackeg(long rxNetErrorPackeg) {
		this.rxNetErrorPackeg = rxNetErrorPackeg;
	}

	public long getTxNetErrorPackeg() {
		return TxNetErrorPackeg;
	}

	public void setTxNetErrorPackeg(long txNetErrorPackeg) {
		TxNetErrorPackeg = txNetErrorPackeg;
	}

	public long getRxNetDropPackeg() {
		return rxNetDropPackeg;
	}

	public void setRxNetDropPackeg(long rxNetDropPackeg) {
		this.rxNetDropPackeg = rxNetDropPackeg;
	}

	public long getTxNetDropPackeg() {
		return TxNetDropPackeg;
	}

	public void setTxNetDropPackeg(long txNetDropPackeg) {
		TxNetDropPackeg = txNetDropPackeg;
	}

	public String getCollectDate() {
		return collectDate;
	}

	public void setCollectDate(String collectDate) {
		this.collectDate = collectDate;
	}
	 
	 
}
