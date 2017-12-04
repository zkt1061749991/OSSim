package sc;

//进程类
public class PCB {
	private String name;  //进程名
	private int time;  //运行时间
	private int pri;  //优先级
	private String state;  //运行状态：就绪 后备 运行 挂起 阻塞
	private PCB next;  // 指向下一个对象的指指针
	private int size;  //内存大小
	private int address;  //分配的内存起始地址
	
	//构造方法
	public PCB() {}

	public PCB(String name, int time, int pri, String state,int size) {
		this.name = name;
		this.time = time;
		this.pri = pri;
		this.state = state;
		this.size = size;
	}

	//各参数的get和set函数
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPri() {
		return pri;
	}
	public void setPri(int pri) {
		this.pri = pri;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getAddress() {
		return address;
	}
	public void setAddress(int address) {
		this.address = address;
	}
	public PCB getNext() {
		return next;
	}
	public void setNext(PCB next) {
		this.next = next;
	}
	
}