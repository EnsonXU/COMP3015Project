import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client2 {
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		boolean correct = false;
		Socket cSocket;
		DataInputStream in;
		DataOutputStream out;
		cSocket = new Socket("127.0.0.1",8999);
		in = new DataInputStream(cSocket.getInputStream());
		out = new DataOutputStream(cSocket.getOutputStream());
		String str = String.format("Connected to server %s:%d through the local port %d",
				cSocket.getInetAddress().getHostAddress(), cSocket.getPort(), cSocket.getLocalPort());
		System.out.println(str);
		while (correct != true) {
			System.out.println("Please enter the password:");
			System.out.print("> ");
			String msg;
			msg = scanner.nextLine();
			out.writeInt(msg.length()); // send the message’s length
			out.write(msg.getBytes());
			correct = in.readBoolean();
		}
		System.out.println("Successfully logged in!");
		System.out.println("Welcome to Cloud system!");
		System.out.println("1. Download file or files : get+full file name");
		System.out.println("2. Download all files in current folder : getsall");
		System.out.println("3. Change your dirctory: cd+path");
		System.out.println("4. List the files in current folder: ls");
		System.out.println("5. Show file information: fi+full file name");
		System.out.println("6. Quit: exit");
		while (true) {
			String statement;
			ClientCommandPrompt prompt = new ClientCommandPrompt();
			System.out.print("> ");
			statement = scanner.nextLine().trim();
			if (statement.equalsIgnoreCase("exit"))
				break;
			prompt.exec(statement);
		}
		scanner.close();
		cSocket.close();
	}
}
