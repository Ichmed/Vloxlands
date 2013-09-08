package com.vloxlands.net.tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

import com.vloxlands.settings.CFG;

/**
 * @author Dakror
 */
public class Client extends Thread
{
	Socket s;
	DataInputStream in;
	DataOutputStream out;
	
	public Client()
	{}
	
	
	public Client(Socket s)
	{
		this.s = s;
		try
		{
			in = new DataInputStream(s.getInputStream());
			out = new DataOutputStream(s.getOutputStream());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * @return -1 for already connected to a server<br>
	 *         0 for can't reach server<br>
	 *         1 for ok<br>
	 *         2 for ERROR
	 */
	public int connectToServer(InetAddress ip)
	{
		if (s != null)
		{
			System.err.println("Client already connected to a server. Disconnect first!");
			return -1;
		}
		
		try
		{
			s = new Socket(ip, CFG.SERVER_PORT);
			in = new DataInputStream(s.getInputStream());
			out = new DataOutputStream(s.getOutputStream());
		}
		catch (ConnectException e)
		{
			return 0;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 2;
		}
		return 1;
	}
	
	public void writeData(byte[] data)
	{
		try
		{
			out.write(data);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
