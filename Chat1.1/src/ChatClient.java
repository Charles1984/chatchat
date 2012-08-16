import java.awt.BorderLayout;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;


public class ChatClient extends JFrame{
	
	DataOutputStream dos;
	DataInputStream dis;
	Socket s;
	TextField tfTxt=new TextField();
	TextArea taContent=new TextArea();
	private boolean bconnected=false;
	
	public static void main(String[] args){
		
		new ChatClient().launchFrame();
		
	}
	
	public void launchFrame(){
		this.setLocation(400, 400);
		this.setSize(400, 300);
		add(tfTxt, BorderLayout.SOUTH);
		add(taContent, BorderLayout.NORTH);
		pack();
		tfTxt.addActionListener(new TFListener());
		this.addWindowListener(new WindowAdapter(){

			@Override
			public void windowClosing(WindowEvent e) {
				
				close();
				System.exit(0);
			}
			
		});
		this.setVisible(true);
		connect();
		new Thread(new RecvThread()).start();
	}
	
	public void connect(){
		try {
			s=new Socket("127.0.0.1", 8889);
			dos=new DataOutputStream(s.getOutputStream());
			dis=new DataInputStream(s.getInputStream());
System.out.println("Connected!");
			bconnected=true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close(){
		
		try {
			s.close();
			dos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private class TFListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String str= tfTxt.getText().trim();
			//taContent.setText(str);
			tfTxt.setText("");
			try {
				dos.writeUTF(str);
				dos.flush();
				//dos.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		}
		
	}

	
	private class RecvThread implements Runnable{

		@Override
		public void run() {
			try {
				while(bconnected){
					String str= dis.readUTF();
					taContent.setText(taContent.getText() + str + '\n');
				}
			}catch (IOException e) {
					System.out.println("quit");
					
			}
			}
			
		}
		
	}

