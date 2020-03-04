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
			//DataInputStream inStream = new DataInputStream(s.getInputStream());
			BufferedReader d = new BufferedReader(new InputStreamReader(s.getInputStream()));
			DataOutputStream outStream = new DataOutputStream(s.getOutputStream());

			// sendMessage thread
			Thread sendMessage = new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {

						// read the message to deliver.
						String msg = scn.nextLine();

						try {
							// write on the output stream
							if (msg.equals("!down")){
								downloadFile(s, "download.html");
							}
							outStream.writeUTF(msg);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			});

			//commented out this block, changed very little but readUTF kept throwing errors (assume because initially when thread created there is  no input stream so kept returning null, not sure how to handle this)
			// readMessage thread
			Thread readMessage = new Thread(new Runnable() {
				@Override
				public void run() {

					try {
						// read the message sent to this client
						// String msg = inStream.readUTF(); // this was producing errors
						String msg = d.readLine();
						while (msg!=null && !msg.equals("!exit")) {
							//if (msg != null) {

								//else{
									System.out.println(msg);
								//}
									msg = d.readLine();
								//}
						}
						
						//System.out.println("hi, thread should be ending.");
					}
					/*
					 * catch (EOFException ex) { run(); }
					 */
					catch (IOException e) {
						e.printStackTrace();
					}

				}
			}); 

			sendMessage.start();
			readMessage.start();
			// readFile.start();

		} catch (UnknownHostException ex) {
			System.out.println("Server not found: " + ex.getMessage());
		} catch (IOException ex) {
			System.out.println("I/O Error: " + ex.getMessage());
		}
	}

	static void downloadFile(Socket s, String file)  throws UnknownHostException, IOException
	{
		//Download file from server
		byte[] b = new byte[23520];
		InputStream is = s.getInputStream();
		FileOutputStream fr = new FileOutputStream(file);
		BufferedOutputStream bos = new BufferedOutputStream(fr);
		// read downloaded file
		int bytesRead = is.read(b, 0, b.length);

		bos.write(b, 0, bytesRead);
		System.out.println("download complete: " + bytesRead);
		bos.close();

	}	
	
	static void uploadFile(Socket s, String file)  throws UnknownHostException, IOException
	{
		//input is file to be uploaded
		File myFile = new File (file);

		BufferedInputStream fileIS = new BufferedInputStream(new FileInputStream(myFile));
		byte b[] = new byte[(int) myFile.length()];
		fileIS.read(b, 0, b.length);

		OutputStream fileOS = s.getOutputStream();

		fileOS.write(b, 0, b.length);
		fileOS.flush();
		System.out.println("upload complete: " + b.length);

		fileOS.close();
	} 
} 


