package de.hundsbuahskerneltweaks;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import de.hundsbuahskerneltweaks.MainActivity.enOnBoot;

public class Governor_IOscheduler
{
	private MainActivity ma;
	private AndroidBash ab;
	private InitdHelper initd;
	public List<String> governors = new ArrayList<String>();
	public List<String> ioschedulers = new ArrayList<String>();
	final public String ioscheduler_0 = "/sys/block/mmcblk0/queue/scheduler";
	final public String ioscheduler_1 = "/sys/block/mmcblk1/queue/scheduler";
	final public String governor = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor";
	
	public Governor_IOscheduler(MainActivity ma)
	{
		this.ma = ma;
		ab = new AndroidBash(ma);
		initd = new InitdHelper(ma);
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
	
	public int saveToInitdFile(enOnBoot status)
	{
		try
		{
			if(initd.updateInitd(ma.available_ioschedulers.getSelectedItem().toString().replace("[", "").replace("]", ""), ioscheduler_0, status) == 0)
				if(initd.updateInitd(ma.available_ioschedulers.getSelectedItem().toString().replace("[", "").replace("]", ""), ioscheduler_1, status) == 0)
					if(initd.updateInitd(ma.available_governors.getSelectedItem().toString(), governor, status) == 0)
						return 0;
			return 1;
		}
		catch (Exception e)
		{
			ma.showMessageBox("Cant write init.d file!", 0);
			return 1;
		}
	}
}
