package com.android.util;

public class Javac
{
	public static void main(String[] args)
	{
		org.eclipse.jdt.internal.compiler.batch.Main.main(args);
	}

	public static boolean javac(String cmd)
	{
		boolean flag=false;
		try
		{
			main(cmd.split(" "));
			flag = true;
		}
		catch (Exception e)
		{

		}
		return flag;
	}
}
