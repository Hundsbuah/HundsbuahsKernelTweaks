package de.hundsbuahskerneltweaks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.os.SystemClock;

public class Governor_IOscheduler
{
	private MainActivity ma;
	private AndroidBash ab;
	public List<String> governors = new ArrayList<String>();
	public List<String> ioschedulers = new ArrayList<String>();
	private List<String> initdappfilecontent = new ArrayList<String>();
	
	public Governor_IOscheduler(MainActivity ma)
	{
		this.ma = ma;
		ab = new AndroidBash(ma);
	}
	
	public int setGovernor()
	{
		if(ma.available_governors.getSelectedItem() != null)
		{
			ab.writeSuCommand("echo " + ma.available_governors.getSelectedItem().toString() + " > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor");
			return 0;
		}
		else
			return 1;
	}
	
	public int setIOScheduler()
	{
		if(ma.available_ioschedulers.getSelectedItem() != null)
		{
			ab.writeSuCommand("echo " + ma.available_ioschedulers.getSelectedItem().toString() + " > /sys/block/mmcblk0/queue/scheduler");
			ab.writeSuCommand("echo " + ma.available_ioschedulers.getSelectedItem().toString() + " > /sys/block/mmcblk1/queue/scheduler");
			return 0;
		}
		else
			return 1;
	}
	
	public int getGovernors()
	{
		governors.clear();
		String available_governors = ab.readSuCommand("cat /sys/devices/system/cpu/cpu0/cpufreq/scaling_available_governors", 1);
		
		if(available_governors.isEmpty())
			return 1;
     
		String[] governors_split = available_governors.split(" ");
   		
		for(int i = 0; i < governors_split.length; i++)
		{
			governors.add(governors_split[i]);

		}
		ma.addListToSpinner(ma.available_governors, governors);
		ma.available_governors.setSelection(governors.indexOf(ab.readSuCommand("cat /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor", 1).split(" ")[0]));
		return 0;
	}
	public int getIOSchedulers()
	{
		ioschedulers.clear();
		String available_ioschedulers = ab.readSuCommand("cat /sys/block/mmcblk0/queue/scheduler", 1);
		
		if(available_ioschedulers.isEmpty())
			return 1;
     
		String[] ioschedulers_split = available_ioschedulers.split(" ");
   		
		for(int i = 0; i < ioschedulers_split.length; i++)
		{
			ioschedulers.add(ioschedulers_split[i].replace("[", "").replace("]", ""));

		}
		ma.addListToSpinner(ma.available_ioschedulers, ioschedulers);
		int num_ioschedulers = ab.readSuCommand("cat /sys/block/mmcblk0/queue/scheduler", 1).split(" ").length;
		String[] io_schedulers_split = ab.readSuCommand("cat /sys/block/mmcblk0/queue/scheduler", 1).split(" ");
		for(int idx = 0; idx < num_ioschedulers; idx++)
		{
			if(io_schedulers_split[idx].contains("[") && io_schedulers_split[idx].contains("]"))
				ma.available_ioschedulers.setSelection(ioschedulers.indexOf(ab.readSuCommand("cat /sys/block/mmcblk0/queue/scheduler", 1).split(" ")[idx].replace("[", "").replace("]", "")));
		}
		
		return 0;
	}
	
	public int saveToInitdFile()
	{
		BufferedWriter initdappfile_bufferedwriter = null;
		initdappfilecontent.clear();
		
		ab.writeSuCommand("busybox mount -o remount,rw /dev/block/mmcblk0p1 /system");
		SystemClock.sleep(200);
		try
		{

			ab.writeSuCommand("rm -f /system/etc/init.d/99hundsapp");
			ab.writeSuCommand("echo \"\" > /system/etc/init.d/99hundsapp");
			SystemClock.sleep(200);
			ab.writeSuCommand("chmod 777 /system/etc/init.d/99hundsapp");
			SystemClock.sleep(50);
			initdappfile_bufferedwriter = new BufferedWriter(new FileWriter(new File("/system/etc/init.d/99hundsapp")));
			
			initdappfile_bufferedwriter.write("#!/system/bin/sh\n");
			initdappfile_bufferedwriter.write("echo " + ma.available_ioschedulers.getSelectedItem().toString().replace("[", "").replace("]", "") + " > /sys/block/mmcblk0/queue/scheduler\n");
			initdappfile_bufferedwriter.write("echo " + ma.available_ioschedulers.getSelectedItem().toString().replace("[", "").replace("]", "") + " > /sys/block/mmcblk1/queue/scheduler\n");
			initdappfile_bufferedwriter.write("echo " + ma.available_governors.getSelectedItem().toString() + " > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor");
			
			return 0;
		}
		catch (Exception e)
		{
			ma.showMessageBox("Cant write init.d file!", 0);
			return 1;
		}
		finally
		{
			// close the file.
			try
			{
				initdappfile_bufferedwriter.close();
				return 0;
			}
			catch (Exception e)
			{

			}
		}
	}
	
	public int deleteInitdFile()
	{
		ab.writeSuCommand("busybox mount -o remount,rw /dev/block/mmcblk0p1 /system");
		SystemClock.sleep(200);
		ab.writeSuCommand("rm -f /system/etc/init.d/99hundsapp");
		SystemClock.sleep(200);

		File checkinitd_file = new File("/system/etc/init.d/99hundsapp");

		if(checkinitd_file.exists())
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
}
