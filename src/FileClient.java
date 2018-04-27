
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class FileClient {
	
	String ip;
	int port;
	DataOutputStream out;

	public FileClient(String ip, int port, String option) throws IOException {
		Socket socket = new Socket(ip, port);
		this.ip = ip;
		this.port = port;
		DataInputStream in = new DataInputStream(socket.getInputStream());
		out = new DataOutputStream(socket.getOutputStream());
		receiveFile(in, option);
		in.close();
		out.close();
		socket.close();
	}
	
	void receiveFile(DataInputStream in, String option) throws IOException {
		// get the file name that needs to receive
		String[] names = option.split(" ");
		if (option.equals("001") == false) {
			
			//Socket socket = new Socket(ip, port);
			//DataOutputStream out1 = new DataOutputStream(socket.getOutputStream());
			//System.out.println(names[0]+" "+names[1]+" "+names[2]);
			/*
			System.out.println(names.length);
			for (int i = 0; i < names.length; i++) {
				String name = names[i];
				System.out.println(name);
				out.writeInt(name.length());
				out.write(name.getBytes());
				System.out.println("out!!!!!!!");
				byte[] buffer = new byte[1024];
				long count = 0, size;
				int len;
				String filename = name;
				File file;
				FileOutputStream fout;
				file = new File(filename);
				fout = new FileOutputStream(file);
				// receive the size of the file
				
				size = in.readLong();
				System.out.println("received!!!!!!!");
				count = 0;
				// receive the file
				while (count < size) {
					len = in.read(buffer, 0, (int) Math.min(buffer.length, size - count));
					count += len;
					fout.write(buffer, 0, len);
				}
				//fout.close();
				System.out.println("Completed receive " + name);
			}
			*/
			if(names.length>1) {
			for (int i = 0; i < names.length; i++) {
				String name = names[i];
				out.writeInt(name.length());
				out.write(name.getBytes());

				byte[] buffer = new byte[1024];
				long count = 0, size;
				int len;
				String filename = name;
				File file;
				FileOutputStream fout;
				file = new File(filename);
				fout = new FileOutputStream(file);
				// receive the size of the file
				size = in.readLong();
				count = 0;
				// receive the file
				while (count < size) {
					len = in.read(buffer, 0, (int) Math.min(buffer.length, size - count));
					count += len;
					fout.write(buffer, 0, len);
				}
				fout.close();
			}
			}else {
				String name = option;
				out.writeInt(name.length());
				out.write(name.getBytes());

				byte[] buffer = new byte[1024];
				long count = 0, size;
				int len;
				String filename = name;
				File file;
				FileOutputStream fout;
				file = new File(filename);
				fout = new FileOutputStream(file);
				// receive the size of the file
				size = in.readLong();
				count = 0;
				// receive the file
				while (count < size) {
					len = in.read(buffer, 0, (int) Math.min(buffer.length, size - count));
					count += len;
					fout.write(buffer, 0, len);
				}
				fout.close();
			}

		} else {
			out.writeInt(option.length());
			out.write(option.getBytes());
			byte[] buffer = new byte[1024];
			int count = 0, size;
			int len;
			count = 0;
			String msg = "";
			size = in.readInt();
			while (count < size) { 
				len = in.read(buffer, 0, Math.min(buffer.length, size - count)); 
				count = count + len; 
				msg += new String(buffer, 0, len);
			}
			System.out.println(msg);
		}

	}

	public FileClient(String ip, int port, String[] allnames) throws IOException {
		Socket socket = new Socket(ip, port);
		DataInputStream in = new DataInputStream(socket.getInputStream());
		out = new DataOutputStream(socket.getOutputStream());
		receiveFile(in,allnames);
		in.close();
		out.close();
		socket.close();
		System.out.println("Completed.");
	}

	void receiveFile(DataInputStream in, String[] allnames) throws IOException {
		// get the file name that needs to receive
		// give all the file names
		for (int i = 0; i < allnames.length; i++) {
			String name = allnames[i];
			out.writeInt(name.length());
			out.write(name.getBytes());
			System.out.println("Receiveing");
			byte[] buffer = new byte[1024];
			long count = 0, size;
			int len;
			String filename = name;
			File file;
			FileOutputStream fout;
			file = new File(filename);
			fout = new FileOutputStream(file);
			// receive the size of the file
			size = in.readLong();
			count = 0;
			// receive the file
			while (count < size) {
				len = in.read(buffer, 0, (int) Math.min(buffer.length, size - count));
				count += len;
				fout.write(buffer, 0, len);
			}
			fout.close();
		}

	}
}