package sc;

//������
public class PCB {
	private String name;  //������
	private int time;  //����ʱ��
	private int pri;  //���ȼ�
	private String state;  //����״̬������ �� ���� ���� ����
	private PCB next;  // ָ����һ�������ָָ��
	private int size;  //�ڴ��С
	private int address;  //������ڴ���ʼ��ַ
	
	//���췽��
	public PCB() {}

	public PCB(String name, int time, int pri, String state,int size) {
		this.name = name;
		this.time = time;
		this.pri = pri;
		this.state = state;
		this.size = size;
	}

	//��������get��set����
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