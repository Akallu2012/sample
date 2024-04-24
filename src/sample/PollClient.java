package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class PollClient {
	  private static final String HOST = "localhost";
	  private static final int PORT = 8000;

	  public static void main(String[] args) throws IOException {
	    try (
	      Socket socket = new Socket(HOST, PORT);
	      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	      Scanner scanner = new Scanner(System.in);
	    ) {
	      System.out.println("Connected to poll server.");
	      System.out.println("Enter 'yes', 'no', or 'dontcare' to vote.");
	      System.out.println("Enter 'getCounts' to retrieve current vote counts.");
	      String inputLine;
	      while (!(inputLine = scanner.nextLine()).equals("exit")) {
	        out.println(inputLine);
	        if (inputLine.equals("getCounts")) {
	          System.out.println(in.readLine());
	        }
	      }
	    }
	  }
	}
