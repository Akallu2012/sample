package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class PollServer {
	  private int yesCount = 0;
	  private int noCount = 0;
	  private int dontCareCount = 0;
	  private ServerSocket serverSocket;
	  private boolean running = true;

	  public PollServer(int port) throws IOException {
	    serverSocket = new ServerSocket(port);
	  }

	  public void start() {
	    System.out.println("Poll server started.");
	    while (running) {
	      try {
	        Socket clientSocket = serverSocket.accept();
	        Thread t = new Thread(new ClientHandler(clientSocket));
	        t.start();
	      } catch (IOException e) {
	        e.printStackTrace();
	      }
	    }
	  }

	  public void stop() {
	    running = false;
	    try {
	      serverSocket.close();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	  }

	  private synchronized void updateVote(String vote) {
	    if (vote.equals("yes")) {
	      yesCount++;
	    } else if (vote.equals("no")) {
	      noCount++;
	    } else if (vote.equals("dontcare")) {
	      dontCareCount++;
	    }
	  }

	  private synchronized String getCounts() {
	    return "Yes: " + yesCount + "\nNo: " + noCount + "\nDon't care: " + dontCareCount;
	  }

	  private class ClientHandler implements Runnable {
	    private Socket clientSocket;
	    private BufferedReader in;
	    private PrintWriter out;
	    public ClientHandler(Socket socket) {
	      this.clientSocket = socket;
	    }

	    public void run() {
	      try {
	        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	        out = new PrintWriter(clientSocket.getOutputStream(), true);

	        String inputLine;
	        while ((inputLine = in.readLine()) != null) {
	          if (inputLine.equals("getCounts")) {
	            out.println(getCounts());
	          } else {
	            updateVote(inputLine);
	          }
	        }
	      } catch (IOException e) {
	        e.printStackTrace();
	      } finally {
	        try {
	          clientSocket.close();
	        } catch (IOException e) {
	          e.printStackTrace();
	        }
	      }
	    }
	  }

	  // Main method to start the server
	  public static void main(String[] args) throws IOException {
	    PollServer server = new PollServer(8000);
	    server.start();
	  }
	}
