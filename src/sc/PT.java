package sc;

//�ڴ���������
public class PT {
	private int size;  //�״�С
	private int address;  //����ʼ��ַ
	private boolean state;  //��״̬��trueΪ�ѷ��䣬falseΪδ����
	
	//���캯��
	public PT() {}
	
	public PT(int size,int address,boolean state) {
		this.size = size;
		this.address = address;
		this.state = state;
	}
	
	//��������get��set����
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
