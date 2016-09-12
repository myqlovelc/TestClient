package com.example.testclient;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.example.message.MessageHead;
import com.example.message.MessageSender;
import com.example.message.MessageType;
import com.example.message.MsgAlive;
import com.example.message.MsgCmdPause;
import com.example.message.MsgCmdPlayByName;
import com.example.message.MsgCmdRegister;
import com.example.message.MsgCmdResume;
import com.example.message.MsgCmdSeek;
import com.example.message.MsgCmdStop;
import com.example.message.MsgPauseRequestResponse;
import com.example.message.MsgPlayRequestResponse;
import com.example.message.MsgRequestLaunchPlayerFromService;
import com.example.message.MsgRequestShutdownPlayer;
import com.example.message.MsgResumeRequestResponse;
import com.example.message.MsgSeekRequestResponse;
import com.example.message.MsgStatusPaused;
import com.example.message.MsgStatusPlaying;
import com.example.message.MsgStopRequestResponse;
import com.example.utils.ControllerConfig;
import com.example.utils.Strings;


public class Client extends JFrame{
	
	private List<VideoInfo> video_list = null;
	
    private InetAddress addr = null;
    
	private ControllerConfig mController = null;
	
	private String deviceId = "";
//private String deviceId = "355848060355628";
	
	private int value = 0;
	
	private static long duration = 0;
	
	private String goal_video = "";
	
	private String default_IP = "127.0.0.1";

	// The list for selecting countries
	@SuppressWarnings("rawtypes")
	private JList jlst = new JList();
	
	private JButton register_btn = new JButton("Register");
	private JButton clear_btn = new JButton("Clear");
	
	private JScrollPane file_list = new JScrollPane(jlst);
	private JPanel file_list_panel = new JPanel(new BorderLayout());
	
	private DatagramSocket clientSocket = null;
	private DatagramSocket serverSocket = null;
	
	private JPanel input_panel = new JPanel();
	//private JTextField ip_input = new JTextField(15);
	//private JTextField port_input = new JTextField(10);
	
	private JTextField position_input = new JTextField(15);
	private JButton seek_btn = new JButton("Seek");
	//private JTextField name_input = new JTextField(15);
	//private JTextField package_input = new JTextField(15);
	private JButton connect_btn = new JButton(Strings.OPEN_SOCKET);
	private JButton disconnect_btn = new JButton(Strings.CLOSE_SOCKET);
	private JButton run_btn = new JButton(Strings.RUN_APP);
	private JButton close_btn = new JButton(Strings.CLOSE_APP);
	private JButton play_btn = new JButton(Strings.PLAY_VIDEO);
	private JButton stop_btn = new JButton(Strings.STOP_VIDEO);
	private JButton pause_btn = new JButton(Strings.PAUSE_VIDEO);
	private JButton resume_btn = new JButton(Strings.RESUME_VIDEO);
	private JTextArea text_area = new JTextArea("", 10, 20);
	private JScrollPane scroll_pane = new JScrollPane(text_area);
	
	private JPanel status_panel = new JPanel();
	private JTextArea status_area = new JTextArea("", 10, 20);
	private JScrollPane status_pane = new JScrollPane(status_area);
	
	private JButton get_video_btn = new JButton(Strings.GET_VIDEOS);
	
	//private static Lock lock = new ReentrantLock();
	
	//private String app_name = Strings.DEFALT_APP_NAME;
	//private String package_name = Strings.DEFALT_PACKAGE_NAME;
	
	private int player_service_port = Strings.PLAYER_SERVICE_PORT;
	private int player_port = Strings.PLAYER_PORT;
	
	//private int status_port = Strings.STATUS_PORT;
	//private int app_port = Strings.APP_PORT;
	//private int command_port = Strings.COMMAND_PORT;
	
	private JSlider slider = new JSlider(0,100);
	
	private static int communicationMode = 1;
	
	public static int getCommunicationMode() {
		return communicationMode;
	}
	
	public static void main(String[] args) {
		
		Client client = new Client();
		client.setTitle("TestClient");
		client.setSize(600, 900);
		client.setLocationRelativeTo(null);
		client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.setVisible(true);

	}
	
