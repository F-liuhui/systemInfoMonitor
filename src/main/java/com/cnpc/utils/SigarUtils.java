package com.cnpc.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarLoader;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StreamUtils;

import com.cnpc.bean.SystemInfo;

/**
 * 类描述：系统资源监控工具类（分装sigar）
 * 
 * 开发时间：2018年9月28日09:31:58
 * 
 * @author lh
 *
 */
public class SigarUtils {
	// 日志
	private static final Logger log = Logger.getLogger(SigarUtils.class);

	// 静态代码块初始化sigar
	static {
		try {
			initSigar();
		} catch (Exception e) {
			log.error("初始化sigar出错,结束本次的资源统计" + e);
		}
	}

	/**
	 * 
	   * 方法描述：初始化sigar
	 * 
	   * 开发时间：2018年9月28日09:33:12
	 * 
	 * params:void
	 * 
	 * @throws IOException
	 */
	private static void initSigar() throws IOException {
		SigarLoader loader = new SigarLoader(Sigar.class);
		String lib = null;
		try {
			lib = loader.getLibraryName();
		} catch (Exception e) {
			log.error("sigar获取不到该操作系统对应的 libraryname,无法进行资源统计" + e);
		}
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		// 获取资源。。
		Resource resource = resourceLoader.getResource("classpath:/sigar/" + lib);
		// 判断资源是否存在。
		if (resource.exists()) {
			InputStream is = resource.getInputStream();
			File tempDir = getUserHomeAsFile();
			BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(new File(tempDir, lib), false));
			StreamUtils.copy(is, os);
			is.close();
			os.close();
			System.setProperty("org.hyperic.sigar.path", tempDir.getCanonicalPath());
		}
	}

	/**
	   * 对外提供的获取系统资源信息的入口
	 * 
	 * 2018年9月28日14:46:32
	 * 
	 * lh
	 * 
	 */
	public static SystemInfo getSystemInfo() {
		Sigar sigar = new Sigar();
		SystemInfo systemInfo = new SystemInfo();
		systemInfo.setCollectDate(getCollectDate(new Date()));
		systemInfo = getNetInfo(
				getFileInfo(getMemInfo(getCpuInfo(getJvmUseInfo(getBaseInfo(systemInfo)), sigar), sigar), sigar),
				sigar);
		sigar = null;
		return systemInfo;
	}

	/**
	 * 方法描述：获取系统的基础信息
	 * 
	 * 开发时间：2018年9月28日09:54:40
	 * 
	 * @param map
	 * 
	 * @return Map
	 */
	private static SystemInfo getBaseInfo(SystemInfo systemInfo) {
		// 获取系统信息
		Map<String, String> envMap = System.getenv();
		// 获取用户名
		systemInfo.setUsername(envMap.get("USERNAME"));
		// 获取主机名
		systemInfo.setComputeName(envMap.get("COMPUTERNAME"));
		// 获取主机域名
		systemInfo.setComputeDomain(envMap.get("USERDOMAIN"));
		InetAddress addr;
		// 获取IP地址
		String ip = "";
		try {
			addr = InetAddress.getLocalHost();
			ip = addr.getHostAddress();

		} catch (UnknownHostException e) {
			log.error("获取IP地址出错" + e);
		}
		systemInfo.setIpAddr(ip);
		systemInfo.setOsname(System.getProperty("os.name"));
		systemInfo.setOsVersion(System.getProperty("os.version"));
		return systemInfo;
	}

	/**
	 * 方法描述：获取虚拟机资源信息
	 * 
	 * 开发时间：2018年9月28日10:19:38
	 * 
	 * @param map
	 * 
	 * @return
	 */
	private static SystemInfo getJvmUseInfo(SystemInfo systemInfo) {
		Runtime r = Runtime.getRuntime();
		Properties props = System.getProperties();
		// JVM可以使用的总内存
		systemInfo.setJvmTotalMemory(r.totalMemory());
		;
		// JVM可以使用的剩余内存
		systemInfo.setJvmFreeMemory(r.totalMemory());
		// JVM版本号
		systemInfo.setJavaVersion(props.getProperty("java.version"));
		// JVM可以使用的处理器个数
		systemInfo.setAvailableProcessors(r.availableProcessors());
		// 用户的家目录
		systemInfo.setUserHome(props.getProperty("user.home"));
		// 用户的工作目录
		systemInfo.setUserDir(props.getProperty("user.dir"));
		return systemInfo;

	}

	/**
	 * 
	 * CUP使用率统计
	 * 
	 * @param map
	 * @param sigar
	 * @return
	 */
	private static SystemInfo getCpuInfo(SystemInfo systemInfo, Sigar sigar) {
		if (sigar != null) {
			CpuInfo[] cpuInfoList;
			try {
				cpuInfoList = sigar.getCpuInfoList();
				if (cpuInfoList != null && cpuInfoList.length > 0) {
					// CPU核数
					int cpuCount = 0;
					cpuCount = cpuInfoList.length;
					systemInfo.setCpuCount(cpuCount);
					// CUP总的Mhz
					int cpuTotalMhz = 0;
					for (int i = 0; i < cpuInfoList.length; i++) {
						cpuTotalMhz += cpuInfoList[i].getMhz();
					}
					systemInfo.setCpuTotalMhz(cpuTotalMhz);
				}
				CpuPerc cpu = sigar.getCpuPerc();
				if (cpu != null) {
					// 当前用户使用率
					systemInfo.setCpuUserRate(CpuPerc.format(cpu.getUser()));
					// 当前系统使用率
					systemInfo.setCpuSysRate(CpuPerc.format(cpu.getSys()));
					// 当前等待率
					systemInfo.setCpuWaitRate(CpuPerc.format(cpu.getWait()));
					// 当前错误率
					systemInfo.setCpuNiceRate(CpuPerc.format(cpu.getNice()));
					// 当前空闲率
					systemInfo.setCpuLeisureRate(CpuPerc.format(cpu.getIdle()));
					// 总的使用率
					systemInfo.setCpuTotalRate(CpuPerc.format(cpu.getCombined()));
				}
			} catch (SigarException e) {
				log.error("获取CPU信息列表出错" + e);
			}
		}
		return systemInfo;
	}

	/**
	 * 获取内存信息
	 * 
	 * @param map
	 * @param sigar
	 * @return
	 */
	private static SystemInfo getMemInfo(SystemInfo systemInfo, Sigar sigar) {
		if (sigar != null) {
			Mem mem;
			try {
				mem = sigar.getMem();
				if (mem != null) {
					// 内存总量
					systemInfo.setMemTotal(mem.getTotal());
					// 当前内存使用量
					systemInfo.setMemUsed(mem.getUsed());
					// 当前内存剩余量
					systemInfo.setMemFree(mem.getFree());
				}
			} catch (SigarException e) {
				log.error("获取内存信息出错" + e);
			}
		}
		return systemInfo;

	}

	/**
	 * 获取硬盘信息
	 * 
	 * @param sigar
	 * @throws SigarException
	 */
	private static SystemInfo getFileInfo(SystemInfo systemInfo, Sigar sigar) {
		if (sigar != null) {
			FileSystem[] fslist;
			try {
				fslist = sigar.getFileSystemList();
				if (fslist != null && fslist.length > 0) {
					long fileTotal = 0;
					long fileFree = 0;
					long fileAvail = 0;
					long fileUse = 0;
					// String fileUsePercent=0+"%";
					for (int i = 0; i < fslist.length; i++) {
						FileSystem fs = fslist[i];
						if (fs != null) {
							// 如果为光盘，则跳过
							if (fs.getType() == 5) {
								continue;
							}
							FileSystemUsage usage = sigar.getFileSystemUsage(fs.getDirName());
							if (usage != null) {
								switch (fs.getType()) {
								case 0:// TYPE_UNKNOWN ：未知

									break;
								case 1:// TYPE_NONE

									break;
								case 2:// TYPE_LOCAL_DISK : 本地硬盘
										//  文件系统总大小
									fileTotal += usage.getTotal();
									//  文件系统剩余大小
									fileFree += usage.getFree();
									//  文件系统可用大小
									fileAvail += usage.getAvail();
									//  文件系统已经使用量
									fileUse += usage.getUsed();
									break;
								case 3:// TYPE_NETWORK ：网络
									break;
								case 4:// TYPE_RAM_DISK ：闪存

									break;
								case 5:// TYPE_CDROM ：光驱

									break;

								case 6:// TYPE_SWAP ：页面交换

									break;
								}
							}
						}
					}
					/*
					 * if(fileTotal!=0){ fileUsePercent=doubleFormat(fileUse/fileTotal)*100D+"%"; }
					 */
					fileTotal = fileTotal * 1024L;
					fileFree = fileFree * 1024L;
					fileAvail = fileAvail * 1024L;
					fileUse = fileUse * 1024L;
					//  文件系统总大小
					systemInfo.setFileTotal(fileTotal);
					//  文件系统剩余大小
					systemInfo.setFileFree(fileFree);
					//  文件系统可用大小
					systemInfo.setFileAvail(fileAvail);
					//  文件系统已经使用量
					systemInfo.setFileUse(fileUse);
					// 文件系统利用率没必要统计
					// map.put("fileUsePercent", fileUsePercent);
				}
			} catch (SigarException e) {
				log.info("获取磁盘使用信息出错" + e);
			}
		}
		return systemInfo;
	}

	/**
	 * 获取网络信息
	 * 
	 * @param sigar
	 */
	private static SystemInfo getNetInfo(SystemInfo systemInfo, Sigar sigar) {
		if (sigar != null) {
			String[] ifNames;
			try {
				ifNames = sigar.getNetInterfaceList();
				if (ifNames != null && ifNames.length > 0) {
					long rxNetTotalPackeg = 0;
					long rxNetTotalFlux = 0;
					long rxNetErrorPackeg = 0;
					long rxNetDropPackeg = 0;
					long TxNetTotalPackeg = 0;
					long TxNetTotalFlux = 0;
					long TxNetErrorPackeg = 0;
					long TxNetDropPackeg = 0;
					for (int i = 0; i < ifNames.length; i++) {
						String name = ifNames[i];
						NetInterfaceConfig ifconfig = sigar.getNetInterfaceConfig(name);
						if (ifconfig != null) {
							// 如果网络设备没有正常启用，则跳过.
							if ((ifconfig.getFlags() & 1L) <= 0L) {
								continue;
							}
							NetInterfaceStat ifstat = sigar.getNetInterfaceStat(name);
							// 如果发送和接收包的对象不为null,怎进行流量统计
							if (ifstat != null) {
								rxNetTotalPackeg += ifstat.getRxPackets();
								TxNetTotalPackeg += ifstat.getTxPackets();
								rxNetTotalFlux += ifstat.getRxBytes();
								TxNetTotalFlux += ifstat.getTxBytes();
								rxNetErrorPackeg += ifstat.getRxErrors();
								TxNetErrorPackeg += ifstat.getTxErrors();
								rxNetDropPackeg += ifstat.getRxDropped();
								TxNetDropPackeg += ifstat.getTxDropped();
							}
						}
					}
					//  接收的总包裹数
					systemInfo.setRxNetTotalPackeg(rxNetTotalPackeg);
					//  发送的总包裹数
					systemInfo.setTxNetTotalPackeg(TxNetTotalPackeg);
					//  接收到的总字节数 
					systemInfo.setRxNetTotalFlux(rxNetTotalFlux);
					//  发送的总字节数  
					systemInfo.setTxNetTotalFlux(TxNetTotalFlux);
					//  接收到的错误包数 
					systemInfo.setRxNetErrorPackeg(rxNetErrorPackeg);
					//  发送数据包时的错误数  
					systemInfo.setTxNetErrorPackeg(TxNetErrorPackeg);
					//  接收时丢弃的包数  
					systemInfo.setRxNetDropPackeg(rxNetDropPackeg);
					//  发送时丢弃的包数  
					systemInfo.setTxNetDropPackeg(TxNetDropPackeg);
				}
			} catch (SigarException e) {
				log.error("获取网络流量失败" + e);
			}
		}
		return systemInfo;
	}

	/**
	 * 方法描述：在用户的家目录下生成一个文件夹，用来存放sigar驱动文件。
	 * 
	 * 开发时间：2018年9月28日09:51:31
	 * 
	 * params：void
	 * 
	 * @return
	 */
	private static File getUserHomeAsFile() {
		String userHome = System.getProperty("user.home");
		File file = new File(userHome, "sigar.temp");
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}

	/**
	 * 获取统计时间
	 * 
	 * @param date
	 * @return
	 */
	private static String getCollectDate(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sringDate = "";
		if (date != null) {
			sringDate = format.format(date);
		}
		return sringDate;
	}
}
