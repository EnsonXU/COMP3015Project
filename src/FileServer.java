
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class FileServer {

	File currentDir = new File(".");
	ServerSocket sSocket;
	DataInputStream in;
	ArrayList<String> allnames = new ArrayList<>();

	public FileServer(int port, File dir) throws IOException {
		currentDir = dir;
		sSocket = new ServerSocket(port);
		while (true) {
			System.out.println("Listening at port " + port);
			Socket cSocket = sSocket.accept();
			in = new DataInputStream(cSocket.getInputStream());
			//System.out.println("1");
			serve(cSocket);
			// cSocket.close();
		}
	}

	void serve(Socket socket) throws IOException {
		//System.out.println("2");
		String name = "";
		byte[] buffer = new byte[1024];
		int count = 0;
		int len = 0;
		int size = in.readInt();
		while (count < size) {
			len = in.read(buffer, 0, buffer.length);
			name += new String(buffer, 0, len);
			count += len;
		}
		System.out.println(name);
		String filename = name;
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		if (name.equals("001")) {

			File[] fileList = currentDir.listFiles();
			String info = "";
			for (int i = 0; i < fileList.length; i++)
				info += getInfo(fileList[i]) + "\n";
			System.out.println(info);
			String msg = info;
			out.writeInt(msg.length());
			out.write(msg.getBytes());
			// sSocket.close();
		} else if (name.contains("002")) {
			String[] tmp = name.split(" ");
			currentDir = new File(tmp[1]);
		} else if (name.contains("003")) {
			// send all names on this folder
			File[] fileList = currentDir.listFiles();
			String info = "";
			for (int i = 0; i < fileList.length; i++) {
				info += getInfo(fileList[i]) + "\n";
				allnames.add(fileList[i].getName());
			}
			//System.out.println(info);
			String msg = "";
			for (int i = 0; i < allnames.size(); i++) {
				msg = msg + allnames.get(i) + " ";
			}
			out.writeInt(msg.length());
			out.write(msg.getBytes());
			System.out.println(msg);
			
			/*
			for (int i = 0; i < allnames.size(); i++) {
				//System.out.println("******");
				String name2 = "";
				byte[] buffer2 = new byte[1024];
				int count2 = 0;
				int len2 = 0;
				int size2 = in.readInt();
				while (count2 < size2) {
					len2 = in.read(buffer, 0, buffer.length);
					name2 += new String(buffer, 0, len2);
					count2 += len2;
				}
				System.out.println(name);
				String filename2 = name;
				sendFile(out, filename2);
			}
			*/
		} else {
			System.out.println("3");
			
			//for (int i = 0; i < names.length; i++) {
			sendFile(out, filename);
			//}
		}
		// out.close();
	}

	private String getInfo(File f) {
		Date date = new Date(f.lastModified());
		String ld = new SimpleDateFormat("MMM dd, yyyy").format(date);
		if (f.isFile()) {
			return String.format("%dKB\t%s\t%s", (int) Math.ceil((float) f.length() / 1024), ld, f.getName());
		} else
			return String.format("<DIR>\t%s\t%s", ld, f.getName());

	}

	void sendFile(DataOutputStream out, String filename) throws IOException {
		System.out.println("*****");
		File file = new File(filename);
		FileInputStream fin = new FileInputStream(file);
		byte[] buffer = new byte[1024];
		int count = 0;
		int len = 0;
		long size = file.length();
		out.writeLong(size);
		count = 0;
		while (count < size) {
			len = fin.read(buffer, 0, buffer.length);
			count += len;
			out.write(buffer, 0, len);
		}
	}

}
