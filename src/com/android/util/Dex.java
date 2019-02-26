package com.android.util;

public class Dex
{
	public static void main(String[] args)
	{
		com.android.dx.command.Main.main(args);
	}

	public static boolean dex(String cmd)
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
