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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Server class 
public class MyServer {

	// Active cClients Storage & Count
	Vector<ClientController> activeClients = new Vector<>();
	int count = 0;

	Vector<String> fileList = new Vector<>();

	Vector<ClientController> getClients() 
	{
		//returns vector of all active clients
        return activeClients;
	}
	

	boolean onlineStatus() 
		{
			//check to see if there are any active clients on server
			return !this.activeClients.isEmpty();
		}


	public static void main(String[] args) throws IOException {
		File myFile = new File("./files/test.html");

		ServerSocket sServer = new ServerSocket(6969);
		Socket s;

		System.out.println("...Server Online... ");

		// infinite loop to catch new clients
		while (true) {
			s = sServer.accept();
			System.out.println("...Incoming client request... ");
			System.out.println(s);

			// input and output streams
			DataInputStream inStream = new DataInputStream(s.getInputStream());
			DataOutputStream outStream = new DataOutputStream(s.getOutputStream());

			// Create a new handler object and corrosponding thread
			System.out.println("...Creating new handler, please wait...");
			ClientController cController = new ClientController(s, "client " + count, inStream, outStream);
			Thread t = new Thread(cController);

			// add this client to active clients list
			activeClients.add(cController);
			System.out.println("Client successfully added list");

			// Start thread and inc counter
			t.start();
			count++;
		}
	}
}

// ClientController class
// Thread class extention to be instantiated each time a request comes to allow
// multiple threads
// Breaks up message into "message" & "recipient" parts and handles accordingly
class ClientController implements Runnable {
	// Variable Declarations
	Scanner scn = new Scanner(System.in);
	private String name;
	final DataInputStream inStream;
	final DataOutputStream outStream;
	Socket s;
	boolean isloggedin;

	// Constructor
	public ClientController(Socket s, String name, DataInputStream inStream, DataOutputStream outStream) {
		this.inStream = inStream;
		this.outStream = outStream;
		this.name = name;
		this.s = s;
		this.isloggedin = true;
	}

	String getList(String dest) {
		// returns list of files in selected dir
		try (Stream<Path> walk = Files.walk(Paths.get(dest))) {

			List<String> result = walk.filter(Files::isRegularFile).map(x -> x.toString()).collect(Collectors.toList());

			result.forEach(System.out::println);
			return result.toString();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return "404";
	}

	void currentUsers() {
		if (MyServer.onlineStatus())
			{

			}
	}


	void downloadFile(Socket s, String file)  throws IOException
	{
		//User request to download file
		File myFile = new File ("../src/files/test.html");	

		//Download from server
		while (true)
		{

			BufferedInputStream fileIS = new BufferedInputStream(new FileInputStream(myFile));
			byte b[] = new byte[(int) myFile.length()];
			fileIS.read(b, 0, b.length);

			OutputStream fileOS = s.getOutputStream();

			fileOS.write(b, 0, b.length);
			fileOS.flush();
			System.out.println(" download complete: " + b.length);

			//fileOS.close();
		}

	}	
	
/* 	void uploadFile(Socket s, String file)  throws IOException
	{
		//File transfer streams
		File myFile = new File (file);

		BufferedInputStream fileIS = new BufferedInputStream(new FileInputStream(myFile));
		byte b[] = new byte[(int) myFile.length()];
		fileIS.read(b, 0, b.length);

		OutputStream fileOS = s.getOutputStream();

		fileOS.write(b, 0, b.length);
		fileOS.flush();
		System.out.println("upload complete: " + b.length);

		fileOS.close();
	}  */
	
	@Override
	public void run() { 

		//recieving and parsing of string input
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
					//System.out.println("test if reaches this"); // trace statement to see if client commands are reaching server side
					this.isloggedin=false; 
					this.s.close(); 
					break; 
				}

				//File Query
				else if(sReceived.equals("!files"))
				{ 
					String temp = getList("../src/files");
					this.outStream.writeUTF(temp);
					break; 
				}
				
				// File Download
				else if(sReceived.equals("!down"))
				{ 
					downloadFile(s, "doesnt matter atm");
					break; 
				}


				
				// String Parsing (seperating message and recipient)
				else if (sReceived.contains("#")) {
					StringTokenizer sToken = new StringTokenizer(sReceived, "#");
					String MsgToSend = sToken.nextToken();
					String recipient = sToken.nextToken();

					// search for the recipient in the connected devices list. If found, send
					// message
					for (ClientController tempClient : MyServer.activeClients) {
						if (tempClient.name.equals(recipient) && tempClient.isloggedin == true) {
							tempClient.outStream.writeUTF(this.name + " : " + MsgToSend);
							break;
						}
						else
						{
							this.outStream.writeUTF(recipient + " is not online, try again");
							break;
						}
					}
				}
				else {
					this.outStream.writeUTF("Please Enter Valid Input");
					break;
					//String recipient = sToken.nextToken();

					//search for the recipient in the connected devices list. If found, send message
					//for (ClientController tempClient : MyServer.activeClients) {
					//	if (tempClient.name.equals(recipient) && tempClient.isloggedin == true) {
							//tempClient.outStream.writeUTF(this.name + " : " + MsgToSend);
							//break;
					//	}
					//}
				}
			} catch (IOException e) { 
				
				e.printStackTrace(); 
			} 
			
		} 
		try
		{ 
			// closing data leaks
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