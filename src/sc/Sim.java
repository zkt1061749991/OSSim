package sc;

import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import java.awt.Font;
import java.util.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

//����ǰ̨��������ʾ�Ͱ�ť���ܣ�
public class Sim {
	private PSA psa = new PSA();  //������̨����
	private boolean flag = false;  //�Ƿ������ұ�־
    private boolean stop = false;  //�Ƿ�����ֹͣ���б�־
    
	private JFrame frame;
	private JLabel lblCpu;
	private JLabel runningname;
	private JLabel label_1;
	private JLabel runningstate;
	private JLabel label_6;
	private JLabel number[] = new JLabel[20];
	private JButton btnNewButton;
	private JButton buttonadd;
	private JTextField textTime;
	private JTextField textPri;
	private JTextField textName;
	private JTextField textSize;
	
    private JScrollPane scrollPane_4;  
    private JTable table_ram;  //�ڴ������
    private JTable table_up;  //������б�
    private JPanel [] showram = new JPanel[102];  //�ڴ�ռ�����ͼ
    private JTable table_ready = new JTable();  //�������б�
	private JTable table_wait = new JTable();  //�󱸶��б�
    
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Sim window = new Sim();
					window.frame.setVisible(true);
					window.show();	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});	
	}

	public void show() {
		psa.init();  //��ʼ����̨
		
		//���ӻ�
		view();
	}
	
	//���Ȳ���
	public void start() {
		
		if(psa.getList().size()>0)  
		    psa.run();  //���н���
		
		//���ӻ�
		runningname.setText(psa.getList().get(0).getName());
		runningstate.setText("����");
		
		//���̽�������
		if(psa.getList().get(0).getTime() == -1) {
			
			runningstate.setText("��ֹ");
			
			psa.delram(psa.getList().get(0));  //�����ڴ�
			psa.getList().remove(0);  //�Ƴ���������
			
			//�����������ӽ���
			if(!flag) {  //��������ң��Ӻ󱸶��е�ȡ
			    if(psa.getWaitlist().size()>0) {
			        psa.sort(psa.getWaitlist());  //�󱸶��а����ȼ�����
			        for(int i=0;i<psa.getWaitlist().size();i++) {  //�����󱸶���ֱ���ڴ��㹻������
			            if(psa.addram(psa.getWaitlist().get(i))) {  
			                psa.getWaitlist().get(i).setState("����");
			                psa.getList().add(psa.getWaitlist().get(i));
			                psa.getWaitlist().remove(i);
			                break;  //��������
			            }
			        }
			    }
			}
			if(flag) {  //�����Ҫ��ң��ӹ�����е�ȡ
				if(psa.getUplist().size()>0) {  //���̼����������
				    psa.getUplist().get(0).setState("����");
				    psa.getList().add(psa.getUplist().get(0));
				    psa.getUplist().remove(0);
				    flag = false;  //�����ɣ���־�ر�
				}
			}	
		}
		//һ�ֵ��Ƚ���
		if(psa.getList().size()>0)
		    psa.getList().get(0).setState("����");
		//��ɺ�����
		psa.sort(psa.getList());
		//���ӻ�
		view();
		}
	
	//���ӻ�����
	public void view() {
		fillTable(psa.getList(),1);
		fillTable(psa.getWaitlist(),2);
		fillTable(psa.getUplist(),3);
		fillram(psa.getRamlist(),1);
		fillram(psa.getRamlist(),2);
	}

	public Sim() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("serial")
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 1179, 728);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 13, 451, 178);
		frame.getContentPane().add(scrollPane);
		table_ready.setFont(new Font("΢���ź�", Font.PLAIN, 16));
		
		
		scrollPane.setViewportView(table_ready);
		table_ready.setRowHeight(26);
		table_ready.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
			},
			new String[] {
				"\u8FDB\u7A0B\u540D", "\u5269\u4F59\u65F6\u95F4", "\u4F18\u5148\u7EA7", "\u72B6\u6001", "\u5185\u5B58", "\u8D77\u59CB\u5730\u5740"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class, Object.class, String.class, Object.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table_ready.getColumnModel().getColumn(0).setResizable(false);
		table_ready.getColumnModel().getColumn(0).setPreferredWidth(192);
		table_ready.getColumnModel().getColumn(1).setResizable(false);
		table_ready.getColumnModel().getColumn(1).setPreferredWidth(60);
		table_ready.getColumnModel().getColumn(2).setResizable(false);
		table_ready.getColumnModel().getColumn(2).setPreferredWidth(60);
		table_ready.getColumnModel().getColumn(3).setPreferredWidth(65);
		table_ready.getColumnModel().getColumn(4).setResizable(false);
		table_ready.getColumnModel().getColumn(4).setPreferredWidth(65);
		table_ready.getColumnModel().getColumn(5).setPreferredWidth(65);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(14, 214, 451, 266);
		frame.getContentPane().add(scrollPane_1);
		
		table_wait = new JTable();
		table_wait.setFont(new Font("΢���ź�", Font.PLAIN, 16));
		table_wait.setRowHeight(30);
		table_wait.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
			},
			new String[] {
				"\u8FDB\u7A0B\u540D", "\u6240\u9700\u65F6\u95F4", "\u4F18\u5148\u7EA7", "\u72B6\u6001", "\u6240\u9700\u5185\u5B58"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class, String.class, Object.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table_wait.getColumnModel().getColumn(0).setPreferredWidth(192);
		scrollPane_1.setViewportView(table_wait);
		
		lblCpu = new JLabel("CPU\u4E0A\u7684\u8FDB\u7A0B");
		lblCpu.setHorizontalAlignment(SwingConstants.CENTER);
		lblCpu.setFont(new Font("΢���ź�", Font.PLAIN, 25));
		lblCpu.setBounds(641, 35, 167, 27);
		frame.getContentPane().add(lblCpu);
		
		runningname = new JLabel("NULL");
		runningname.setHorizontalAlignment(SwingConstants.CENTER);
		runningname.setFont(new Font("΢���ź�", Font.PLAIN, 19));
		runningname.setBounds(621, 84, 209, 27);
		frame.getContentPane().add(runningname);
		
		label_1 = new JLabel("\u72B6\u6001");
		label_1.setFont(new Font("΢���ź�", Font.PLAIN, 25));
		label_1.setBounds(700, 124, 51, 27);
		frame.getContentPane().add(label_1);
		
		runningstate = new JLabel("\u5F53\u524D\u65E0\u8FDB\u7A0B\u8FD0\u884C");
		runningstate.setHorizontalAlignment(SwingConstants.CENTER);
		runningstate.setFont(new Font("΢���ź�", Font.PLAIN, 19));
		runningstate.setBounds(654, 164, 144, 27);
		frame.getContentPane().add(runningstate);
		
		btnNewButton = new JButton("\u8C03\u5EA6");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				psa.sort(psa.getList());
			
				start();
				
			}
		});
		
		btnNewButton.setBounds(514, 35, 86, 37);
		frame.getContentPane().add(btnNewButton);
		
		buttonadd = new JButton("\u6DFB\u52A0\u8FDB\u7A0B");
		buttonadd.addActionListener(new ActionListener() {
			//��ӽ��̲���
			public void actionPerformed(ActionEvent e) {
				//��ȡҪ��ӽ��̵����֡�ʱ�䡢���ȼ����ڴ�
				int addtime = Integer.parseInt(textTime.getText());
				int addpri = Integer.parseInt(textPri.getText());
				int addsize = Integer.parseInt(textSize.getText());
				String addname = textName.getText();
				//��ӽ���
				PCB addp = new PCB(addname,addtime,addpri,"�����",addsize);
				psa.add(addp);
				//���ӻ�
				view();
			}
		});
		buttonadd.setBounds(845, 417, 102, 57);
		frame.getContentPane().add(buttonadd);
		
		textTime = new JTextField();
		textTime.setText("30");
		textTime.setBounds(738, 417, 86, 24);
		frame.getContentPane().add(textTime);
		textTime.setColumns(10);
		
		textPri = new JTextField();
		textPri.setText("20");
		textPri.setBounds(564, 454, 86, 24);
		frame.getContentPane().add(textPri);
		textPri.setColumns(10);
		
		textName = new JTextField();
		textName.setText("New job");
		textName.setBounds(564, 417, 86, 24);
		frame.getContentPane().add(textName);
		textName.setColumns(10);
		
		JButton button = new JButton("\u6302\u8D77");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//�������
				psa.getList().get(0).setState("����");
				psa.getUplist().add(psa.getList().get(0));
				psa.getList().remove(0);
				//�Ӻ󱸶��е�ȡ����
				if(psa.getWaitlist().size()>0) {
					for(int i=0;i<psa.getWaitlist().size();i++) {  //�����󱸶���ֱ���ڴ��㹻����
						if(psa.addram(psa.getWaitlist().get(i))) {  //���̼����������
						psa.getWaitlist().get(i).setState("����");
						psa.getList().add(psa.getWaitlist().get(i));
						psa.getWaitlist().remove(i);
						break;  //��������
						}
					}
				}
				//���ӻ�
				view();
			}
		});
		button.setBounds(834, 111, 113, 27);
		frame.getContentPane().add(button);
		
		JButton button_1 = new JButton("\u89E3\u6302");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//��Ҳ���
				if(psa.getUplist().size() >0) {
					if(psa.getList().size()<6) {  //��������δ����ֱ�ӷ��ؾ�������
					    psa.getUplist().get(0).setState("����");
						psa.getList().add(psa.getUplist().get(0));
						psa.getUplist().remove(0);
					}
				    else {  //����������������Ҫ�ȴ�
				        flag = true;  //��Ҫ��ұ�־�򿪣��ȴ��´α�����
				        psa.getUplist().get(0).setState("����");
				    }
				}
			}
		});
		button_1.setBounds(834, 164, 113, 27);
		frame.getContentPane().add(button_1);
		
		JButton buttonstart = new JButton("\u5F00\u59CB\u8FD0\u884C");
		buttonstart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                //���в���
				new Thread(new Runnable() {
		            @Override
		            public void run() {
		                
		                while(psa.getList().size()>0 && !stop) {  //�������в�Ϊ����ֹͣ��־Ϊ�񣬲��Ͻ��е��Ȳ���
		                	btnNewButton.doClick();
		                    try {
		                        Thread.sleep(300);  //�����Զ����ȵ�ʱ��
		                    } catch (InterruptedException ex) {
		                        ex.printStackTrace();
		                    }
		                }
		                stop = false;  //ֹͣ���У���־�ر�
		            }
		        }).start();
			}
		});
		buttonstart.setBounds(503, 111, 113, 27);
		frame.getContentPane().add(buttonstart);
		
		JButton button_2 = new JButton("\u505C\u6B62\u8FD0\u884C");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stop = true;
				runningstate.setText("�ȴ�");
			}
		});
		button_2.setBounds(503, 164, 113, 27);
		frame.getContentPane().add(button_2);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(503, 214, 447, 183);
		frame.getContentPane().add(scrollPane_2);
		
		table_up = new JTable();
		table_up.setFont(new Font("΢���ź�", Font.PLAIN, 16));
		table_up.setRowHeight(30);
		table_up.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
			},
			new String[] {
				"\u8FDB\u7A0B\u540D", "\u5269\u4F59\u65F6\u95F4", "\u4F18\u5148\u7EA7", "\u72B6\u6001", "\u5185\u5B58", "\u8D77\u59CB\u5730\u5740"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class, String.class, Object.class, Object.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table_up.getColumnModel().getColumn(0).setPreferredWidth(190);
		scrollPane_2.setViewportView(table_up);
		
		textSize = new JTextField();
		textSize.setText("400");
		textSize.setBounds(738, 454, 86, 24);
		frame.getContentPane().add(textSize);
		textSize.setColumns(10);
		
		scrollPane_4 = new JScrollPane();
		scrollPane_4.setBounds(14, 502, 936, 167);
		frame.getContentPane().add(scrollPane_4);
		
		table_ram = new JTable();
		table_ram.setFont(new Font("΢���ź�", Font.PLAIN, 13));
		table_ram.setBackground(Color.WHITE);
		table_ram.setEnabled(false);
		table_ram.setRowHeight(15);
		table_ram.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
			},
			new String[] {
				"\u8D77\u59CB\u5730\u5740", "\u957F\u5EA6", "\u72B6\u6001"
			}
		));
		scrollPane_4.setViewportView(table_ram);
		
		JLabel label = new JLabel("\u8FDB\u7A0B\u540D");
		label.setBounds(503, 420, 45, 18);
		frame.getContentPane().add(label);
		
		JLabel label_2 = new JLabel("\u6240\u9700\u5185\u5B58");
		label_2.setBounds(664, 457, 60, 18);
		frame.getContentPane().add(label_2);
		
		JLabel label_3 = new JLabel("\u8FD0\u884C\u65F6\u95F4");
		label_3.setBounds(664, 420, 60, 18);
		frame.getContentPane().add(label_3);
		
		JLabel label_4 = new JLabel("\u4F18\u5148\u7EA7");
		label_4.setBounds(505, 457, 45, 18);
		frame.getContentPane().add(label_4);
		
		label_6 = new JLabel("1024");
		label_6.setBounds(967, 642, 37, 18);
		frame.getContentPane().add(label_6);
		
		JLabel label_7 = new JLabel("\u5185\u5B58\u5360\u7528\u60C5\u51B5");
		label_7.setBounds(1037, 21, 90, 18);
		frame.getContentPane().add(label_7);
	
		//�������̶ֿ�
		for(int i=0;i<20;i++) {
			int a = 0 + i*50;
			String num = String.valueOf(a);
		number[i] = new JLabel(num);
		number[i].setBounds(970, 50+30*i, 30, 15);
		frame.getContentPane().add(number[i]);
		}
		
		//�����ڴ�ռ��ͼ
		for(int i=0;i<102;i++) {
		showram[i] = new JPanel();
		showram[i].setBounds(1000, 50+6*i, 150, 6);
		showram[i].setBackground(Color.WHITE);
		showram[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
		frame.getContentPane().add(showram[i]);
		}
		
	}
 
	//����ڴ�������ռ�����ͼ����
	public void fillram(List<PT> list,int num) {
		if(num == 1) {
		 for(int r = 0;r<list.size();r++) {
		    if(list.get(r).isState()) {
		    	int add = list.get(r).getAddress();
		    	int s = list.get(r).getSize();
		    	for(int i=add/10;i<(add+s)/10;i++) {
		    		showram[i].setBackground(Color.CYAN);
		    	}
		    }
		    else {
		    	int add = list.get(r).getAddress();
		    	int s = list.get(r).getSize();
		    	for(int i=add/10;i<(add+s)/10;i++) {
		    		showram[i].setBackground(Color.WHITE);
		        }
		    }
		 }
		}
		if(num ==2) {
			for(int r = 0;r<30;r++) {
				  String clean = "";
				  table_ram.setValueAt(clean, r, 0);
				  table_ram.setValueAt(clean, r, 1);
				  table_ram.setValueAt(clean, r, 2);
			  }
			
		   for(int r = 0;r<list.size();r++) {
			   table_ram.setValueAt(list.get(r).getAddress(), r, 0);
			   table_ram.setValueAt(list.get(r).getSize(), r, 1);
			   if(list.get(r).isState())
			       table_ram.setValueAt("��ռ��  (�ձ�Ŀ)", r, 2);
			   else
				   table_ram.setValueAt("����  (δ��)", r, 2);
		   }
		}
	}
	
	//�������б����
	public void fillTable(List<PCB> list,int tablenum){	 
		 if(tablenum == 1) {
		  // �������
		  for(int r = 0;r<6;r++) {
			  String clean = "";
			  table_ready.setValueAt(clean, r, 0);
			  table_ready.setValueAt(clean, r, 1);
			  table_ready.setValueAt(clean, r, 2);
			  table_ready.setValueAt(clean, r, 3);
			  table_ready.setValueAt(clean, r, 4);
			  table_ready.setValueAt(clean, r, 5);
		  }
			  
		  for(int r = 0;r<list.size();r++){
			  String name = list.get(r).getName();
			  table_ready.setValueAt(name, r, 0);
			  int time = list.get(r).getTime();
			  table_ready.setValueAt(time, r, 1);
			  int pri = list.get(r).getPri();
			  table_ready.setValueAt(pri, r, 2);
			  String state = list.get(r).getState();
			  table_ready.setValueAt(state, r, 3);
			  int size = list.get(r).getSize();
			  table_ready.setValueAt(size, r, 4);
			  int address = list.get(r).getAddress();
			  table_ready.setValueAt(address, r, 5);
		   }
		 table_ready.invalidate();
		 }
		 if(tablenum == 2) {
			 // �������
			 for(int r = 0;r<16;r++) {
				  String clean = "";
				  table_wait.setValueAt(clean, r, 0);
				  table_wait.setValueAt(clean, r, 1);
				  table_wait.setValueAt(clean, r, 2);
				  table_wait.setValueAt(clean, r, 3);
				  table_wait.setValueAt(clean, r, 4);
			  }
			  for(int r = 0;r<list.size();r++){
				  String name = list.get(r).getName();
				  table_wait.setValueAt(name, r, 0);
				  int time = list.get(r).getTime();
				  table_wait.setValueAt(time, r, 1);
				  int pri = list.get(r).getPri();
				  table_wait.setValueAt(pri, r, 2);
				  String state = list.get(r).getState();
				  table_wait.setValueAt(state, r, 3);
				  int size = list.get(r).getSize();
				  table_wait.setValueAt(size, r, 4);
			  } 
			  table_wait.invalidate();
		 }
		 
		 if(tablenum == 3) {
			 // �������
			 for(int r = 0;r<16;r++) {
				  String clean = "";
				  table_up.setValueAt(clean, r, 0);
				  table_up.setValueAt(clean, r, 1);
				  table_up.setValueAt(clean, r, 2);
				  table_up.setValueAt(clean, r, 3);
				  table_up.setValueAt(clean, r, 4);
				  table_up.setValueAt(clean, r, 5);
			  }
			  for(int r = 0;r<list.size();r++){
				  String name = list.get(r).getName();
				  table_up.setValueAt(name, r, 0);
				  int time = list.get(r).getTime();
				  table_up.setValueAt(time, r, 1);
				  int pri = list.get(r).getPri();
				  table_up.setValueAt(pri, r, 2);
				  String state = list.get(r).getState();
				  table_up.setValueAt(state, r, 3);
				  int size = list.get(r).getSize();
				  table_up.setValueAt(size, r, 4);
				  int address = list.get(r).getAddress();
				  table_up.setValueAt(address, r, 5);
			  } 
		table_up.invalidate();
		}
	}
}




