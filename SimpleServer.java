
import java.net.*;
import java.util.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Collections;

public class SimpleServer {
	static Random rnd = new Random();
	
	public static void main(String[] args) throws IOException {

		
		if (args.length != 1) {
			System.err.println("Usage: java EchoServer <port number>");
			System.exit(1);
		}

		int portNumber = Integer.parseInt(args[0]);

		try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
				Socket clientSocket1 = serverSocket.accept();
				PrintWriter responseWriter1 = new PrintWriter(clientSocket1.getOutputStream(), true);
				BufferedReader requestReader1 = new BufferedReader(new InputStreamReader(clientSocket1.getInputStream()));
	
				) {
			
			//Message to be sent to client.
			String messageString = "Hello World Project!";
			
			//Gets the total amount of packets that need to be sent to the client
			responseWriter1.println(messageString.length());
			
			//Instantiates the ArrayList that holds all the packets that need to be sent over to the client.
			ArrayList<Packet> packets = new ArrayList<Packet>();
			
			//Instantiates the ArrayList that holds the packet that the client has not received.
			ArrayList<Packet> missingPacket = new ArrayList<>();
			
			//Fills the packets and missingPacket ArrayLists with all 20 packets.
			for(int i = 0; i < messageString.length(); i++) {
				packets.add(new Packet(messageString.substring(i,i+1), i));
				missingPacket.add(packets.get(i));
			}
			
			//Iterates loop as long as there are packets in the missingPacket ArrayList.
			do {
				//Shuffles the missingPacket ArrayList.
				Collections.shuffle(missingPacket);
				
				//Iterates through the missingPackets ArrayList.
				for(int i = 0; i < missingPacket.size(); i++) {
					//Only sends packet with an 80% probability
					if(rnd.nextInt(100) < 80) {
						responseWriter1.println(missingPacket.get(i).getCharacter() +""+ missingPacket.get(i).getIndex());
					}
				}
				//Indicates that all packets of this round have been sent. 
				responseWriter1.println("Finished!");
				
				
				String usersRequest;
				//Reinitializes the missingPacket ArrayList to an empty ArrayList.
				missingPacket = new ArrayList<>();
	
				//Reads the clients input.
				while((usersRequest = requestReader1.readLine()) != null) {
					//Adds the missing packets to the missingPacket ArrayList as long as there are still packets missing.
					if(!usersRequest.equals("All Missing Packets Sent!")) {
						missingPacket.add(packets.get(Integer.parseInt(usersRequest)));
					}else {
						//If all missing packets of this round have been received, break out of the loop.
						break;
					}
				}
				
			} while (missingPacket.size() != 0);
			System.out.println("Full Message has been sent.");
			
		}catch (IOException e) {
			System.out.println(
					"Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
			System.out.println(e.getMessage());
		}

	}


}
