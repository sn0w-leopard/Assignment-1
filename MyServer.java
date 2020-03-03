/* 
CSC3002F
KHRTHU001 | SCHZEE001 | KGWKAG001
Networking Assignment

Server Side Control
Server runs an infinite loop to accept incoming requests assigning a new thread to handle the communication part for each.
Server also stores client name and corresponding thread object to keep track of connected devices.
 */

import java.io.*; 
import java.util.*; 
import java.net.*; 

// Server class 
public class MyServer 
{ 

	// Active clients Storage & Count
	static Vector<ClientController> activeClients = new Vector<>(); 
	static int count = 0; 

	public static void main(String[] args) throws IOException 
	{ 
		
		ServerSocket sServer = new ServerSocket(1001); 		
		Socket s; 
		
		// infinite loop to catch new clients 
		while (true) 
		{ 
	
			s = sServer.accept();
			System.out.println("...Incoming client request... "); 
			System.out.println(s); 
			
			//input and output streams 
			DataInputStream inStream = new DataInputStream(s.getInputStream()); 
			DataOutputStream outStream = new DataOutputStream(s.getOutputStream());
			
			
			// Create a new handler object and corrosponding thread			
			System.out.println("...Creating new handler, please wait..."); 
			ClientController cController = new ClientController(s,"client " + count, inStream, outStream); 
			Thread t = new Thread(cController);
						

			// add this client to active clients list
			activeClients.add(cController);  			
			System.out.println("Client successfully added list"); 


			//Start thread and inc counter
			t.start(); 
			count++; 

		} 
	} 
} 

// ClientController class 
// Thread class extention to be instantiated each time a request comes to allow multiple threads
// Breaks up message into "message" & "recipient" parts and handles accordingly
class ClientController implements Runnable 
{
	//Variable Declarations
	Scanner scn = new Scanner(System.in); 
	private String name; 
	final DataInputStream inStream; 
	final DataOutputStream outStream; 
	Socket s; 
	boolean isloggedin; 
	
	public ClientController(Socket s, String name, DataInputStream inStream, DataOutputStream outStream) 
	{ 
		this.inStream = inStream; 
		this.outStream = outStream; 
		this.name = name; 
		this.s = s; 
		this.isloggedin=true; 
	} 

	@Override
	public void run() { 

		//recieving and parsing of string, loging out if "!exit" command recieved
		String sReceived; 
		while (true) 
		{ 
			try
			{ 
	
				sReceived = inStream.readUTF(); 				
				System.out.println(sReceived); 
				
				//Logout
				if(sReceived.equals("!exit"))
				{ 
					this.isloggedin=false; 
					this.s.close(); 
					break; 
				} 
				
				// String Parsing (seperating message and recipient)
				StringTokenizer sToken = new StringTokenizer(sReceived, "#"); 
				String MsgToSend = sToken.nextToken(); 
				String recipient = sToken.nextToken(); 

				// search for the recipient in the connected devices list. If found, send message 
				for (ClientController tempClient : MyServer.activeClients) 
				{ 
					if (tempClient.name.equals(recipient) && tempClient.isloggedin==true) 
					{ 
						tempClient.outStream.writeUTF(this.name+" : "+MsgToSend); 
						break; 
					} 
				} 
			} catch (IOException e) { 
				
				e.printStackTrace(); 
			} 
			
		} 
		try
		{ 
			// leaky taps
			this.inStream.close(); 
			this.outStream.close(); 
			
		}catch(IOException e){ 
			e.printStackTrace(); 
		} 
	} 
} 





/* 
//ftp but wrong way and corrupts file
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {
  public static void main(String[] args) throws IOException {
    ServerSocket servsock = new ServerSocket(5000);
    File myFile = new File("s.pdf");
    while (true) {
      Socket sock = servsock.accept();
      byte[] mybytearray = new byte[(int) myFile.length()];
      BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));
      bis.read(mybytearray, 0, mybytearray.length);
      OutputStream os = sock.getOutputStream();
      os.write(mybytearray, 0, mybytearray.length);
      os.flush();
      sock.close();
    }
  }
}
*/