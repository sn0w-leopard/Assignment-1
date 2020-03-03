/* 
CSC3002F
KHRTHU001 | SCHZEE001 | KGWKAG001
Networking Assignment

Client Side Services
Establishes connection to server, sends & recieve messages (seperate threads for each act)
*/

package src;

import java.io.*; 
import java.net.*; 
import java.util.Scanner; 

public class MyClient 
{ 
	final static int ServerPort = 1001; 

	public static void main(String args[]) throws UnknownHostException, IOException 
	{ 
		Scanner scn = new Scanner(System.in); 
		
		// getting localhost ip 
		InetAddress ip = InetAddress.getByName("localhost"); 
		
		// establish the connection 
		Socket s = new Socket(ip, ServerPort); 
		
		// obtaining input and out streams 
		DataInputStream inStream = new DataInputStream(s.getInputStream()); 
		DataOutputStream outStream = new DataOutputStream(s.getOutputStream()); 

		// sendMessage thread 
		Thread sendMessage = new Thread(new Runnable() 
		{ 
			@Override
			public void run() { 
				while (true) { 

					// read the message to deliver. 
					String msg = scn.nextLine(); 
					
					try { 
						// write on the output stream 
						outStream.writeUTF(msg); 
					} catch (IOException e) { 
						e.printStackTrace(); 
					} 
				} 
			} 
		}); 
		
		// readMessage thread 
		Thread readMessage = new Thread(new Runnable() 
		{ 
			@Override
			public void run() { 

				while (true) { 
					try { 
						// read the message sent to this client 
						String msg = inStream.readUTF(); 
						System.out.println(msg); 
					} catch (IOException e) { 

						e.printStackTrace(); 
					} 
				} 
			} 
		}); 

		sendMessage.start(); 
		readMessage.start(); 

	} 
} 





/* 
//ftp but wrong way and corrupts file
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.Socket;

public class MyClient {
  public static void main(String[] argv) throws Exception {
    // Creation of socket and input streams to recieve user input and to pass information between client and server 
    Socket sock = new Socket("127.0.0.1", 5000);
    byte[] mybytearray = new byte[1024];
    InputStream is = sock.getInputStream();
    FileOutputStream fos = new FileOutputStream("s.pdf");
    BufferedOutputStream bos = new BufferedOutputStream(fos);

	// establish a connection, recieve user input then send output to socket
    int bytesRead = is.read(mybytearray, 0, mybytearray.length);
    bos.write(mybytearray, 0, bytesRead);

    // closing of sockets & streams
    bos.close();
    sock.close();
  }
}
*/