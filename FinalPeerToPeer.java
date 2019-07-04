import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class FinalPeerToPeer 
{
  public static ArrayList<String> ipList = new ArrayList<>();

  public static void main(String[] args) throws Exception 
  {
	ipList.add("150.243.201.096"); //Client 1
	ipList.add("150.243.215.120"); //Client 2
	ipList.add("150.243.153.007"); //Client 3
	ipList.add("150.243.147.003"); //Client 4
    startServer();
    startSender();
  }

  public static void startSender() throws UnknownHostException
  {
    (new Thread() 
    {
        @Override
        public void run() 
        {
            byte data[] = "Client 1 is uppp".getBytes();
            DatagramSocket socket = null;
            try 
            {
                socket = new DatagramSocket();
                socket.setBroadcast(true);
            } 
            catch (SocketException ex) 
            {
                ex.printStackTrace();
            }
            while (true)
            {
	        	for (int j = 0; j < ipList.size(); j++) 
	        	{
	        		InetAddress ipAddress;
					try 
					{
						ipAddress = InetAddress.getByName(ipList.get(j));
						if (ipAddress.isReachable(10000))
						{
							DatagramPacket sendPacket = new DatagramPacket(data,data.length,ipAddress,9876);
			                socket.send(sendPacket);
						}
						else
						{
							for (int i = 0; i < ipList.size(); i++)
							{
								String downString = "Client " + j + " is down";
					            byte dataFail[] = downString.getBytes();
								DatagramPacket sendPacket = new DatagramPacket(dataFail, dataFail.length, InetAddress.getByName(ipList.get(i)), 9876);
								socket.send(sendPacket);
							}
						}
		                
		                Thread.sleep(5000);
					} 
					catch (UnknownHostException e) 
					{
						System.out.println("Client " + j + 1 + " is down.");
					}
					catch (IOException ex) 
	                {
						ex.printStackTrace();
	                }
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
	        	}
            }
        }
        }).start();
    }

  public static void startServer() 
  {
    (new Thread() 
    {
        @Override
        public void run() 
        {
                DatagramSocket serverSocket = null;
                try 
                {
                	serverSocket = new DatagramSocket(9876);
                } 
                catch (SocketException ex) 
                {
                    ex.printStackTrace();
                }
                DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);
                while (true) 
                {
                	try 
                	{
                		serverSocket.receive(receivePacket);
	                    String temp = new String(receivePacket.getData());
	                    
	                    System.out.println(temp);
                	} 
                	catch (IOException ex) 
                	{
	                    ex.printStackTrace();
	                    //parent.quit();
                	}
                }
            }
    }).start();
 }
}
