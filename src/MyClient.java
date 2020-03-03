/* 
CSC3002F
KHRTHU001 | SCHZEE001 | KGWKAG001
Networking Assignment

Client Side Services
Establishes connection to server, sends & recieve messages (seperate threads for each act)
*/

import java.io.*;
import java.net.*;
import java.util.Scanner;


public class MyClient 
{ 
	final static int ServerPort = 6969;


	public static void main(String args[]) throws UnknownHostException, IOException 
	{ 
		Scanner scn = new Scanner(System.in); 
		
		// getting localhost ip 
		InetAddress ip = InetAddress.getByName("localhost"); 
		
		// establish the connection 
		Socket s = new Socket(ip, ServerPort); 
		
		//input and out streams 
		DataInputStream inStream = new DataInputStream(s.getInputStream()); 
		DataOutputStream outStream = new DataOutputStream(s.getOutputStream());

		//File transfer streams
		byte[] b = new byte[9999999];
		InputStream is = s.getInputStream();
		FileOutputStream fr = new FileOutputStream("./success.html");
		BufferedOutputStream bos = new BufferedOutputStream(fr);
		int bytesRead = is.read(b, 0, b.length);
		System.out.println("recieve complete: " + b.length);

		bos.write(b, 0, bytesRead);
		System.out.println("write complete: " + bytesRead);

		bos.close();
		s.close();

		

/* 
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

		// readFile thread 
		Thread readFile = new Thread(new Runnable() 
		{ 
			@Override
			public void run() { 

				while (true) { 
					try { 
						// read the message sent to this client 
						int bSize = is.read(b,0,b.length);
						fr.write(b,0,bSize); 
						System.out.println("Copy Complete"); 
					} catch (IOException e) { 

						e.printStackTrace(); 
					} 
				} 
			} 
		}); 

		sendMessage.start(); 
		readMessage.start(); 
		readFile.start();
 */
	} 
} 


