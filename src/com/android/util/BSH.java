package com.android.util;

public class BSH
{
	public static void main(String[] args)
	{
		bsh.Interpreter.main(args);
	}

	public static boolean bsh(String cmd)
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
