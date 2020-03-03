/* 
CSC3002F
KHRTHU001 | SCHZEE001 | KGWKAG001
Networking Assignment

Client Side Services
Establishes connection to server, sends & recieve messages (seperate threads for each act)
*/

import java.io.*;
import java.io.EOFException;
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
		try {
			Socket s = new Socket(ip, ServerPort);

			System.out.println("Successfully connected to server at " + ip + " on port " + ServerPort);
			System.out.println("To interact with server:\n!upload - to upload a file to the server\n!files - to query the server for its list of available files\n!name_of_file - to download one of the listed files from the server\n!exit - to exit\n\n");

			// input and out streams
			DataInputStream inStream = new DataInputStream(s.getInputStream());
			DataOutputStream outStream = new DataOutputStream(s.getOutputStream());

			downloadFile(s, "success.html");

			// sendMessage thread
			Thread sendMessage = new Thread(new Runnable() {
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

			//commented out this block, changed very little but readUTF kept throwing errors (assume because initially when thread created there is  no input stream so kept returning null, not sure how to handle this)
			// readMessage thread
			/* Thread readMessage = new Thread(new Runnable() {
				@Override
				public void run() {
					String msg = "test";
					while (msg!=null) {
						try {
							// read the message sent to this client
							msg = inStream.readUTF(); // this was producing errors
							System.out.println(msg);
						} catch (EOFException ex) {
							run();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}); */

			sendMessage.start();
			//readMessage.start();
			// readFile.start();

		} catch (UnknownHostException ex) {
			System.out.println("Server not found: " + ex.getMessage());
		} catch (IOException ex) {
			System.out.println("I/O Error: " + ex.getMessage());
		}
	}

	static void downloadFile(Socket s, String file)  throws UnknownHostException, IOException
	{
		//File transfer streams
		byte[] b = new byte[9999999];
		InputStream is = s.getInputStream();
		FileOutputStream fr = new FileOutputStream(file);
		BufferedOutputStream bos = new BufferedOutputStream(fr);
		// read downloaded file
		int bytesRead = is.read(b, 0, b.length);
		System.out.println("recieve complete: " + b.length);

		bos.write(b, 0, bytesRead);
		System.out.println("write complete: " + bytesRead);
		bos.close();

	} 
} 


