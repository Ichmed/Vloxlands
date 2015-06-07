package com.vloxlands.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipAssistant extends Thread {
	public static final byte[] BUFFER = new byte[0xFFFF];
	
	public int progress;
	public int fullsize;
	public int speed;
	private long time;
	public String state;
	ArrayList<URL> urls = new ArrayList<>();
	ArrayList<File> dests = new ArrayList<>();
	
	public ZipAssistant() {
		progress = 0;
		fullsize = 0;
	}
	
	public void addDownload(URL url, File dest, boolean kill) {
		if (kill) Assistant.deleteFolder(dest);
		urls.add(url);
		dests.add(dest);
		dest.mkdirs();
	}
	
	public boolean hasDownloads() {
		return dests.size() > 0;
	}
	
	@Override
	public void run() {
		try {
			state = "Herunterladen";
			for (URL u : urls)
				fullsize += u.openConnection().getContentLength();
			
			time = System.currentTimeMillis();
			
			for (int i = 0; i < dests.size(); i++) {
				InputStream in = new BufferedInputStream(urls.get(i).openStream(), 1024);
				File zip = new File(dests.get(i), "download.zip");
				zip.createNewFile();
				OutputStream out = new BufferedOutputStream(new FileOutputStream(zip));
				copyInputStream(in, out);
				out.close();
			}
			
			
			state = "Entpacken";
			for (File f : dests) {
				unzip(new File(f, "download.zip"), f);
				new File(f, "download.zip").delete();
			}
			
			state = "Fertig";
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static File unzip(File zip, File dest) {
		
		try {
			ZipFile zipFile = new ZipFile(zip);
			for (ZipEntry entry : Collections.list(zipFile.entries())) {
				extractEntry(zipFile, entry, dest.getPath().replace("\\", "/"));
			}
			zipFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return zip;
	}
	
	public static void extractEntry(ZipFile zipFile, ZipEntry entry, String destDir) throws IOException {
		File file = new File(destDir + "/" + entry.getName().replace("\\", "/"));
		if (entry.isDirectory()) file.mkdirs();
		else {
			file.getParentFile().mkdirs();
			InputStream is = null;
			OutputStream os = null;
			try {
				is = zipFile.getInputStream(entry);
				os = new FileOutputStream(file);
				for (int len; (len = is.read(BUFFER)) != -1;) {
					os.write(BUFFER, 0, len);
				}
			} finally {
				if (os != null) os.close();
				if (is != null) is.close();
			}
		}
	}
	
	public void copyInputStream(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int len = in.read(buffer);
		while (len >= 0) {
			out.write(buffer, 0, len);
			progress += len;
			speed = (int) (progress / ((System.currentTimeMillis() - time) / 1000f));
			len = in.read(buffer);
		}
		in.close();
		out.close();
	}
}
