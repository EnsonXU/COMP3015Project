import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class ClientCommandPrompt {
	File currentDir = new File(".");
	boolean server = false;
	boolean client = false;
	String path;

	public void exec(String statement) throws IOException {
		String command = null;
		String option = null;
		String msg;

		int endIdx = statement.trim().indexOf(' ');
		if (endIdx > 0) {
			command = statement.substring(0, endIdx).trim();
			option = statement.substring(endIdx + 1).trim();
		} else
			command = statement;

		switch (command.toLowerCase()) {
		case "get":
			get(option);
			break;
		case "getall":
			getsall();
			break;
		case "cd":
			changeDir(option);
			break;
		case "ls":
			listFiles();
			break;
		case "fi":
			getInfo(option);
			break;
		case "exit":
			System.exit(0);

		default:
			msg = String.format("'%s' is not recognized as an command.", command);
			System.out.println(msg);
			break;
		}
	}

	int port = 9001;
	String host;
	DataOutputStream out;

	private void get(String option) throws IOException {
		// TODO Auto-generated method stub
		System.out.println(option);
		String[] names = option.split(" ");
		if(names.length>1) {
		for(int i=0;i<names.length;i++) {
			FileClient client = new FileClient("127.0.0.1", port, names[i]);
			}
		}else {
		FileClient client = new FileClient("127.0.0.1", 9001, option);
		}
		/*
		Socket socket = new Socket("127.0.0.1", 9001);
		DataInputStream in = new DataInputStream(socket.getInputStream());
		out = new DataOutputStream(socket.getOutputStream());
		
			String[] names = option.split(" ");
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
				System.out.println("Completed receive " + name);
			}

		in.close();
		out.close();
		socket.close();
		*/
	}

	private boolean isPortUsing(String host2, int port2) {
		boolean flag = false;
		try {
			Socket socket = new Socket(host2, port2);
			flag = true;
			socket.close();
		} catch (IOException e) {

		}
		return flag;
	}
	
	String[] allnames;

	private void getsall() throws IOException {
		// TODO Auto-generated method stub
		
			Socket socket = new Socket("127.0.0.1", 9001);
			DataInputStream in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			String option="003";
			out.writeInt(option.length());
			out.write(option.getBytes());
			byte[] buffer = new byte[1024];
			int count = 0, size;
			int len;
			count = 0;
			String msg = "";
			size = in.readInt();// receive the message’s length
			while (count < size) { // check whether completed or not
				len = in.read(buffer, 0, Math.min(buffer.length, size - count)); // receive message
				count = count + len; // update counter
				msg += new String(buffer, 0, len);
			}
			System.out.println(msg);	
			allnames = msg.split(" ");
			for(int i=0;i<allnames.length;i++) {
			FileClient client = new FileClient("127.0.0.1", port, allnames[i]);
			}
	}

	Socket cSocket;

	private void changeDir(String path) throws IOException {
		/*
		if (path == null) {
			System.out.println(currentDir.getCanonicalPath());
			return;
		}

		File dir;
		if (path.startsWith("/") || path.startsWith("\\") || path.contains(":"))
			dir = new File(path);
		else
			dir = new File(currentDir.getCanonicalPath() + "/" + path);

		if (!dir.exists() || dir.isFile()) {
			System.out.println("The system cannot find the path specified.");
			return;
		}
		*/
		File dir = new File(path);
		currentDir = new File(path);
		
		Socket socket = new Socket("127.0.0.1", 9001);
		DataInputStream in = new DataInputStream(socket.getInputStream());
		out = new DataOutputStream(socket.getOutputStream());
		String option="002 "+dir;
		out.writeInt(option.length());
		out.write(option.getBytes());
		
		// currentPath =dir.getAbsolutePath();
		System.out.println("enter the " + dir + " directory");
	}

	private void getInfo(String fn) {
		// change the string into file
		File f = new File("src/" + fn);
		Date date = new Date(f.lastModified());
		String ld = new SimpleDateFormat("MMM dd, yyyy").format(date);
		if (f.isFile()) {
			System.out.println(
					String.format("%dKB\t%s\t%s", (int) Math.ceil((float) f.length() / 1024), ld, f.getName()));
		} else
			System.out.println("There is no such file!");
	}

	private void listFiles() throws IOException {
		//FileClient client = new FileClient("127.0.0.1", 9001, "001");
		Socket socket = new Socket("127.0.0.1", 9001);
		DataInputStream in = new DataInputStream(socket.getInputStream());
		out = new DataOutputStream(socket.getOutputStream());
		String option="001";
		out.writeInt(option.length());
		out.write(option.getBytes());
		byte[] buffer = new byte[1024];
		int count = 0, size;
		int len;
		count = 0;
		String msg = "";
		size = in.readInt();// receive the message’s length
		while (count < size) { // check whether completed or not
			len = in.read(buffer, 0, Math.min(buffer.length, size - count)); // receive message
			count = count + len; // update counter
			msg += new String(buffer, 0, len);
		}
		System.out.println(msg);
		/*
		String[] tmp = msg.split(" ");
		int j=0;
		for(int i=2;i<tmp.length;i=i+3)
		{
			allnames[j] = tmp[i];
			j++;
		}
		*/
		
	}
	
	
}