	@SuppressWarnings("unchecked")
	public Client() {
		
		try {
			//addr = InetAddress.getByName(Strings.DEFALT_IP);

			video_list = new ArrayList<VideoInfo>();
			File config = new File("./config.xml");
			
			if (config.exists()) {
				SAXBuilder builder = new SAXBuilder();
				InputStream file = new FileInputStream(config);
				Document document = builder.build(file);//获得文档对象
				Element root = document.getRootElement();//获得根节点
				Element child_IP = root.getChild("IP");
				Element child_device = root.getChild("Device");
				addr = InetAddress.getByName(child_IP.getText());
				deviceId = child_device.getText();
				
				List<Element> video_nodes = root.getChild("VideoList").getChildren("Video");
				
				for (int i = 0; i < video_nodes.size(); i++) {
					video_list.add(new VideoInfo(video_nodes.get(i).getChild("Name").getText(), 
							video_nodes.get(i).getChild("Code").getText()));
				}
			}
			else {
				addr = InetAddress.getByName(default_IP);
				deviceId = "";
			}
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JDOMException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		
		mController = new ControllerConfig();
		mController.id = 0;
		mController.alive_port = Strings.APP_PORT;
		mController.status_port = Strings.STATUS_PORT;
		mController.cmd_port = Strings.COMMAND_PORT;
		
		input_panel.setLayout(new GridLayout(3,4,10,5));
		input_panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		input_panel.add(register_btn);
		//ip_input.setText(Strings.DEFALT_IP);
		input_panel.add(clear_btn);
		
		input_panel.add(position_input);
		position_input.setText("3");
		input_panel.add(seek_btn);
		
		//input_panel.add(new JLabel(""));
		//input_panel.add(new JLabel(""));
		
		input_panel.add(connect_btn);
		input_panel.add(disconnect_btn);
		input_panel.add(run_btn);
		input_panel.add(close_btn);
		
		input_panel.add(play_btn);
		input_panel.add(stop_btn);
		input_panel.add(pause_btn);
		input_panel.add(resume_btn);
		
		text_area.setBorder(new EmptyBorder(10, 10, 10, 10));
		text_area.setLineWrap(true);
		text_area.setEditable(false);
		
		status_area.setBorder(new EmptyBorder(10, 10, 10, 10));
		status_area.setLineWrap(true);
		status_area.setEditable(false);
		
		//JScrollPane scroll_pane = new JScrollPane(text_area);
		//file_list_panel.setSize(100, 150);
		file_list_panel.setBorder(new EmptyBorder(0, 5, 0, 10));
		file_list_panel.setPreferredSize(new Dimension(180, 100));
		file_list_panel.add(get_video_btn, BorderLayout.NORTH);
		file_list_panel.add(file_list, BorderLayout.CENTER);
		
		slider.setPaintTicks(true);
		slider.setPaintTrack(true); 
		slider.setMajorTickSpacing(10);
		slider.setMinorTickSpacing(5);
		slider.setValue(0);
		status_panel.setLayout(new GridLayout(2,1,10,5));
		status_panel.add(status_pane);
		status_panel.add(slider);
		
		//this.add(left_panel, BorderLayout.CENTER);
		this.add(input_panel, BorderLayout.NORTH);
		this.add(scroll_pane, BorderLayout.CENTER);
		this.add(file_list_panel, BorderLayout.EAST);
		this.add(status_panel, BorderLayout.SOUTH);

		register_btn.addActionListener(new RegisterListener());
		clear_btn.addActionListener(new ClearListener());
		disconnect_btn.addActionListener(new DisconnectListener());
		connect_btn.addActionListener(new ConnectListener());
		run_btn.addActionListener(new RunListener());
		close_btn.addActionListener(new CloseListener());
		play_btn.addActionListener(new PlayListener());
		stop_btn.addActionListener(new StopListener());
		pause_btn.addActionListener(new PauseListener());
		resume_btn.addActionListener(new ResumeListener());
		get_video_btn.addActionListener(new GetVideoListener());
		
		seek_btn.addActionListener(new SeekVideoListener());
		
		String flagTitles[] = new String[video_list.size()];
		for (int i = 0; i < video_list.size(); i++) {
			flagTitles[i] = video_list.get(i).getName();
		}
		jlst.setListData(flagTitles);
		jlst.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		jlst.addListSelectionListener(new ListSelectionListener() {
			/** Handle list selection */
			public void valueChanged(ListSelectionEvent e) {
				// Get selected indices
				//int index = jlst.getSelectedIndex();
				String str = (String)jlst.getSelectedValue();
				
				//text_area.append(str);
				goal_video = str;
				//System.out.println(goal_video);
			}
	    });
		
		/*slider.addChangeListener(new ChangeListener()
        {
            public void stateChanged(ChangeEvent e)
            {
            	new Thread(new SeekVideo()).start();
            }
        });*/
		
		new Thread(new GetAppStatus()).start();
		new Thread(new AppAliveStatus()).start();
		new Thread(new CommandResponse()).start();
	}
	
	public void addTextToPane(String text) {
		
		text_area.append(text);
		
		JScrollBar scroll_bar = scroll_pane.getVerticalScrollBar();
		scroll_bar.setValue(scroll_bar.getMaximum());
	}
	
	public void addTextToStatusPane(String text) {
		
		status_area.append(text);
		
		JScrollBar scroll_bar = status_pane.getVerticalScrollBar();
		scroll_bar.setValue(scroll_bar.getMaximum());
	}
	
	class RegisterListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			
			new Thread(new RegisterController()).start();
		}
	}
	
	class ClearListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			
			status_area.setText("");
		}
	}
	
	class ConnectListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			  
			try {
				
				clientSocket = new DatagramSocket();
				
				//group = InetAddress.getByName(Strings.MULTICAST_HOST);
				//clientSocket.joinGroup(group);
				
				addTextToPane("Socket Opened!\n");
			} catch (SocketException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				addTextToPane("Fail to open a socket!\n");
			} 
			//new Thread(new Connect()).start();
	
		}
	}
	
	class DisconnectListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			
			clientSocket.close();
			clientSocket = null;
			
			addTextToPane("Socket Closed!\n");
		}
	}
	
	class RunListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			
			new Thread(new RunApp()).start();
			
		}
	}
	
	class CloseListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			
			new Thread(new CloseApp()).start();
		}
	}
	
	class PlayListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			
			if (goal_video == null || goal_video.equals("")) {
				JOptionPane.showMessageDialog(null, "Please choose a video!","Message", JOptionPane.INFORMATION_MESSAGE); 
			}
			
			else {
				new Thread(new PlayVideo()).start();
			}
			
		}
	}
	
	class StopListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			
			new Thread(new StopVideo()).start();
		}
	}
	
	class PauseListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			
			new Thread(new PauseVideo()).start();
			
		}
	}
	
	class ResumeListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			
			new Thread(new ResumeVideo()).start();
		}
	}
	
	class GetVideoListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			
			//new Thread(new GetVideoList()).start();
		}
	}
	
	class SeekVideoListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			new Thread(new StaticSeekVideo()).start();
		}
	}
	
	class RegisterController implements Runnable {
		
		public void run() { 
			if (clientSocket == null) {
				addTextToPane("No socket opened\n");
			}
			
			else {
				
				byte[] sendBuf = new byte[Strings.BUFFER_LENGTH];
				MsgCmdRegister msg = new MsgCmdRegister((long)1, mController, (int)0);
				
				int length = msg.serialize(sendBuf);
				
				MessageSender.sendMessage(clientSocket, sendBuf, length, 
						addr, player_port);
			}
		}
	}
	
	class RunApp implements Runnable {
		
		public void run() { 
			if (clientSocket == null) {
				addTextToPane("No socket opened\n");
			}
			
			else {
				
				byte[] sendBuf = new byte[Strings.BUFFER_LENGTH];
				MsgRequestLaunchPlayerFromService msg = new MsgRequestLaunchPlayerFromService((long)1, (int)0, "myqTmac", Strings.COMMAND_PORT);
				
				int length = msg.serialize(sendBuf);
				
				//String test = "myqTmac";
				
				MessageSender.sendMessage(clientSocket, sendBuf, length, 
						addr, player_service_port);
				
				/*byte[] sendBuf = new byte[Strings.BUFFER_LENGTH];
				MsgRequestPingService msg = new MsgRequestPingService((long)1, (int)0, Strings.COMMAND_PORT);
				
				int length = msg.serialize(sendBuf);
				
				MessageSender.sendMessage(clientSocket, sendBuf, length, 
						addr, player_service_port);*/
				
				//MessageSender.sendMessage(clientSocket, test.getBytes(), test.getBytes().length, 
				//				addr, player_service_port);
			}
		}
	}
	
	class CloseApp implements Runnable {
		
		public void run() { 
			
			if (clientSocket == null) {
				addTextToPane("No socket opened\n");
			}
			
			else {
				
				byte[] sendBuf = new byte[Strings.BUFFER_LENGTH];
				MsgRequestShutdownPlayer msg = new MsgRequestShutdownPlayer((long)1, (int)0, Strings.COMMAND_PORT);
				int length = msg.serialize(sendBuf);
				
				MessageSender.sendMessage(clientSocket, sendBuf, length, 
						addr, player_service_port);
			}
		}
	}
	
	class PlayVideo implements Runnable {
		
		public void run() { 
			
			if (clientSocket == null) {
				addTextToPane("No socket opened\n");
			}
			
			else {
				
				String hash_code = "";
				for (int i = 0; i < video_list.size(); i++) {
					if (video_list.get(i).getName().equals(goal_video)) {
						hash_code = video_list.get(i).getCode();
						break;
					}
				}
				
				byte[] sendBuf = new byte[Strings.BUFFER_LENGTH];
				MsgCmdPlayByName msg = new MsgCmdPlayByName((long)1, (int)0, goal_video, hash_code.toUpperCase(), deviceId, 0, 0);
				int length = msg.serialize(sendBuf);
				
				System.out.println(length);
				
				MessageSender.sendMessage(clientSocket, sendBuf, length, 
						addr, player_port);
			}
		}
	}

	class StopVideo implements Runnable {
	
		public void run() { 
			
			if (clientSocket == null) {
				addTextToPane("No socket opened\n");
			}
			
			else {
				
				byte[] sendBuf = new byte[Strings.BUFFER_LENGTH];
				MsgCmdStop msg = new MsgCmdStop((long)1, (int)0, 0);
				int length = msg.serialize(sendBuf);
				
				MessageSender.sendMessage(clientSocket, sendBuf, length, 
						addr, player_port);
				
				slider.setValue(0);
			}
		}
	}
	
	class PauseVideo implements Runnable {
		
		public void run() { 
			
			if (clientSocket == null) {
				addTextToPane("No socket opened\n");
			}
			
			else {
				
				byte[] sendBuf = new byte[Strings.BUFFER_LENGTH];
				MsgCmdPause msg = new MsgCmdPause((long)1, (int)0);
				int length = msg.serialize(sendBuf);
				
				MessageSender.sendMessage(clientSocket, sendBuf, length, 
						addr, player_port);
			}
		}
	}
	
	class ResumeVideo implements Runnable {
		
		public void run() { 
			
			if (clientSocket == null) {
				addTextToPane("No socket opened\n");
			}
			
			else {
				
				byte[] sendBuf = new byte[Strings.BUFFER_LENGTH];
				MsgCmdResume msg = new MsgCmdResume((long)1, (int)0);
				int length = msg.serialize(sendBuf);
				
				MessageSender.sendMessage(clientSocket, sendBuf, length, 
						addr, player_port);
			}
		}
	}
	
	class SeekVideo implements Runnable {
		
		public void run() { 
			
			if (clientSocket == null) {
				addTextToPane("No socket opened\n");
			}
			
			else {
				
				if (Math.abs(slider.getValue() - value) > 5) {
					long new_position = (long) (((long)(slider.getValue()) * duration) / 100);
				    
				    byte[] sendBuf = new byte[Strings.BUFFER_LENGTH];
				    MsgCmdSeek msg = new MsgCmdSeek((long)1, (int)0, new_position);
				    int length = msg.serialize(sendBuf);
				    
				    MessageSender.sendMessage(clientSocket, sendBuf, length, 
				    		addr, player_port);
				}
			}
		}
	}
	
	class StaticSeekVideo implements Runnable {
		
		public void run() { 
			
			if (clientSocket == null) {
				addTextToPane("No socket opened\n");
			}
			
			else {
				
				long new_position = Long.parseLong(position_input.getText());
				
				byte[] sendBuf = new byte[Strings.BUFFER_LENGTH];
				MsgCmdSeek msg = new MsgCmdSeek((long)1, (int)0, new_position);
				int length = msg.serialize(sendBuf);
				
				MessageSender.sendMessage(clientSocket, sendBuf, length, 
						addr, player_port);
			}
		}
	}
	
	class GetAppStatus implements Runnable {
		
		public void run() { 
			
			try {
				serverSocket  = new DatagramSocket(mController.status_port);
				
				byte[] recvBuf = new byte[Strings.BUFFER_LENGTH];
		        DatagramPacket recvPacket = new DatagramPacket(recvBuf , recvBuf.length);
	            
				while (true) {
					
					serverSocket.receive(recvPacket);
					
					try {
						
						byte[] data = MessageSender.receiveMessage(recvPacket);
						
						MessageHead head = com.example.message.Message.parseHead(data, data.length);
						
						if (head.type == MessageType.STATUS_Home) {
							
							addTextToStatusPane("Home\n");
							slider.setValue(0);
						}
						
						else if (head.type == MessageType.STATUS_LoadingVideo) {
							
							addTextToStatusPane("Loading\n");
							slider.setValue(0);
						}
						
						else if (head.type == MessageType.STATUS_Playing) {
							
					        MsgStatusPlaying msg = new MsgStatusPlaying();
					        msg.deserialize(data, data.length);
					        String video_name = msg.video_name;
					        long position = msg.current_frame;
					        duration = msg.total_frame;
							addTextToStatusPane("Playing " + video_name + " " + position + " " + duration + "\n");
							float ratio = ((float)position) / ((float)duration);
							value = (int)(100 * ratio);
							slider.setValue(value);
						}
						
						else if (head.type == MessageType.STATUS_Paused) {
							
							MsgStatusPaused msg = new MsgStatusPaused();
					        msg.deserialize(data, data.length);
					        String video_name = msg.video_name;
					        long position = msg.current_frame;
					        duration = msg.total_frame;
							addTextToStatusPane("Paused " + video_name + " " + position + " " + duration + "\n");
							float ratio = ((float)position) / ((float)duration);
							value = (int)(100 * ratio);
							slider.setValue(value);
						}
						
						else {
							slider.setValue(0);
						}
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					
				}
					
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	class AppAliveStatus implements Runnable {
		
		public void run() { 
			
			try {
				@SuppressWarnings("resource")
				DatagramSocket aliveSocket  = new DatagramSocket(mController.alive_port);
				
				byte[] recvBuf = new byte[Strings.BUFFER_LENGTH];
		        DatagramPacket recvPacket = new DatagramPacket(recvBuf , recvBuf.length);
	            
				while (true) {
					
					aliveSocket.receive(recvPacket);
					
					try {

						byte[] data = MessageSender.receiveMessage(recvPacket);
						
						if (data == null) {
							continue;
						}
						
						MessageHead head = com.example.message.Message.parseHead(data, data.length);
						if (head.type == MessageType.Alive) {
							MsgAlive msg = new MsgAlive();
					        msg.deserialize(data, data.length);
						}
						
						
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					
				}
					
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	class CommandResponse implements Runnable {
		
		public void run() { 
			
			try {
				@SuppressWarnings("resource")
				DatagramSocket responseSocket  = new DatagramSocket(mController.cmd_port);
				
				byte[] recvBuf = new byte[Strings.BUFFER_LENGTH];
		        DatagramPacket recvPacket = new DatagramPacket(recvBuf , recvBuf.length);
	            
				while (true) {
					
					responseSocket.receive(recvPacket);
					System.out.println("myqTmac");
					try {
						
						byte[] data = MessageSender.receiveMessage(recvPacket);
						
						if (data == null) {
							continue;
						}
						
						//System.out.println(data[1023]);
						/*for (int i = 0; i < 1024; i++) {
							if (i == 512) {
								System.out.println();
							}
							System.out.print(data[i] + " ");
						}
						System.out.println();*/
						
						MessageHead head = com.example.message.Message.parseHead(data, data.length);
						System.out.println(head.type);
						
						if (head.type == MessageType.ACK_PingService_OK) {
							addTextToPane("Ping service success\n");
						}
						
						else if (head.type == MessageType.ACK_Launch_OK) {
							addTextToPane("Launch success\n");
						}
						
						else if (head.type == MessageType.ACK_Shutdown_OK) {
							addTextToPane("Shutdown success\n");
						}
						
						else if (head.type == MessageType.ACK_Register_OK) {
							addTextToPane("Register success\n");
						}
						
						else if (head.type == MessageType.ACK_Play_OK) {
							
							MsgPlayRequestResponse msg = new MsgPlayRequestResponse();
							msg.deserialize(data, data.length);

							System.out.println(msg.status);
							
							if (msg.status == MsgPlayRequestResponse.OK) {
								addTextToPane("Play success\n");
							}
							
							else if (msg.status == MsgPlayRequestResponse.FileNotFound) {
								addTextToPane("File not found\n");
							}
							
							else if (msg.status == MsgPlayRequestResponse.DeviceNotAuthorized) {
								addTextToPane("Device Not authorized\n");
							}
							
							else if (msg.status == MsgPlayRequestResponse.VideoNotAuthorized) {
								addTextToPane("Video Not authorized\n");
							}
							
							else if (msg.status == MsgPlayRequestResponse.BothNotAuthorized) {
								addTextToPane("Both Device And Video Not authorized\n");
							}
							
							else if (msg.status == MsgPlayRequestResponse.HeadsetUnmounted) {
								addTextToPane("Headset unmounted\n");
							}
							
							else if (msg.status == MsgPlayRequestResponse.InvalidVideoSuffix) {
								addTextToPane("Invalid video suffix\n");
							}
							
							else if (msg.status == MsgPlayRequestResponse.CommandBuffered) {
								addTextToPane("Command buffered\n");
							}
							
							else if (msg.status == MsgPlayRequestResponse.InvalidVideoData) {
								addTextToPane("Invalid video data\n");
							}
							
							else if (msg.status == MsgPlayRequestResponse.VideoLoading) {
								addTextToPane("Video is loading now\n");
							}
						}
						
						else if (head.type == MessageType.ACK_Stop_OK) {
							
							MsgStopRequestResponse msg = new MsgStopRequestResponse();
							msg.deserialize(data, data.length);
							
							if (msg.status == MsgStopRequestResponse.OK) {
								addTextToPane("Stop success\n");
							}
							
							else if (msg.status == MsgStopRequestResponse.HeadsetUnmounted) {
								addTextToPane("Headset unmounted\n");
							}
							
							else if (msg.status == MsgStopRequestResponse.CommandIgnored) {
								addTextToPane("Command ignored\n");
							}
						}
						
						else if (head.type == MessageType.ACK_Pause_OK) {
							
							MsgPauseRequestResponse msg = new MsgPauseRequestResponse();
							msg.deserialize(data, data.length);
							
							if (msg.status == MsgPauseRequestResponse.OK) {
								addTextToPane("Pause success\n");
							}
							
							else if (msg.status == MsgPauseRequestResponse.HeadsetUnmounted) {
								addTextToPane("Headset unmounted\n");
							}
							
							else if (msg.status == MsgPauseRequestResponse.CommandIgnored) {
								addTextToPane("Command ignored\n");
							}
						}
						
						else if (head.type == MessageType.ACK_Resume_OK) {
							
							MsgResumeRequestResponse msg = new MsgResumeRequestResponse();
							msg.deserialize(data, data.length);
							
							if (msg.status == MsgResumeRequestResponse.OK) {
								addTextToPane("Resume success\n");
							}
							
							else if (msg.status == MsgResumeRequestResponse.HeadsetUnmounted) {
								addTextToPane("Headset unmounted\n");
							}
							
							else if (msg.status == MsgResumeRequestResponse.CommandIgnored) {
								addTextToPane("Command ignored\n");
							}
						}
						
						else if (head.type == MessageType.ACK_Seek_OK) {
							
							MsgSeekRequestResponse msg = new MsgSeekRequestResponse();
							msg.deserialize(data, data.length);
							
							if (msg.status == MsgSeekRequestResponse.OK) {
								addTextToPane("Seek success\n");
							}
							
							else if (msg.status == MsgSeekRequestResponse.HeadsetUnmounted) {
								addTextToPane("Headset unmounted\n");
							}
							
							else if (msg.status == MsgSeekRequestResponse.CommandIgnored) {
								addTextToPane("Command ignored\n");
							}
						}
						
						else {
							addTextToPane("Invalid response\n");
						}
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}
					
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
