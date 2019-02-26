package com.android.util;

import java.io.*;

public class Data
{
	public static String javaccmd,dexcmd,loadcmd,copy,dir,def,apk,data,fn,script,program,dex,srcdir,outdir,tmpdir,classname;
	public static boolean run;

	public static String path()
	{
		String s=
			"export data=" + data + "\n" +
			"export apk=" + apk + "\n" +
			"export dir=" + dir + "\n" +
			"export PATH=$data/bin:$PATH\n" +
			"bsh(){\n" +
			"dalvikvm -Xmx200M -cp $apk com.android.util.BSH ${*}\n" +
			"}\n" +
			"javac(){\n" +
			"dalvikvm -Xmx200M -cp $apk com.android.util.Javac ${*}\n" +
			"}\n" +
			"dex(){\n" +
			"dalvikvm -Xmx200M -cp $apk com.android.util.Dex ${*}\n" +
			"}\n" +
			"load(){\n" +
			"dalvikvm -Xmx200M -cp $apk com.android.util.ClassLoader ${*}\n" +
			"}\n" +
			"replace(){\n" +
			"dalvikvm -Xmx200M -cp $apk com.android.util.Replacer ${*}\n" +
			"}\n" +
			"cd $dir\n";
		return s;
	}

	public static void java(final String fn)
	{
		if (fn.contains("/src/"))
		{
			srcdir = fn.substring(0, fn.indexOf("/src") + "/src".length());
		}
		else
		{
			srcdir = fn;
		}
		String libs=new File(srcdir).getParent() + "/libs";
		if (new File(libs).exists())
		{
			String classpath=IO.classpath(libs);
			if (!classpath.isEmpty())
			{
				javaccmd = "-source 1.5 -target 1.5 -nowarn -noExit -cp /sdcard/.aide/android.jar:" + classpath + " -d " + outdir + " " + srcdir;
				dexcmd = "--dex --no-strict --output=" + dex + " " + outdir + " " + IO.search(".jar", libs);
			}
		}
		else
		{
			javaccmd = "-source 1.5 -target 1.5 -nowarn -noExit -cp /sdcard/.aide/android.jar -d " + outdir + " " + srcdir;
			dexcmd = "--dex --no-strict --output=" + dex + " " + outdir;
		}
		loadcmd = "load " + dex + " " + IO.classname(fn);
	}

	public static void pause()
	{

		if (!new File(dex).exists())
		{
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{}
			pause();
		}
	}
}
