package com.android.util;

import java.io.*;
import java.util.*;
import java.util.zip.*;

public class Unzip
{

	// ============================================
	// unzip
	// ============================================

	public static void unzip(String from, String to)
	{
		main((from + " " + to).split(" "));
	}
	// ============================================
	// main
	// ============================================

	public static void main(String[] args)
	{
		try
		{
			String szZipFilePath = args[0];
			String szExtractPath = args[1];
			int i;
			ZipFile zf;
			Vector zipEntries = new Vector();
			try
			{
				zf = new ZipFile(szZipFilePath);
				Enumeration en = zf.entries();
				while (en.hasMoreElements())
				{
					zipEntries.addElement(en.nextElement());
				}
				for (i = 0; i < zipEntries.size(); i++)
				{
					ZipEntry ze = (ZipEntry)zipEntries.elementAt(i);
					//publishProgress(i, zipEntries.size());
					//a.size = (i + " / " + zipEntries.size());
					extractFromZip(szZipFilePath, szExtractPath, ze.getName(), zf, ze);
				}
				zf.close();
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	// ============================================
	// extractFromZip
	// ============================================
	static void extractFromZip(String szZipFilePath, String szExtractPath, String szName, ZipFile zf, ZipEntry ze)
	{
		if (ze.isDirectory()) return;
		String szDstName = slash2sep(szName);
		String szEntryDir;
		if (szDstName.lastIndexOf(File.separator) != -1)
		{
			szEntryDir = szDstName.substring(0, szDstName.lastIndexOf(File.separator));
		}
		else
			szEntryDir = "";
		try
		{
			File newDir = new File(szExtractPath + File.separator + szEntryDir);
			newDir.mkdirs();
			FileOutputStream fos = new FileOutputStream(szExtractPath + File.separator + szDstName);
			InputStream is = zf.getInputStream(ze);
			byte[] buf = new byte[1025 * 50];
			int nLength;
			while (true)
			{
				try
				{
					nLength = is.read(buf);
				}
				catch (EOFException ex)
				{
					break;
				}
				if (nLength < 0) break;
				fos.write(buf, 0, nLength);
			}
			is.close();
			fos.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	// ============================================
	// slash2sep
	// ============================================
	static String slash2sep(String src)
	{
		int i;
		char[] chDst = new char[src.length()];
		String dst;
		for (i = 0; i < src.length(); i++)
		{
			if (src.charAt(i) == '/')
				chDst[i] = File.separatorChar;
			else
				chDst[i] = src.charAt(i);
		}
		dst = new String(chDst);
		return dst;
	}
}
