
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class ServerCommandPrompt {
	File currentDir = new File(".");
	DataOutputStream out;
	boolean server = false;
	boolean client = false;
	String path;
	String password;
	int port = 9001;
	int threadnum=0;
	int usernum=0;

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
		case "setpw":
			setup(option);
			break;
		case "cd":
			changeDir(option);
			break;
		case "ls":
			listFiles();
			break;
		case "chpw":
			changepw(option);
			break;
		// add more cases here for different commands

		default:
			msg = String.format("'%s' is not recognized as an command.", command);
			System.out.println(msg);
			break;
		}
	}

	private void setup(String option) throws IOException {
		// TODO Auto-generated method stub
		this.password = option;
		System.out.println("Set up successfully!");
		while (true) {
			DataInputStream in;
			DataOutputStream out;
			ServerSocket sSocket = new ServerSocket(8999);
			Socket cSocket = sSocket.accept();
			in = new DataInputStream(cSocket.getInputStream());
			out = new DataOutputStream(cSocket.getOutputStream());
			String msg = "";
			byte[] buffer = new byte[1024];
			int count = 0;
			int len = 0;
			int size = in.readInt();
			while (count < size) {
				len = in.read(buffer, 0, buffer.length);
				msg += new String(buffer, 0, len);
				count += len;
			}
			if (msg.equals(this.password)) {
				out.writeBoolean(true);
				System.out.println("A user connected to your server");
				usernum++;
				while (threadnum != usernum) {
					threadnum++;
					addThread(9001);
					//System.out.println(port);	
				}
				cSocket.close();
				sSocket.close();
			}
		}
		
	}

	public void addThread(int port) throws IOException {
		Thread t = new Thread() {
			@Override
			public void run() {
				try {
					usernum++;
					threadnum++;
					FileServer server = new FileServer(9001, currentDir);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		t.start();
	}

	private void changepw(String option) {
		// TODO Auto-generated method stub
		this.password = option;
	}

	private void changeDir(String path) throws IOException {
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

		currentDir = dir;
		
		System.out.println("enter the " + dir + " directory");
	}
	
	
	
	private String getInfo(File f) {
		Date date = new Date(f.lastModified());
		String ld = new SimpleDateFormat("MMM dd, yyyy").format(date);
		if (f.isFile()) {
			return String.format("%dKB\t%s\t%s", (int) Math.ceil((float) f.length() / 1024), ld, f.getName());
		} else
			return String.format("<DIR>\t%s\t%s", ld, f.getName());

	}
	
	//ArrayList<String> allnames = new ArrayList<>();

	private void listFiles() throws IOException {
		File[] fileList = currentDir.listFiles();
		String info = "";
		for (int i = 0; i < fileList.length; i++)
		{
			info += getInfo(fileList[i]) + "\n";
			//allnames.add(fileList[i].getName());
		}
		System.out.println(info);
	
	}

}
