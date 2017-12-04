package sc;

import java.util.*;

//程序后台（调度算法和内存分配算法）
public class PSA {
	private List<PCB> list = new ArrayList<PCB>();  //就绪队列
	private List<PCB> waitlist = new ArrayList<PCB>();  //后备队列
	private List<PCB> uplist = new ArrayList<PCB>();  //挂起队列
	
	private List<PT> ramlist = new ArrayList<PT>();  //内存分区表
	
	private int runningtime = 0;  //记录CPU的运行时间
	
	//随机初始化各队列和内存分区表
	public void init() {
		PCB [] p = new PCB[6];  //随机设置6个进程入就绪队列
		PCB [] q = new PCB[14];  //随机设置14个进程入后备队列
		for(int i = 0;i<6;i++)
			p[i] = new PCB(name(i),random(),random(),"就绪",random2());
		
		for(int i = 0;i<14;i++)
			q[i] = new PCB(name(i+6),random(),random(),"后备",random2());
		
		for(int i =0;i<6;i++)
			list.add(p[i]);
		
		for(int i = 0;i<14;i++)
			waitlist.add(q[i]);
		
		PT system = new PT(100,0,true);  //操作系统常驻的内存空间
		PT hole = new PT(923,101,false);
		
		ramlist.add(system);
		ramlist.add(hole);
		
		//分配内存
		for(int i=0;i<getList().size();i++) {
		   addram(getList().get(i));
		}
	}
	
	//各队列和表目的get方法
	public List<PCB> getList() {
		return list;
	}
	public List<PCB> getWaitlist() {
		return waitlist;
	}
	public List<PCB> getUplist() {
		return uplist;
	}
	public List<PT> getRamlist() {
		return ramlist;
	}

	//0-20随机数，用于生成时间，优先级
	private int random() {
		Random random = new Random();
		int n = random.nextInt(20)+1;
		return n;
	}
	
	//1-200随机数，用于生成内存
	private int random2() {
		Random random = new Random();
		int n = random.nextInt(300)+1;
		return n;
	}

	//预先设置的进程名
	public String name(int i) {
		switch(i) {
		case 0: return "Windows 资源管理器";
		case 1: return "TIM(32位)";
		case 2: return "搜狗拼音输入法";
		case 3: return "Windows 会话管理器";
		case 4: return "控制面板";
		case 5: return "Windows 启动程序";
		case 6: return "服务和控制器应用";
		case 7: return "Java(TM) SE";
		case 8: return "任务管理器";
		case 9: return "360安全卫士";
		case 10: return "System";
		case 11: return "系统中断";
		case 12: return "Google Chrome";
		case 13: return "System";
		case 14: return "系统中断";
		case 15: return "NVDIA Container";
		case 16: return "System";
		case 17: return "Steam Client";
		case 18: return "网易云音乐";
		case 19: return "League of legends";
		case 20: return "System";
		}
		return null;
	}
	
	//进程队列的排序算法（冒泡排序）
	public void sort(List<PCB> list) {
		for(int i = 0;i<list.size();i++)
			for(int j = 0;j<list.size()-1-i;j++)
				if(list.get(j).getPri() < list.get(j+1).getPri())
					Collections.swap(list,j,j+1);		
	}
	
    //单进程的运行算法
	public void run() {
		runningtime++;  //CUP运行时间加一
		list.get(0).setState("运行");  //设定优先级最高的进程为运行状态
		if(list.get(0).getPri()>0)
		list.get(0).setPri(list.get(0).getPri()-1);  //优先级减一
		list.get(0).setTime(list.get(0).getTime()-1);  //时间减一
		
		//防止其他进程饥饿
		if(runningtime%1 == 0)  //CUP每运行x下，给其他进程优先级加一 （当前x为1）               
		if(list.size()>1)
		for(int i = 1;i<list.size();i++)
			list.get(i).setPri(list.get(i).getPri()+1);
	}
	
	//添加进程
	public void add(PCB p) {
		if(list.size()<6 && addram(p)) {  //若就绪队列未满且足够分配内存，直接加入就绪队列
		    p.setState("就绪");
		    list.add(p);
		}
		else {  //其他情况加入后备队列
			p.setState("后备");
			waitlist.add(p);
		}
	}
	
	//为进程分配内存算法
	public boolean addram(PCB p) {
		boolean addok = false;  //添加是否成功的标志
		for(int i=0;i<ramlist.size();i++) {  //从第一个孔开始遍历
			if(!ramlist.get(i).isState() && p.getSize()<=ramlist.get(i).getSize()) {  //若此孔是可分配的且大小足够此进程加入
					int restsize = ramlist.get(i).getSize()-p.getSize();  //设置分配后的剩余空间大小
					int restaddress = ramlist.get(i).getAddress()+p.getSize();  //设置分配后的剩余空间起始地址
					//分配进程
					ramlist.get(i).setSize(p.getSize());  
					ramlist.get(i).setState(true);  //将此孔设为已用
					p.setAddress(ramlist.get(i).getAddress());
					//分配剩余空间为新孔
					if(restsize!=0) {
					PT rest = new PT(restsize,restaddress,false);
					ramlist.add(i+1,rest);	
				    }
					addok = true;  //添加成功
					break;  //结束遍历
			}	
		}
		return addok;  //返回是否添加成功
	}
	
	//撤销内存空间算法
	public void delram(PCB p) {
		int add = p.getAddress();  //记录要撤销进程的起始地址
		int size = p.getSize();  //记录要撤销进程的内存大小
		for(int i=1;i<ramlist.size();i++) {  
			if(ramlist.get(i).getAddress() == add) {  //遍历寻找到孔
				//撤销该孔并与其相邻可分配孔合并
				int mergeadd = add;  //设置合并后的起始地址
				int mergesize = size;  //设置合并后的内存大小
				if(!ramlist.get(i+1).isState()) {  //相邻后一孔为可分配的，合并
					mergesize += ramlist.get(i+1).getSize();
					ramlist.remove(i+1);
				}
				if(!ramlist.get(i-1).isState()) {  //相邻前一孔为可分配的，合并
					mergeadd = ramlist.get(i-1).getAddress();  //注意这里的起始地址也要改变
				    mergesize += ramlist.get(i-1).getSize();
				    //将此孔设置为合并后的孔
				    ramlist.get(i).setAddress(mergeadd);
				    ramlist.get(i).setSize(mergesize);
				    ramlist.get(i).setState(false);
				    ramlist.remove(i-1);
				    break;  //结束遍历
				}
				//将此孔设置为合并后的孔
				ramlist.get(i).setAddress(mergeadd);
				ramlist.get(i).setSize(mergesize);
				ramlist.get(i).setState(false);
				break;  //结束遍历		
			}	
		}
	}
	
}