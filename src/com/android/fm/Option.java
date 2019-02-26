package com.android.fm;

public class Option implements Comparable<Option>
{

	private String name;
	private String data;
	private String path;
	private long date;

	public Option(String n, String d, String p, long y)
	{
		name = n;
		data = d;
		path = p;
		date = y;
	}

	public String getName()
	{
		return name;
	}

	public String getData()
	{
		return data;
	}

	public String getPath()
	{
		return path;
	}

	public long getDate()
	{
		return date;
	}

	//@Override
	public int compareTo(Option o)
	{
		if (this.name != null)
			return this.name.toLowerCase().compareTo(o.getName().toLowerCase()); 
		else 
			throw new IllegalArgumentException();
	}
}
