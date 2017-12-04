package sc;

import java.util.*;

//�����̨�������㷨���ڴ�����㷨��
public class PSA {
	private List<PCB> list = new ArrayList<PCB>();  //��������
	private List<PCB> waitlist = new ArrayList<PCB>();  //�󱸶���
	private List<PCB> uplist = new ArrayList<PCB>();  //�������
	
	private List<PT> ramlist = new ArrayList<PT>();  //�ڴ������
	
	private int runningtime = 0;  //��¼CPU������ʱ��
	
	//�����ʼ�������к��ڴ������
	public void init() {
		PCB [] p = new PCB[6];  //�������6���������������
		PCB [] q = new PCB[14];  //�������14��������󱸶���
		for(int i = 0;i<6;i++)
			p[i] = new PCB(name(i),random(),random(),"����",random2());
		
		for(int i = 0;i<14;i++)
			q[i] = new PCB(name(i+6),random(),random(),"��",random2());
		
		for(int i =0;i<6;i++)
			list.add(p[i]);
		
		for(int i = 0;i<14;i++)
			waitlist.add(q[i]);
		
		PT system = new PT(100,0,true);  //����ϵͳ��פ���ڴ�ռ�
		PT hole = new PT(923,101,false);
		
		ramlist.add(system);
		ramlist.add(hole);
		
		//�����ڴ�
		for(int i=0;i<getList().size();i++) {
		   addram(getList().get(i));
		}
	}
	
	//�����кͱ�Ŀ��get����
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

	//0-20���������������ʱ�䣬���ȼ�
	private int random() {
		Random random = new Random();
		int n = random.nextInt(20)+1;
		return n;
	}
	
	//1-200����������������ڴ�
	private int random2() {
		Random random = new Random();
		int n = random.nextInt(300)+1;
		return n;
	}

	//Ԥ�����õĽ�����
	public String name(int i) {
		switch(i) {
		case 0: return "Windows ��Դ������";
		case 1: return "TIM(32λ)";
		case 2: return "�ѹ�ƴ�����뷨";
		case 3: return "Windows �Ự������";
		case 4: return "�������";
		case 5: return "Windows ��������";
		case 6: return "����Ϳ�����Ӧ��";
		case 7: return "Java(TM) SE";
		case 8: return "���������";
		case 9: return "360��ȫ��ʿ";
		case 10: return "System";
		case 11: return "ϵͳ�ж�";
		case 12: return "Google Chrome";
		case 13: return "System";
		case 14: return "ϵͳ�ж�";
		case 15: return "NVDIA Container";
		case 16: return "System";
		case 17: return "Steam Client";
		case 18: return "����������";
		case 19: return "League of legends";
		case 20: return "System";
		}
		return null;
	}
	
	//���̶��е������㷨��ð������
	public void sort(List<PCB> list) {
		for(int i = 0;i<list.size();i++)
			for(int j = 0;j<list.size()-1-i;j++)
				if(list.get(j).getPri() < list.get(j+1).getPri())
					Collections.swap(list,j,j+1);		
	}
	
    //�����̵������㷨
	public void run() {
		runningtime++;  //CUP����ʱ���һ
		list.get(0).setState("����");  //�趨���ȼ���ߵĽ���Ϊ����״̬
		if(list.get(0).getPri()>0)
		list.get(0).setPri(list.get(0).getPri()-1);  //���ȼ���һ
		list.get(0).setTime(list.get(0).getTime()-1);  //ʱ���һ
		
		//��ֹ�������̼���
		if(runningtime%1 == 0)  //CUPÿ����x�£��������������ȼ���һ ����ǰxΪ1��               
		if(list.size()>1)
		for(int i = 1;i<list.size();i++)
			list.get(i).setPri(list.get(i).getPri()+1);
	}
	
	//��ӽ���
	public void add(PCB p) {
		if(list.size()<6 && addram(p)) {  //����������δ�����㹻�����ڴ棬ֱ�Ӽ����������
		    p.setState("����");
		    list.add(p);
		}
		else {  //�����������󱸶���
			p.setState("��");
			waitlist.add(p);
		}
	}
	
	//Ϊ���̷����ڴ��㷨
	public boolean addram(PCB p) {
		boolean addok = false;  //����Ƿ�ɹ��ı�־
		for(int i=0;i<ramlist.size();i++) {  //�ӵ�һ���׿�ʼ����
			if(!ramlist.get(i).isState() && p.getSize()<=ramlist.get(i).getSize()) {  //���˿��ǿɷ�����Ҵ�С�㹻�˽��̼���
					int restsize = ramlist.get(i).getSize()-p.getSize();  //���÷�����ʣ��ռ��С
					int restaddress = ramlist.get(i).getAddress()+p.getSize();  //���÷�����ʣ��ռ���ʼ��ַ
					//�������
					ramlist.get(i).setSize(p.getSize());  
					ramlist.get(i).setState(true);  //���˿���Ϊ����
					p.setAddress(ramlist.get(i).getAddress());
					//����ʣ��ռ�Ϊ�¿�
					if(restsize!=0) {
					PT rest = new PT(restsize,restaddress,false);
					ramlist.add(i+1,rest);	
				    }
					addok = true;  //��ӳɹ�
					break;  //��������
			}	
		}
		return addok;  //�����Ƿ���ӳɹ�
	}
	
	//�����ڴ�ռ��㷨
	public void delram(PCB p) {
		int add = p.getAddress();  //��¼Ҫ�������̵���ʼ��ַ
		int size = p.getSize();  //��¼Ҫ�������̵��ڴ��С
		for(int i=1;i<ramlist.size();i++) {  
			if(ramlist.get(i).getAddress() == add) {  //����Ѱ�ҵ���
				//�����ÿײ��������ڿɷ���׺ϲ�
				int mergeadd = add;  //���úϲ������ʼ��ַ
				int mergesize = size;  //���úϲ�����ڴ��С
				if(!ramlist.get(i+1).isState()) {  //���ں�һ��Ϊ�ɷ���ģ��ϲ�
					mergesize += ramlist.get(i+1).getSize();
					ramlist.remove(i+1);
				}
				if(!ramlist.get(i-1).isState()) {  //����ǰһ��Ϊ�ɷ���ģ��ϲ�
					mergeadd = ramlist.get(i-1).getAddress();  //ע���������ʼ��ַҲҪ�ı�
				    mergesize += ramlist.get(i-1).getSize();
				    //���˿�����Ϊ�ϲ���Ŀ�
				    ramlist.get(i).setAddress(mergeadd);
				    ramlist.get(i).setSize(mergesize);
				    ramlist.get(i).setState(false);
				    ramlist.remove(i-1);
				    break;  //��������
				}
				//���˿�����Ϊ�ϲ���Ŀ�
				ramlist.get(i).setAddress(mergeadd);
				ramlist.get(i).setSize(mergesize);
				ramlist.get(i).setState(false);
				break;  //��������		
			}	
		}
	}
	
}