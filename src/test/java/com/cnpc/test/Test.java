package com.cnpc.test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Properties;

import org.hyperic.jni.ArchNotSupportedException;
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
import org.hyperic.sigar.Swap;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StreamUtils;

//测试类
public class Test {
	  static{
		try {
			  initSigar();
		} catch (Exception e) {
			e.printStackTrace();
		}
	  }
   //初始化sigar的配置文件
   private static void initSigar() throws IOException { 
		   SigarLoader loader = new SigarLoader(Sigar.class);  
		   String lib = null;     
		   try {        
			   lib = loader.getLibraryName(); 
			   System.out.println(lib);
		   } catch (ArchNotSupportedException var7) {  
			   ResourceLoader resourceLoader = new DefaultResourceLoader(); 
			   Resource resource = resourceLoader.getResource("classpath:/sigar/" + lib);   
			   if (resource.exists()) {   
				   //System.out.println(resource.getFilename());
				   InputStream is = resource.getInputStream();      
				   File tempDir = new File("./sigar");   
				   BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(new File(tempDir, lib), false));
				   StreamUtils.copy(is, os);       
				   is.close();       
				   os.close();        
				   System.setProperty("org.hyperic.sigar.path",tempDir.getCanonicalPath());  
		   }
       }
   }
   public static void main(String[] args) throws SigarException, UnknownHostException {
	   Sigar sigar = new Sigar();
       CpuInfo[] cpuInfoList = sigar.getCpuInfoList();
       for(int i=0;i<cpuInfoList.length;i++){
    	   System.out.println("CPU的总量MHz   "+cpuInfoList[i].getMhz());
    	   
       }
       CpuPerc[] cpus=sigar.getCpuPercList();
       for(int i=0;i<cpus.length;i++){
    	   CpuPerc cpu=cpus[i];
    	   System.out.println(i+"CPU用户使用率:    "+CpuPerc.format(cpu.getUser()));// 用户使用率  
	   	   System.out.println(i+"CPU系统使用率:    "+CpuPerc.format(cpu.getSys()));// 系统使用率  
	   	   System.out.println(i+"CPU当前等待率:    "+CpuPerc.format(cpu.getWait()));// 当前等待率  
	   	   System.out.println(i+"CPU当前错误率:    "+CpuPerc.format(cpu.getNice()));//  
	   	   System.out.println(i+"CPU当前空闲率:    "+CpuPerc.format(cpu.getIdle()));// 当前空闲率  
	   	   System.out.println(i+"CPU总的使用率:    "+CpuPerc.format(cpu.getCombined()));// 总的使用率
       }
		
        Mem mem=sigar.getMem();//
        System.out.println("内存总量:    "+mem.getTotal()/(1024L*1024L*1024L)+"GB");
		System.out.println("当前内存使用量:    "+mem.getUsed()/(1024L*1024L*1024L)+"GB");// 当前内存使用量  
		System.out.println("当前内存剩余量:    "+mem.getFree()/(1024L*1024L*1024L)+"GB");// 当前内存剩余量
		Swap swap=sigar.getSwap();
		System.out.println("交换区总量:"+swap.getTotal()/(1024L*1024L*1024L)+"GB");// 交换区总量 
		System.out.println("当前交换区使用量:    "+swap.getUsed()/(1024L*1024L*1024L)+"GB");// 当前交换区使用量
		System.out.println("当前交换区剩余量:    "+swap.getFree()/(1024L*1024L*1024L)+"GB");// 当前交换区剩余量
		file(sigar);
		//net(sigar);
		//baseInfo();
   }
   public static void file(Sigar sigar) throws SigarException{
	   if(sigar!=null){
		   FileSystem[] fslist = sigar.getFileSystemList();
		   if(fslist!=null){
			   for(int i=0;i<fslist.length;i++){
				  FileSystem fs=fslist[i];
				  if(fs!=null){
					  if(fs.getType()==5){
						  continue;
					  }
					  System.out.println("盘符名称:    "+fs.getDevName());
					// 分区的盘符名称
					  System.out.println("盘符路径:    "+fs.getDirName());
					  System.out.println("盘符标志:    "+fs.getFlags());
					// 文件系统类型，比如 FAT32、NTFS
					  System.out.println("盘符类型:    "+fs.getSysTypeName());
					// 文件系统类型名，比如本地硬盘、光驱、网络文件系统等
					  System.out.println("盘符类型名:    "+fs.getTypeName());
					// 文件系统类型
					  System.out.println("盘符文件系统类型:    "+fs.getType());
					  FileSystemUsage usage=null;
					  try {
						    usage = sigar.getFileSystemUsage(fs.getDirName()); } 
					  catch (Exception e) {//当fileSystem.getType()为5时会出现该异常——此时文件系统类型为光驱                
						  System.out.println("----------------------------------------------------------------------------------");               
						  System.out.println(fs.getDirName());                //经测试，会输出个G:\ 我表示是相当的不解。后来发现是我笔记本的光驱，吐血。。。这也行。怪不得原来这代码是OK的                //估计是台式机，还是没光驱的台式机。               
					   }
					  if(usage!=null){
						  switch (fs.getType()) {
						case 0://TYPE_UNKNOWN ：未知
							
							break;
                        case 1://TYPE_NONE
                        	
							break;
						case 2://TYPE_LOCAL_DISK : 本地硬盘
							// 文件系统总大小
							System.out.println(fs.getDevName()+"总大小:    "+usage.getTotal()+"KB");
							// 文件系统剩余大小
							System.out.println(fs.getDevName()+"剩余大小:    "+usage.getFree()+"KB");
							// 文件系统可用大小
							System.out.println(fs.getDevName()+"可用大小:    "+usage.getAvail()+"KB");
							// 文件系统已经使用量
							System.out.println(fs.getDevName()+"已经使用量:    "+usage.getUsed()+"KB");
							double usePercent=usage.getUsePercent()*100D;
							// 文件系统资源的利用率
							System.out.println(fs.getDevName()+"资源的利用率:    "+usePercent+"%");
							break;
						case 3://TYPE_NETWORK ：网络
							System.out.println("-------------------------------------网络");
							break;
						case 4://TYPE_RAM_DISK ：闪存
							
							break;
						case 5://TYPE_CDROM ：光驱
							
							break;

						case 6://TYPE_SWAP ：页面交换
							
							break;
					  }
				  }
			   }
		   }
		   
	   }
   }
  }
  //网络设备信息
  public static void net(Sigar sigar) throws SigarException{
	    if(sigar!=null){
	    	String[] ifNames=sigar.getNetInterfaceList();
	    	if(ifNames!=null && ifNames.length>0){
	    		for(int i=0;i<ifNames.length;i++){
					String name=ifNames[i];
					NetInterfaceConfig ifconfig=sigar.getNetInterfaceConfig(name);
					if(ifconfig!=null){
						//如果网络设备没有正常启用，则跳过.
						if((ifconfig.getFlags()&1L)<=0L){
							System.out.println("!IFF_UP...skipping getNetInterfaceStat");
							  continue;
							}
						System.out.println("网络设备名:    "+name);// 网络设备名  
						System.out.println("IP地址:    "+ifconfig.getAddress());// IP地址  
						System.out.println("子网掩码:    "+ifconfig.getNetmask());// 子网掩码  
						NetInterfaceStat ifstat=sigar.getNetInterfaceStat(name);
						//如果发送和接收包的对象不为null,怎进行流量统计
						if(ifstat!=null){
							System.out.println(name+"接收的总包裹数:"+ifstat.getRxPackets());// 接收的总包裹数  
							System.out.println(name+"发送的总包裹数:"+ifstat.getTxPackets());// 发送的总包裹数  
							System.out.println(name+"接收到的总字节数:"+ifstat.getRxBytes());// 接收到的总字节数  
							System.out.println(name+"发送的总字节数:"+ifstat.getTxBytes());// 发送的总字节数  
							System.out.println(name+"接收到的错误包数:"+ifstat.getRxErrors());// 接收到的错误包数  
							System.out.println(name+"发送数据包时的错误数:"+ifstat.getTxErrors());// 发送数据包时的错误数  
							System.out.println(name+"接收时丢弃的包数:"+ifstat.getRxDropped());// 接收时丢弃的包数  
							System.out.println(name+"发送时丢弃的包数:"+ifstat.getTxDropped());
						}
					}
				}
	    	}
	    }
	}
  public static void baseInfo() throws UnknownHostException{
	    Runtime r =Runtime.getRuntime();
  	    Properties props =System.getProperties();
  	    InetAddress addr;
		addr=InetAddress.getLocalHost();
		String ip=addr.getHostAddress();
		Map<String,String> map=System.getenv();
		String userName=map.get("USERNAME");// 获取用户名  
		String computerName=map.get("COMPUTERNAME");// 获取计算机名  
		String userDomain=map.get("USERDOMAIN");// 获取计算机域名  
		System.out.println("用户名:    "+userName);
		System.out.println("计算机名:    "+computerName);
		System.out.println("计算机域名:    "+userDomain);
		System.out.println("本地ip地址:    "+ip);
		System.out.println("本地主机名:    "+addr.getHostName());
	    System.out.println("JVM可以使用的总内存:    "+new BigDecimal(r.totalMemory()/(1024*1024)).doubleValue());
		System.out.println("JVM可以使用的剩余内存:    "+r.freeMemory());
		System.out.println("JVM可以使用的处理器个数:    "+r.availableProcessors());
		System.out.println("Java的运行环境版本：    "+props.getProperty("java.version"));
		System.out.println("Java的运行环境供应商：    "+props.getProperty("java.vendor"));
		System.out.println("Java供应商的URL：    "+props.getProperty("java.vendor.url"));
		System.out.println("Java的安装路径：    "+props.getProperty("java.home"));
		System.out.println("Java的虚拟机规范版本：    "+props.getProperty("java.vm.specification.version"));
		System.out.println("Java的虚拟机规范供应商：    "+props.getProperty("java.vm.specification.vendor"));
		System.out.println("Java的虚拟机规范名称：    "+props.getProperty("java.vm.specification.name"));
		System.out.println("Java的虚拟机实现版本：    "+props.getProperty("java.vm.version"));
		System.out.println("Java的虚拟机实现供应商：    "+props.getProperty("java.vm.vendor"));
		System.out.println("Java的虚拟机实现名称：    "+props.getProperty("java.vm.name"));
		System.out.println("Java运行时环境规范版本：    "+props.getProperty("java.specification.version"));
		System.out.println("Java运行时环境规范供应商：    "+props.getProperty("java.specification.vender"));
		System.out.println("Java运行时环境规范名称：    "+props.getProperty("java.specification.name"));
		System.out.println("Java的类格式版本号：    "+props.getProperty("java.class.version"));
		System.out.println("Java的类路径：    "+props.getProperty("java.class.path"));
		System.out.println("加载库时搜索的路径列表：    "+props.getProperty("java.library.path"));
		System.out.println("默认的临时文件路径：    "+props.getProperty("java.io.tmpdir"));
		System.out.println("一个或多个扩展目录的路径：    "+props.getProperty("java.ext.dirs"));
		System.out.println("操作系统的名称：    "+props.getProperty("os.name"));
		System.out.println("操作系统的构架：    "+props.getProperty("os.arch"));
		System.out.println("操作系统的版本：    "+props.getProperty("os.version"));
		System.out.println("文件分隔符：    "+props.getProperty("file.separator"));
		System.out.println("路径分隔符：    "+props.getProperty("path.separator"));
		System.out.println("行分隔符：    "+props.getProperty("line.separator"));
		System.out.println("用户的账户名称：    "+props.getProperty("user.name"));
		System.out.println("用户的主目录：    "+props.getProperty("user.home"));
		System.out.println("用户的当前工作目录：    "+props.getProperty("user.dir"));
  }
  
}
