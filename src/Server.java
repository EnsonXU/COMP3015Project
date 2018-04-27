import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Server {

	
	public static void server() throws IOException {
		Scanner scanner = new Scanner(System.in);
		String statement;
		ServerCommandPrompt prompt = new ServerCommandPrompt();
		System.out.println("Welcome to Cloud system!");
		System.out.println("1. set up your server : setpw+password");
		System.out.println("2. change your dirctory: cd+path");
		System.out.println("3. list the files in current folder: ls");
		
		
		while (true) {
			System.out.print("> ");
			statement = scanner.nextLine().trim();
			if (statement.equalsIgnoreCase("exit"))
				break;
			prompt.exec(statement);

		}
		scanner.close();
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		server();
	}
}
