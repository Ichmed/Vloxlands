package com.vloxlands.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import com.vloxlands.settings.CFG;

/**
 * @author Dakror
 */
public class NetworkAssistant {
	public static InetAddress getMyHamachiIP() {
		try {
			if (!CFG.INTERNET) return InetAddress.getLocalHost();
			
			List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface ni : interfaces) {
				if (ni.getDisplayName().equals("Hamachi Network Interface")) return ni.getInetAddresses().nextElement();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
