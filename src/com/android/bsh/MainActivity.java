package com.android.bsh;

import android.app.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.android.fm.*;
import com.android.util.*;
import java.io.*;

public class MainActivity extends Activity 
{
	EditText ed;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		ed = (EditText)findViewById(R.id.ed);
		init();
    }

	public void install(String apk)
	{
		startActivity(new Intent().setAction(Intent.ACTION_VIEW).setDataAndType(Uri.fromFile(new File(apk)), "application/vnd.android.package-archive"));
		finish();
		System.exit(0);
	}

	public void init()
	{
		Data.data = getFilesDir().toString();
		Data.def = Data.data + "/tmp/default";
		Data.dex = Data.data + "/classes.zip";
		Data.srcdir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/BSH";
		Data.tmpdir = Data.data + "/tmp";
		Data.outdir = Data.tmpdir + "/classes";
		Data.apk = getPackageResourcePath();
		Data.fn = IO.read(Data.def);
		Data.script = Data.tmpdir + "/script.sh";
		if (!new File(Data.srcdir).exists())
		{
			IO.mkdirs(Data.srcdir);
		}
		if (!new File(Data.data + "/bin/bash").exists())
		{
			copyFromAssets("data.zip", Data.data + "/data.zip");
			Unzip.unzip(Data.data + "/data.zip", Data.data);
			sh("chmod -R 0777 " + Data.data + "/bin");
			IO.delete(Data.data + "/data.zip");
		}
		if (Data.fn.isEmpty())
		{
			Data.fn = Data.srcdir + "/test.bsh";
			IO.write(Data.def, Data.fn);
			new File(Data.tmpdir).mkdirs();
			new File(Data.outdir).mkdirs();
			new File(Data.srcdir).mkdirs();
			sh("chmod -R 0777 " + Data.data + "/bin");
			sh("chmod -R 0777 " + Data.tmpdir);
		}
		Intent i = this.getIntent();
		if (i.getAction().equals(Intent.ACTION_VIEW))
		{
			Data.fn = i.getData().getPath();
			IO.write(Data.def, Data.fn);
		}
		if (Data.fn.endsWith(".apk"))
		{
			install(Data.fn);
		}
		else
		{
			ed.setText(IO.read(Data.fn));
		}

		ed.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		ed.setTextSize(20);

		Data.dir = new File(Data.fn).getParent();
	}

	public Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			String result=(String)msg.obj;
			ed.append(result);
		}
	};

	public void run()
	{
		Data.program = ed.getText().toString();
		if (!Data.program.equals(IO.read(Data.fn)))
		{
			if (Data.fn.endsWith(".java"))
			{
				IO.delete(Data.dex);
				IO.delete(Data.outdir);
			}
			IO.write(Data.fn, Data.program);
		}
		startActivity(new Intent(this, RunActivity.class).setAction(Intent.ACTION_VIEW).setData(Uri.fromFile(new File(Data.fn))));
		finish();
	}

	public void copyFromAssets(String from, String to)
	{
		try
		{
			AssetManager am = getAssets();
			InputStream is=am.open(from);
			OutputStream os=new FileOutputStream(to);
			byte[] buffer=new byte[1025 * 20];
			int i;
			while ((i = is.read(buffer)) != -1)
			{
				os.write(buffer, 0, i);
			}
			is.close();
			os.flush();
			os.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed()
	{
		run();
	}

	public String sh(String cmd)
	{
		IO.write(Data.script, cmd);
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		try
		{
			java.lang.Process p= Runtime.getRuntime().exec("/system/bin/sh " + Data.script);
			InputStream out=p.getInputStream();
			InputStream err=p.getErrorStream();
			p.waitFor();
			int i=0;
			while ((i = out.read()) != -1)
			{
				baos.write(i);
			}
			while ((i = err.read()) != -1)
			{
				baos.write(i);

			}
		}
		catch (Exception e)
		{
			try
			{
				baos.write(e.toString().getBytes());
			}
			catch (IOException ex)
			{}
		}
		return new String(baos.toByteArray());
	}

	public void open()
	{
		startActivity(new Intent(this, FileBrowser.class).setAction(Intent.ACTION_VIEW).setData(Uri.fromFile(new File(Data.fn))));
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		if (id == R.id.run)
		{
			run();
			return true;
		}
		if (id == R.id.exit)
		{
			finish();
			System.exit(0);
			IO.eval("kill $#");
			return true;
		}
		if (id == R.id.open)
		{
			open();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
