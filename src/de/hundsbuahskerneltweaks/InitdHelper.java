package de.hundsbuahskerneltweaks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.os.SystemClock;
import de.hundsbuahskerneltweaks.MainActivity.enOnBoot;

public class InitdHelper
{
	private MainActivity ma;
	private AndroidBash ab;
    private List<String> initd_content = new ArrayList<String>();
    private List<String> initd_activated_check = new ArrayList<String>();

    public InitdHelper(MainActivity ma)
	{
		this.ma = ma;
		ab = new AndroidBash(ma);
	}
	
	public int updateInitd(String parameter, String kernel_interface, enOnBoot status)
	{
        InputStream initd_inputstream = null;
		InputStreamReader initd_inputstreamreader = null;
		BufferedReader buffreader = null;
		BufferedWriter initd_bufferedwriter = null;
		String line = "";
		initd_content.clear();
		
		try
		{
			ab.writeSuCommand("busybox mount -o remount,rw /dev/block/mmcblk0p1 /system");
			SystemClock.sleep(200);
			ab.writeSuCommand("chmod 777 /system/etc/init.d/99hundsapp");
			SystemClock.sleep(50);
			
			checkIfInitdFileExists();

			initd_inputstream = new FileInputStream("/system/etc/init.d/99hundsapp");
			initd_inputstreamreader = new InputStreamReader(initd_inputstream);
			buffreader = new BufferedReader(initd_inputstreamreader);
			do
			{
				line = buffreader.readLine();
				if(line != null)
				{
					initd_content.add(line);
				}
		  }
		  while(line != null);
		}
		catch(Exception e)
		{
			ma.showMessageBox("Error updating init.d file!", 0);
			return 1;
		}
		finally
		{
			// close the file.
			try
			{
				initd_inputstream.close();
			}
			catch (Exception e)
			{
				ma.showMessageBox("Cant close file input stream for initd file!", 0);
				return 1;
			}
		}
		
		switch (status)
		{
			case DISABLE:
			{
				int index = searchForInterface(kernel_interface);
				if(index != -1)
				{
					if(!initd_content.get(index).contains("#"))
						initd_content.set(index, "#echo \"" + parameter + "\" > " + kernel_interface);
					else
						ma.showMessageBox(kernel_interface + " is already deactivated!", 0);
				}
				else
				{
					initd_content.add("#echo \"" + parameter + "\" > " + kernel_interface);
				}
				break;
			}
			case ENABLE:
			{
				int index = searchForInterface(kernel_interface);
				if(index != -1)
				{
					if(initd_content.get(index).contains("#"))
						initd_content.set(index, "echo \"" + parameter + "\" > " + kernel_interface);
					else
						ma.showMessageBox(kernel_interface + " is already activated!", 0);
				}
				else
				{
					initd_content.add("echo \"" + parameter + "\" > " + kernel_interface);
				}
				break;
			}
		}
		try
		{
			ab.writeSuCommand("chmod 777 /system/etc/init.d/99hundsapp");
			SystemClock.sleep(50);
			initd_bufferedwriter = new BufferedWriter(new FileWriter(new File("/system/etc/init.d/99hundsapp")));
			
			for(int i = 0; i < initd_content.size(); i++)
			{
				initd_bufferedwriter.write(initd_content.get(i) + "\n");
			}
			initd_bufferedwriter.flush();
		}
		catch(Exception e)
		{
			ma.showMessageBox("Cant write to initd file!", 0);
			return 1;
		}
		finally
		{
			// close the file.
			try
			{
				initd_bufferedwriter.close();
			}
			catch (IOException e)
			{
				ma.showMessageBox("Cant close file output stream for initd file!", 0);
				return 1;
			}
		}

		
		return 0;
	}
	
	int searchForInterface(String kernel_interface)
	{
		int index = 0;
		
		for(index = 0; index < initd_content.size(); index++)
		{
			if(initd_content.get(index).contains(kernel_interface))
			{
				return index;
			}
		}
		return -1;
	}
	
	public int checkIfInterfaceIsActivatedOnBoot(String kernel_interface)
	{
        InputStream initd_inputstream = null;
		InputStreamReader initd_inputstreamreader = null;
		BufferedReader buffreader = null;
		String line = "";
		initd_activated_check.clear();
		
		try
		{
			ab.writeSuCommand("busybox mount -o remount,rw /dev/block/mmcblk0p1 /system");
			SystemClock.sleep(200);
			ab.writeSuCommand("chmod 777 /system/etc/init.d/99hundsapp");
			SystemClock.sleep(50);
			
			initd_inputstream = new FileInputStream("/system/etc/init.d/99hundsapp");
			initd_inputstreamreader = new InputStreamReader(initd_inputstream);
			buffreader = new BufferedReader(initd_inputstreamreader);
			do
			{
				line = buffreader.readLine();
				if(line != null)
				{
					initd_activated_check.add(line);
				}
		  }
		  while(line != null);
		}
		catch(Exception e)
		{
			ma.showMessageBox("Error reading init.d file!", 0);
			return 1;
		}
		finally
		{
			// close the file.
			try
			{
				initd_inputstream.close();
			}
			catch (Exception e)
			{
				ma.showMessageBox("Cant close file input stream for initd file!", 0);
				return 1;
			}
		}
		
		for(int index = 0; index < initd_activated_check.size(); index++)
		{
			if(initd_activated_check.get(index).contains(kernel_interface) && initd_activated_check.get(index).contains("#"))
				return 0;
			else if(initd_activated_check.get(index).contains(kernel_interface) && !initd_activated_check.get(index).contains("#"))
				return 1;
		}
		return -1;
	}

	
	private void checkIfInitdFileExists()
	{
		File initd = new File("/system/etc/init.d/99hundsapp");
		if(initd.exists() == false)
		{
			ab.writeSuCommand("echo \"\" > /system/etc/init.d/99hundsapp");
			SystemClock.sleep(200);
			ab.writeSuCommand("chmod 777 /system/etc/init.d/99hundsapp");
			initd_content.add("#!/system/bin/sh\n");
		}
	}
}
