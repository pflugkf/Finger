import java.net.Socket;
import java.net.UnknownHostException;
import java.net.InetAddress;
import java.io.*;

/**
 * FingerTcp class
 * 
 * This class implements the Finger protocol
 * 
 * @author Kristin Pflug
 * @version 1, 10/3/16
 */
public class FingerTcp {
	
	/**
	 * Main method which runs the Finger protocol
	 * 
	 * @param args String array holding the command line arguments
	 */
	public static void main(String[] args){
		
		/**Checks for invalid number of arguments**/
		if(args.length > 3 || args.length == 0){
			errorMsg();
		}
		
		try{
			Socket myClient;
			/**Mandatory domain name/IP address of the end host**/
			InetAddress hostname = InetAddress.getByName(args[0]);
			/**Optional query for userID**/
			String userID = null;
			/**Optional port number, defaults to 79 (the Finger port)**/
			int portNum = 79;
			
			/**Sets variables using command line arguments, 
			    based on # of arguments**/
			if(args.length == 2){
				if(arg2Check(args[1])){
					portNum = Integer.parseInt(args[1]);
				}else{
					userID = args[1];
				}
			}
			else if(args.length == 3){
				
				if(arg2Check(args[1]) && arg3Check(args[2])){
					portNum = Integer.parseInt(args[1]);
					userID = args[2];
				}else{
					/**arguments are in the wrong order**/
					errorMsg();
				}
			}
			else{
				portNum = 79;
				userID = null;
			}
			
			/**Socket is initialized using given hostname and port number**/
			myClient = new Socket(hostname, portNum);
			
			PrintWriter outboundStream = new PrintWriter
					(myClient.getOutputStream(), true);
			
			BufferedReader inboundStream = new BufferedReader
					(new InputStreamReader(myClient.getInputStream()));
			
			/**Requests information from the server**/
			if(userID == null){
				/**Get information on polaris**/
				outboundStream.println("\n");
			}else{
				/**Get all information on given userID**/
				outboundStream.println(userID);
			}
			
			boolean moreStuff = true;
			
			/**reads in the data from the server response above**/
			while (moreStuff){
				String line = inboundStream.readLine();
				
				/**checks if there is more data**/
				if(line == null){
					moreStuff = false;
				}else{
					System.out.println(line);
				}
			}

			/**Closes the PrintWriter, BufferedReader, and Socket**/ 
			outboundStream.close();
			inboundStream.close();
			myClient.close();
		}
		catch(UnknownHostException e){
			System.out.println("Invalid host name/address: " + e);
		}
		catch(IOException e){
			System.out.println(
					"IO Exception while trying to connect to localhost: " + e);
		}

	}
	
	/**
	 * Helper method that checks whether argument 2 is a port number or a
	 *query by checking whether argument 2 is an Integer.
	 * 
	 * @param args String holding command line argument 2
	 * @return Boolean value true/false, depending on whether arg2 is an int
	 */
	public static boolean arg2Check(String arg2){
		boolean isNum = false;
		try{
			isNum = true;
			/**Attempts to make arg1 into an integer**/
			Integer.parseInt(arg2);
		}
		catch(NumberFormatException e){
			/**if this exception is thrown, then arg2 is a string and the method
			returns false**/
			isNum = false;
		}
		
		return isNum;
	}
	
	/**
	 * Helper method that checks whether argument 2 is a port number or a
	 *query by checking whether argument 2 is a String.
	 * 
	 * @param arg3 String holding command line argument 3
	 * @return Boolean value true/false, depending on whether arg3 is a String
	 */
	public static boolean arg3Check(String arg3){
		boolean isString = true;
		try{
			isString = false;
			/**Attempts to make arg2 into an integer**/
			Integer.parseInt(arg3);
		}
		catch(NumberFormatException e){
			/**if this exception is thrown, then arg3 is a string and the method
			returns true**/
			isString = true;
		}
		
		return isString;
	}
	
	/**
	 * Helper method that prints an error message and the usage guide
	 */
	public static void errorMsg(){
		System.out.println("Error: Incorrect Arguments");
		System.out.println("Usage: finger <hostname> [<port>] [<query>]");
		System.exit(-1);
	}
}

