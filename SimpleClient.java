

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

public class SimpleClient {
    public static void main(String[] args) throws IOException {
    	
        if (args.length != 2) {
            System.err.println(
                "Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try (
            Socket clientSocket = new Socket(hostName, portNumber);
        		
            BufferedReader responseReader= // stream to read text response from server
            	new BufferedReader(
            		new InputStreamReader(clientSocket.getInputStream())); 	
        		
            PrintWriter requestWriter = // stream to write text requests to server
                new PrintWriter(clientSocket.getOutputStream(), true);

        ) {
			//Total packets to be received
        	int totalPackets = Integer.parseInt(responseReader.readLine());
        	
        	//Array that will hold and keep track of the packets being sent.
			String[] messageClient = new String[totalPackets];
			
			//Initiates the amount of packets received to zero.
			int amountReceived = 0;
			
			//Iterates as long as all the packets have not been received. 
			while(amountReceived < totalPackets) {
				
				//ServerResponse will hold a string whose first character is a letter of the message, and
				//the rest of the string is relevant metadata of the packet(index where the letter belongs).
				String serverResponse;
				
				//Reads the input from the Server.
				while((serverResponse = responseReader.readLine()) != null) {
					
					//Fills the messageClient array as long as the current packet is not equal to Finished.
					if(!serverResponse.equals("Finished!")) {
						//Fills the messageClient array according to the metadata of the packets being received. 
						messageClient[Integer.parseInt(serverResponse.substring(1))] = serverResponse.substring(0,1);
						System.out.println("Received packet " + ((Integer.parseInt(serverResponse.substring(1)) + 1)) + " from the server.");
						//Adds one to the total amount of packets received.
						amountReceived++;	
					}else {
						//breaks out of the loop after all the packets have been received, 
						//and the serverResponse  equals Finished. 
						break;
					}
			}	
            
				//Iterates as long as all the packets have not yet been received.
				if(amountReceived < totalPackets) {
					System.out.println("\nStill missing " + (totalPackets - amountReceived) + " packet\\s.\n");
					//Iterates through the messageClient array and sends the server the 
					//metadata of the missing packets.
					for(int i = 0; i < messageClient.length; i++) {
						//Checks to see if the array index == null.
						if(messageClient[i] == null) {
							requestWriter.println(""+ i); 
						}
					}
					//Indicates that all the missing information metadata was sent back to the server.
					requestWriter.println("All Missing Packets Sent!");	
				}else {
					break;
				}
			}
            		
			//	
			System.out.println("\nAll Packets Received!--Message Complete!");
			//Prints the final message once all packets have been received. 
			for(int i = 0; i < messageClient.length; i++) {
				System.out.print(messageClient[i]);
			}
            
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        } 
    }
}
