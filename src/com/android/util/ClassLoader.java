package com.android.util;

import dalvik.system.*;
import java.io.*;
import java.lang.reflect.*;

public class ClassLoader extends DexClassLoader
{
	public ClassLoader(String dex)
	{
		super(dex, new File(dex).getParent(), new File(dex).getParent(), getSystemClassLoader());
	}

	public static void main(String[] args)
	{
		if (args.length >= 2)
		{
			String arg="";
			for (int i=0;i < args.length;i++)
			{
				if (i < 2)
				{
					continue;
				}
				arg += args[i] + " ";
			}
			new ClassLoader(args[0]).load(args[1], arg);
		}
	}

	public static void run(String dex, String classname, String arg)
	{
		new ClassLoader(dex).load(classname, arg);
	}

	public void load(String classname, String arg)
	{
		try
		{
			Class c=super.loadClass(classname);
			boolean flag=false;
			Method[] mm=c.getDeclaredMethods();
			for (Method m:mm)
			{
				if (m.getName().equals("main"))
				{
					flag = true;
					break;
				}
			}
			if (flag == true)
			{
				if (!arg.isEmpty())
				{
					c.getMethod("main", String[].class).invoke(new Object(), new Object[]{arg.split(" ")});
				}
				else
				{
					c.getMethod("main", String[].class).invoke(new Object(), new Object[]{new String[0]});
				}
			}
			else
			{
				c.newInstance();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static boolean load(String arg)
	{
		main(arg.split(" "));
		return true;
	}
}
