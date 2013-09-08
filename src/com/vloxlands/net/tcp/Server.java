package com.vloxlands.net.tcp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.vloxlands.Vloxlands;
import com.vloxlands.settings.CFG;

/**
 * @author Dakror
 */
public class Server extends Thread
{
	ServerSocket ss;
	ArrayList<Client> clients = new ArrayList<Client>();
	
	public Server(InetAddress ipAddress)
	{
		try
		{
			
			ss = new ServerSocket(CFG.SERVER_PORT, 0, ipAddress);
			start();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void run()
	{
		CFG.p("[SERVER]: Starting TCP");
		CFG.p("[SERVER]: -------------------------------------------------");
		while (Vloxlands.running)
		{
			Socket s = null;
			
			try
			{
				s = ss.accept();
				handleConnection(s);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void handleConnection(Socket s)
	{
		CFG.p("[SERVER]: Client connected: " + s.getInetAddress().getHostAddress().toString());
		// clients.add(s);
		// DataInputStream dis = null;
		// DataOutputStream dos = null;
		// try
		// {
		// dis = new DataInputStream(s.getInputStream());
		// dos = new DataOutputStream(s.getOutputStream());
		// }
		// catch (IOException e)
		// {
		// e.printStackTrace();
		// }
		// while (s.isClosed())
		// {
		// CFG.p("Connection closed: " + s.getInetAddress().getHostAddress().toString());
		// clients.remove(s);
		// }
	}
}
