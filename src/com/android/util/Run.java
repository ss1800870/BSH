package com.android.util;

import android.os.*;
import com.android.bsh.*;
import java.io.*;
import java.util.*;

public class Run extends Thread implements Runnable
{
	ByteArrayOutputStream baos;
	RunActivity ra;
	static boolean done;
	static java.lang.Process p;
	static InputStream out;
	static InputStream err;
	static OutputStream in;

	public Run(RunActivity ra)
	{
		try
		{
			this.ra = ra;
			this.p = Runtime.getRuntime().exec(Data.data + "/bin/bash");
			this.in = p.getOutputStream();
			this.out = p.getInputStream();
			this.err = p.getErrorStream();
		}
		catch (Exception e)
		{}
	}

	public void run()
	{
		String cmd="";
		Data.run = true;
		done = false;
		ra.ed.setText("");
		try
		{
			if (Data.fn.endsWith(".java"))
			{

				System.setOut(new PrintStream(new FileOutputStream(new File(Data.data + "/out"))));
				System.setErr(new PrintStream(new FileOutputStream(new File(Data.data + "/err"))));
				Data.java(Data.fn);
				if (!new File(Data.dex).exists())
				{
					if (Javac.javac(Data.javaccmd))
					{
						scan(Data.data + "/out");
					}
					if (IO.read(Data.data + "/err").isEmpty())
					{
						if (Dex.dex(Data.dexcmd))
						{
							scan(Data.data + "/out");
							cmd = Data.loadcmd;
						}
						else
						{
							scan(Data.data + "/err");
							cmd = "echo;echo DEX ERROR !!!";
						}
					}
					else
					{
						scan(Data.data + "/err");
						cmd = "echo;echo JAVAC ERROR !!!";
					}
				}
				else
				{
					cmd = Data.loadcmd;
				}
			}
			else if (Data.fn.endsWith(".bsh"))
			{
				System.setOut(new PrintStream(Data.data + "/out"));
				System.setErr(new PrintStream(Data.data + "/out"));
				BSH.bsh(Data.fn);
				scan(Data.data + "/out");
			}
			else
			{
				cmd = "bash " + Data.script;
			}
			eval(Data.path() + cmd);
		}
		catch (Exception e)
		{
			print(e.toString());
		}
		finally
		{
			Data.run = false;
		}
	}

	public void print(String s)
	{
		ra.handler.sendMessage(Message.obtain(ra.handler, 0, s));
		pause(20);
	}

	public void pause(long time)
	{
		try
		{
			Thread.sleep(time);
		}
		catch (Exception e)
		{}
	}

	public void eval(String cmd)
	{
		baos = new ByteArrayOutputStream();
		done = false;
		try
		{
			exec(cmd);
			exec("exit");
			int i=0;
			while ((i = out.read()) != -1)
			{
				baos.write(i);
				if (i == 10)
				{
					print(new String(baos.toByteArray()));
					baos = new ByteArrayOutputStream();
				}
			}
			while ((i = err.read()) != -1)
			{
				baos.write(i);
				if (i == 10)
				{
					print(new String(baos.toByteArray()));
					baos = new ByteArrayOutputStream();
				}
			}
			print(new String(baos.toByteArray()));
			p.waitFor();
		}
		catch (Exception e)
		{
			try
			{
				baos.write(e.toString().getBytes());
			}
			catch (IOException ex)
			{}
			done = true;
		}
		done = true;
	}

	public static void exec(String cmd)
	{
		try
		{
			in.write((cmd + "\n").getBytes());
			in.flush();
		}
		catch (IOException e)
		{}
	}

	public void scan(String fn)
	{
		try
		{
			System.setIn(new FileInputStream(new File(fn)));
			Scanner input = new Scanner(System.in);
			String s="";
			while (true)
			{
				s = input.nextLine();
				if (s.isEmpty() || s.equals(null))
				{
					break;
				}
				print(s + "\n");
			}
			print("\n");
		}
		catch (Exception e)
		{}
	}
}
