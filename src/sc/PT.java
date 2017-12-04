package sc;

//内存分区表孔类
public class PT {
	private int size;  //孔大小
	private int address;  //孔起始地址
	private boolean state;  //孔状态，true为已分配，false为未分配
	
	//构造函数
	public PT() {}
	
	public PT(int size,int address,boolean state) {
		this.size = size;
		this.address = address;
		this.state = state;
	}
	
	//各参数的get和set函数
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
	public boolean isState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
	}

}
