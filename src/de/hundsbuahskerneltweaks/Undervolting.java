package de.hundsbuahskerneltweaks;

import java.util.ArrayList;
import java.util.List;

import android.os.SystemClock;


import de.hundsbuahskerneltweaks.MainActivity.enCore;
import de.hundsbuahskerneltweaks.MainActivity.enOnBoot;

public class Undervolting
{
    private MainActivity ma;
    private AndroidBash ab;
    private InitdHelper initd;
	public List<String> cpu_frequencies = new ArrayList<String>();
	public List<String> cpu_voltages = new ArrayList<String>();
	public List<String> gpu_frequencies = new ArrayList<String>();
	public List<String> gpu_voltages = new ArrayList<String>();

	public Undervolting(MainActivity ma)
    {
    	this.ma = ma;
    	ab = new AndroidBash(this.ma);
    	initd = new InitdHelper(this.ma);
    }
    
    public int getNumberOfFrequencies(enCore core)
    {
    	switch(core)
    	{
	    	case CPU:
	    	{
	    		SystemClock.sleep(50);
	    		String[] cpu_freqlist = ab.readSuCommand("cat /sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies", 1).split(" ");
	    		if(cpu_freqlist.length == 0)
	    			return -1;
	    		else
	    			return cpu_freqlist.length;
	    	}
	    	case GPU:
	    	{
	    		String[] gpu_freqlist = ab.readSuCommand("cat /sys/kernel/tegra_cap/gpu_freqs", 1).split(" ");
	    		if(gpu_freqlist.length == 0)
	    			return -1;
	    		else
	    			return gpu_freqlist.length;
	    	}
    	}
    	return -1;
    }
    
    public int getFrequenciesAndVoltage(enCore core)
    {
    	switch(core)
    	{
	    	case CPU:
	    	{
	    		cpu_frequencies.clear();
	    		cpu_voltages.clear();
	    		String[] cpu_freqlist_split = ab.readSuCommand("cat /sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies", 1).split(" ");
	    		String[] cpu_voltages_split = ab.readSuCommand("cat /sys/devices/system/cpu/cpu0/cpufreq/UV_mV_table", 1).split("\n");
	    		if(cpu_freqlist_split.length == 0)
	    			return -1;
	    		else
	    		{
	    			int j = cpu_freqlist_split.length-1;
	    			for(int i = 0; i < cpu_freqlist_split.length; i++)
	    			{
        				int voltage = Integer.valueOf((cpu_voltages_split[j].split(" ")[1]));
    					ma.section5_sb.get(i).setProgress(voltage);
	    				cpu_voltages.add(String.valueOf(voltage));
        				j--;
	    				ma.section5_tv.get(i).setText(cpu_freqlist_split[i] + " kHz (" + String.valueOf(ma.section5_sb.get(i).getProgress()) + " mV)");
	    				cpu_frequencies.add(cpu_freqlist_split[i]);
	    			}
	    		}
	    	}
	    	case GPU:
	    	{
	    		String[] gpu_freqlist = ab.readSuCommand("cat /sys/kernel/tegra_cap/gpu_freqs", 1).split(" ");
	    		if(gpu_freqlist.length == 0)
	    			return -1;
	    		else
	    		{
	    			for(int i = 0; i < gpu_freqlist.length; i++)
	    			{
	    				//ma.section6_sb.get(i).setProgress(1111);
	    				//ma.section6_tv.get(i).setText(gpu_freqlist[i] + " kHz ( " + String.valueOf(ma.section5_sb.get(i).getProgress()) + " mV)");
	    			}
	    		}
	    	}
    	}
    	return -1;
    }
    
    int saveToInitdFile(enCore core, enOnBoot status)
    {
    	StringBuilder parameter = new StringBuilder();
    	switch(core)
    	{
	    	case CPU:
	    	{
	        	for(int i = ma.section5_sb.size()-1; i >= 0; i--)
	    		{
	        		if(i != 0)
	        			parameter.append(String.valueOf(ma.section5_sb.get(i).getProgress()) + " ");
	        		else
	        			parameter.append(String.valueOf(ma.section5_sb.get(i).getProgress()));
	    		}
	        	return (initd.updateInitd(parameter.toString(), "/sys/devices/system/cpu/cpu0/cpufreq/UV_mV_table", status));
	    	}
	    	case GPU:
	    	{
	    		
	    	}
    	}
    	return 1;
    }
    
    public int setVoltages(enCore core)
    {
    	StringBuilder parameter = new StringBuilder();
    	switch(core)
    	{
	    	case CPU:
	    	{
	        	for(int i = ma.section5_sb.size()-1; i >= 0; i--)
	    		{
	        		if(i != 0)
	        			parameter.append(String.valueOf(ma.section5_sb.get(i).getProgress()) + " ");
	        		else
	        			parameter.append(String.valueOf(ma.section5_sb.get(i).getProgress()));
	    		}
	        	ab.writeSuCommand("echo \"" + parameter.toString() + "\" > /sys/devices/system/cpu/cpu0/cpufreq/UV_mV_table");
	        	return 0;
	    	}
	    	case GPU:
	    	{
	    		
	    	}
    	}
    	return 1;
    }
}
