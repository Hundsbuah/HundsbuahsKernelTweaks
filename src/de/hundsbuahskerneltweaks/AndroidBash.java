package de.hundsbuahskerneltweaks;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AndroidBash
{
	final int BUFFER_SIZE = 4096;
	
    private Process su_bash = null;
    private Process normal_bash = null;
    private DataOutputStream su_bash_outputstream = null;
    private InputStream su_bash_inputstream = null;
    private DataOutputStream normal_bash_outputstream = null;
    private InputStream normal_bash_inputstream = null;
    private MainActivity ma;
    
    public AndroidBash(MainActivity ma)
    {
    	getNormalBash();
    	getSuBash();
    	this.ma = ma;
    }
    
	public void getSuBash()
	{
		int process_result = 0;
		try
		{
			su_bash = Runtime.getRuntime().exec(new String[]{"su", "-c", "/system/bin/sh"});
			while(su_bash == null);
			try
			{
				process_result = su_bash.exitValue();
			}
			catch(IllegalThreadStateException e)
			{
				e.printStackTrace();
			}
			if(process_result == 0)
			{
				/* TODO: Wait for granted su rights */
		    }
			su_bash_outputstream = new DataOutputStream(su_bash.getOutputStream());
			su_bash_inputstream = su_bash.getInputStream();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void getNormalBash()
	{
		int process_result = 0;
		try
		{
			normal_bash = Runtime.getRuntime().exec(new String[]{"/system/bin/sh"});
			while(normal_bash == null);
			try
			{
				process_result = normal_bash.exitValue();
			}
			catch(IllegalThreadStateException e)
			{
				e.printStackTrace();
			}
			if(process_result == 0)
			{
				/* TODO: What is when the normal bash fails? */
		    }
			normal_bash_outputstream = new DataOutputStream(normal_bash.getOutputStream());
			normal_bash_inputstream = normal_bash.getInputStream();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void writeSuCommand(String str)
	{
		try
		{
			su_bash_outputstream.writeBytes(str + "\n");
			su_bash_outputstream.flush();
		}
		catch (Exception e)
		{
			ma.showMessageBox("Cant write su bash command!", 0);
		}
	}
	
	public String readSuCommand(String str, int wait)
	{
		String str_ret = new String();
		try
		{
			su_bash_outputstream.writeBytes(str + "\n");
			su_bash_outputstream.flush();
						
			int timeout = 0xFFFF;
			while((su_bash_inputstream.available() == 0) && (--timeout != 0));
			if(timeout == 0)
			{
				if(wait == 1)
					ma.showMessageBox("read timeout - su rights? hundsbuah kernel version >= 2.2?", 0);
				return "";
			}
			byte[] buffer = new byte[BUFFER_SIZE];
			int read = 0;

			while(true)
			{

				read = su_bash_inputstream.read(buffer);

			    str_ret += new String(buffer, 0, read-1);
			    if(read < BUFFER_SIZE)
			    {
			        break;
			    }
			}
		}
		catch (Exception e)
		{
			ma.showMessageBox("Cant read/write su command!", 0);
		}
		
		return str_ret;
	}
	
	public String readCommand(String str, int wait)
	{
		String str_ret = new String();

		try
		{
			normal_bash_outputstream.writeBytes(str + "\n");
			normal_bash_outputstream.flush();
			
			int timeout = 0xFFFF;
			while((normal_bash_inputstream.available() == 0) && (--timeout != 0));
			if(timeout == 0)
			{
				if(wait == 1)
					ma.showMessageBox("read timeout - su rights? hundsbuah kernel version >= 2.2?", 0);
				return "";
			}
			byte[] buffer = new byte[BUFFER_SIZE];
			int read = 0;

			while(true)
			{

				read = normal_bash_inputstream.read(buffer);

			    str_ret += new String(buffer, 0, read-1);
			    if(read < BUFFER_SIZE)
			    {
			        break;
			    }
			}
		}
		catch (Exception e)
		{
			ma.showMessageBox("Cant read/write normal bash command!", 0);
		}
		
		return str_ret;
	}
}
