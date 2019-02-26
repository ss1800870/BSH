package com.android.util;

import java.io.*;

public class Replacer
{
	public static int i=0;
	public static void main(String[] args)
	{
		if (args.length >= 3)
		{
			String from = args[0];
			String to = args[1];
			int t=0;
			for (String a:args)
			{
				if (t < 2)
				{
					t++;
					continue;
				}
				replacer(a, from, to);
				t++;
			}
		}
		else
		{
			System.out.println("[ USAGE: ] replace <\"from\"> <\"to\"> <\"dir1\"> <\"dir2\"> <...>");
		}
	}
	public static void replacer(String dir, String from, String to)
	{
		File[] ff=new File(dir).listFiles();
		if (ff != null)
		{
			for (File f:ff)
			{
				if (f.isFile())
				{
					String fn=f.getAbsolutePath();
					System.out.println("[" + i + "] " + fn);
					String r=read(fn);
					if (r.contains(from))
					{
						write(fn, r.replace(from, to));
					}
					i++;
				}
				else
				{
					String fn=f.getAbsolutePath();
					replacer(fn, from, to);
				}
			}
		}
	}
	
	public static String read(String fn)
	{
		String s="";
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		try
		{
			FileInputStream fis=new FileInputStream(new File(fn));
			int i=0;
			while ((i = fis.read()) != -1)
			{
				baos.write(i);
			}
			baos.flush();
			s = new String(baos.toByteArray());
			fis.close();
			baos.close();
		}
		catch (Exception e)
		{}
		return s;
	}
	
	public static void write(String fn, String s)
	{
		try
		{
			FileOutputStream fos=new FileOutputStream(new File(fn));
			fos.write(s.getBytes());
			fos.flush();
			fos.close();
		}
		catch (Exception e)
		{}
	}
}
