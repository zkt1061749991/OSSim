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

//程序前台（界面显示和按钮功能）
public class Sim {
	private PSA psa = new PSA();  //建立后台程序
	private boolean flag = false;  //是否允许解挂标志
    private boolean stop = false;  //是否允许停止运行标志
    
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
    private JTable table_ram;  //内存分区表
    private JTable table_up;  //挂起队列表
    private JPanel [] showram = new JPanel[102];  //内存占用情况图
    private JTable table_ready = new JTable();  //就绪队列表
	private JTable table_wait = new JTable();  //后备队列表
    
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
		psa.init();  //初始化后台
		
		//可视化
		view();
	}
	
	//调度操作
	public void start() {
		
		if(psa.getList().size()>0)  
		    psa.run();  //运行进程
		
		//可视化
		runningname.setText(psa.getList().get(0).getName());
		runningstate.setText("运行");
		
		//进程结束操作
		if(psa.getList().get(0).getTime() == -1) {
			
			runningstate.setText("终止");
			
			psa.delram(psa.getList().get(0));  //撤销内存
			psa.getList().remove(0);  //移出就绪队列
			
			//向就绪队列添加进程
			if(!flag) {  //如果无需解挂，从后备队列调取
			    if(psa.getWaitlist().size()>0) {
			        psa.sort(psa.getWaitlist());  //后备队列按优先级排序
			        for(int i=0;i<psa.getWaitlist().size();i++) {  //遍历后备队列直到内存足够被分配
			            if(psa.addram(psa.getWaitlist().get(i))) {  
			                psa.getWaitlist().get(i).setState("就绪");
			                psa.getList().add(psa.getWaitlist().get(i));
			                psa.getWaitlist().remove(i);
			                break;  //结束遍历
			            }
			        }
			    }
			}
			if(flag) {  //如果需要解挂，从挂起队列调取
				if(psa.getUplist().size()>0) {  //进程加入就绪队列
				    psa.getUplist().get(0).setState("就绪");
				    psa.getList().add(psa.getUplist().get(0));
				    psa.getUplist().remove(0);
				    flag = false;  //解挂完成，标志关闭
				}
			}	
		}
		//一轮调度结束
		if(psa.getList().size()>0)
		    psa.getList().get(0).setState("就绪");
		//完成后排序
		psa.sort(psa.getList());
		//可视化
		view();
		}
	
	//可视化操作
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
		table_ready.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		
		
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
		table_wait.setFont(new Font("微软雅黑", Font.PLAIN, 16));
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
		lblCpu.setFont(new Font("微软雅黑", Font.PLAIN, 25));
		lblCpu.setBounds(641, 35, 167, 27);
		frame.getContentPane().add(lblCpu);
		
		runningname = new JLabel("NULL");
		runningname.setHorizontalAlignment(SwingConstants.CENTER);
		runningname.setFont(new Font("微软雅黑", Font.PLAIN, 19));
		runningname.setBounds(621, 84, 209, 27);
		frame.getContentPane().add(runningname);
		
		label_1 = new JLabel("\u72B6\u6001");
		label_1.setFont(new Font("微软雅黑", Font.PLAIN, 25));
		label_1.setBounds(700, 124, 51, 27);
		frame.getContentPane().add(label_1);
		
		runningstate = new JLabel("\u5F53\u524D\u65E0\u8FDB\u7A0B\u8FD0\u884C");
		runningstate.setHorizontalAlignment(SwingConstants.CENTER);
		runningstate.setFont(new Font("微软雅黑", Font.PLAIN, 19));
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
			//添加进程操作
			public void actionPerformed(ActionEvent e) {
				//获取要添加进程的名字、时间、优先级、内存
				int addtime = Integer.parseInt(textTime.getText());
				int addpri = Integer.parseInt(textPri.getText());
				int addsize = Integer.parseInt(textSize.getText());
				String addname = textName.getText();
				//添加进程
				PCB addp = new PCB(addname,addtime,addpri,"待添加",addsize);
				psa.add(addp);
				//可视化
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
				//挂起操作
				psa.getList().get(0).setState("挂起");
				psa.getUplist().add(psa.getList().get(0));
				psa.getList().remove(0);
				//从后备队列调取进程
				if(psa.getWaitlist().size()>0) {
					for(int i=0;i<psa.getWaitlist().size();i++) {  //遍历后备队列直到内存足够分配
						if(psa.addram(psa.getWaitlist().get(i))) {  //进程加入就绪队列
						psa.getWaitlist().get(i).setState("就绪");
						psa.getList().add(psa.getWaitlist().get(i));
						psa.getWaitlist().remove(i);
						break;  //结束遍历
						}
					}
				}
				//可视化
				view();
			}
		});
		button.setBounds(834, 111, 113, 27);
		frame.getContentPane().add(button);
		
		JButton button_1 = new JButton("\u89E3\u6302");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//解挂操作
				if(psa.getUplist().size() >0) {
					if(psa.getList().size()<6) {  //就绪队列未满，直接返回就绪队列
					    psa.getUplist().get(0).setState("就绪");
						psa.getList().add(psa.getUplist().get(0));
						psa.getUplist().remove(0);
					}
				    else {  //就绪队列已满，需要等待
				        flag = true;  //需要解挂标志打开，等待下次被调度
				        psa.getUplist().get(0).setState("阻塞");
				    }
				}
			}
		});
		button_1.setBounds(834, 164, 113, 27);
		frame.getContentPane().add(button_1);
		
		JButton buttonstart = new JButton("\u5F00\u59CB\u8FD0\u884C");
		buttonstart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                //运行操作
				new Thread(new Runnable() {
		            @Override
		            public void run() {
		                
		                while(psa.getList().size()>0 && !stop) {  //就绪队列不为空且停止标志为否，不断进行调度操作
		                	btnNewButton.doClick();
		                    try {
		                        Thread.sleep(300);  //调节自动调度的时间
		                    } catch (InterruptedException ex) {
		                        ex.printStackTrace();
		                    }
		                }
		                stop = false;  //停止运行，标志关闭
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
				runningstate.setText("等待");
			}
		});
		button_2.setBounds(503, 164, 113, 27);
		frame.getContentPane().add(button_2);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(503, 214, 447, 183);
		frame.getContentPane().add(scrollPane_2);
		
		table_up = new JTable();
		table_up.setFont(new Font("微软雅黑", Font.PLAIN, 16));
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
		table_ram.setFont(new Font("微软雅黑", Font.PLAIN, 13));
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
	
		//绘制数字刻度
		for(int i=0;i<20;i++) {
			int a = 0 + i*50;
			String num = String.valueOf(a);
		number[i] = new JLabel(num);
		number[i].setBounds(970, 50+30*i, 30, 15);
		frame.getContentPane().add(number[i]);
		}
		
		//绘制内存占用图
		for(int i=0;i<102;i++) {
		showram[i] = new JPanel();
		showram[i].setBounds(1000, 50+6*i, 150, 6);
		showram[i].setBackground(Color.WHITE);
		showram[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
		frame.getContentPane().add(showram[i]);
		}
		
	}
 
	//填充内存分区表和占用情况图操作
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
			       table_ram.setValueAt("已占用  (空表目)", r, 2);
			   else
				   table_ram.setValueAt("空闲  (未分)", r, 2);
		   }
		}
	}
	
	//填充各队列表操作
	public void fillTable(List<PCB> list,int tablenum){	 
		 if(tablenum == 1) {
		  // 填充数据
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
			 // 填充数据
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
			 // 填充数据
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




