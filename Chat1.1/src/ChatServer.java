import java.awt.List;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class ChatServer {
	
	ArrayList<Client> clients=new ArrayList<Client>();
	ServerSocket ss = null;
	boolean started = false;
	Client c=null;

	public static void main(String[] args) {
		new ChatServer().start();
	}
	
	public void start(){
		try {
			ss = new ServerSocket(8889);
			started = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			
			while (started) {
				Socket s = ss.accept();
				c = new Client(s);
				System.out.println("a client connected!");
				new Thread(c).start();
				clients.add(c);

				// dis.close();

			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				ss.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public class Client implements Runnable {
		private Socket s;
		private DataInputStream dis = null;
		private DataOutputStream dos = null;
		private boolean bconnect = false;

		public Client(Socket s) {
			this.s = s;
			try {
				dis = new DataInputStream(s.getInputStream());
				dos = new DataOutputStream(s.getOutputStream());
				bconnect = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		public void send(String str){
			try {
				dos.writeUTF(str);
			} catch (IOException e) {
				clients.remove(this); 
			}
		}
		

		@Override
		public void run() {

			try {
				while (bconnect) {
					String str = dis.readUTF();
System.out.println(str);
					for(int i=0; i<clients.size(); i++){
						Client c=clients.get(i);
						c.send(str);
					}
				}
			}catch(SocketException e){
				System.out.println("a client quit!");
			}catch (Exception e) {

				System.out.println("Client disconnected!");
			} finally {
				try {
					if (s != null)
						s.close();
					if(dos != null)
						dos.close();
					if (dis != null)
						dis.close();
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}

		}

	}
}
