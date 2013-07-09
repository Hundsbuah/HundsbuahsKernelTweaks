package de.hundsbuahskerneltweaks;

import java.util.ArrayList;
import java.util.List;

import android.widget.Toast;

public class CurrentSettings
{
	private MainActivity ma;
	private AndroidBash ab;
	
	public List<String> cpu_frequencys_core1 = new ArrayList<String>();
	public List<String> cpu_frequencys_core2 = new ArrayList<String>();
	public List<String> cpu_frequencys_core3 = new ArrayList<String>();
	public List<String> cpu_frequencys_core4 = new ArrayList<String>();
	public List<String> gpu_voltages = new ArrayList<String>();
	public List<String> gpu_freqs = new ArrayList<String>();
	
	public CurrentSettings(MainActivity ma)
	{
		this.ma = ma;
		ab = new AndroidBash(ma);
	}
	
	public int readAvailableCpuFrequencies()
	{
    	try
    	{
    		cpu_frequencys_core1.clear();
    		cpu_frequencys_core2.clear();
    		cpu_frequencys_core3.clear();
    		cpu_frequencys_core4.clear();

    		String cpu_freqlist = ab.readSuCommand("cat /sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies", 1);
    		if(cpu_freqlist.isEmpty())
    			return 1;
    		
    		cpu_frequencys_core1.add("0");
    		cpu_frequencys_core2.add("0");
    		cpu_frequencys_core3.add("0");
    		cpu_frequencys_core4.add("0");
			
    		String[] cpu_freqlist_split = cpu_freqlist.split(" ");

    		for(int i = 0; i < cpu_freqlist_split.length; i++)
    		{
    			cpu_frequencys_core1.add(cpu_freqlist_split[i]);
    			cpu_frequencys_core2.add(cpu_freqlist_split[i]);
    			cpu_frequencys_core3.add(cpu_freqlist_split[i]);
    			cpu_frequencys_core4.add(cpu_freqlist_split[i]);
    		}
    	}
    	catch(Exception e)
    	{
    		ma.showMessageBox("Cant read available frequencies", 0);
    		return 1;
    	}
    	
    	return 0;
	}
	
	public int readCpuFrequencys()
	{
    	try
    	{
    		readAvailableCpuFrequencies();
    		
    		ma.addListToSpinner(ma.cpu1_selection_box, cpu_frequencys_core1);
    		ma.addListToSpinner(ma.cpu2_selection_box, cpu_frequencys_core2);
    		ma.addListToSpinner(ma.cpu3_selection_box, cpu_frequencys_core3);
    		ma.addListToSpinner(ma.cpu4_selection_box, cpu_frequencys_core4);

            if((ma.current_freq_core1 = ab.readSuCommand("cat /sys/module/cpu_tegra/parameters/pwr_cap_limit_1", 1)).isEmpty())
            	return 1;
            if((ma.current_freq_core2 = ab.readSuCommand("cat /sys/module/cpu_tegra/parameters/pwr_cap_limit_2", 1)).isEmpty())
            	return 1;
            if((ma.current_freq_core3 = ab.readSuCommand("cat /sys/module/cpu_tegra/parameters/pwr_cap_limit_3", 1)).isEmpty())
            	return 1;
            if((ma.current_freq_core4 = ab.readSuCommand("cat /sys/module/cpu_tegra/parameters/pwr_cap_limit_4", 1)).isEmpty())
            	return 1;
            if(ma.current_freq_core1.isEmpty() || ma.current_freq_core2.isEmpty() || ma.current_freq_core3.isEmpty() || ma.current_freq_core4.isEmpty())
            	return 1;
            
            ma.cpu1_selection_box.setSelection(cpu_frequencys_core1.indexOf(ma.current_freq_core1.toString()));
            ma.cpu2_selection_box.setSelection(cpu_frequencys_core2.indexOf(ma.current_freq_core2.toString()));
            ma.cpu3_selection_box.setSelection(cpu_frequencys_core3.indexOf(ma.current_freq_core3.toString()));
            ma.cpu4_selection_box.setSelection(cpu_frequencys_core4.indexOf(ma.current_freq_core4.toString()));

            
        	return 0;
    	}
    	catch(Exception e)
    	{
        	ma.showMessageBox("Cant read cpu limits...", 0);
        	return 1;
    	}
	}
	
	public int writeCpuFrequencys()
	{
		try
		{
			int cpu1_new_freq = Integer.valueOf(ma.cpu1_selection_box.getSelectedItem().toString());
			int cpu2_new_freq = Integer.valueOf(ma.cpu2_selection_box.getSelectedItem().toString());
			int cpu3_new_freq = Integer.valueOf(ma.cpu3_selection_box.getSelectedItem().toString());
			int cpu4_new_freq = Integer.valueOf(ma.cpu4_selection_box.getSelectedItem().toString());

    		if((cpu1_new_freq < 475000 && cpu1_new_freq != 0) || cpu1_new_freq > 1900000)
    		{
    			Toast.makeText(ma.getApplicationContext(), "Wrong CPU1 Frequency Setting\nFrequency must be >= 204000 and <= 1900000!", Toast.LENGTH_SHORT).show();
    			return 1;
    		}
    		if((cpu2_new_freq < 475000 && cpu2_new_freq != 0) || cpu2_new_freq > 1900000)
    		{
    			Toast.makeText(ma.getApplicationContext(), "Wrong CPU2 Frequency Setting\nFrequency must be >= 204000 and <= 1900000!", Toast.LENGTH_SHORT).show();
    			return 1;
    		}
    		if((cpu3_new_freq < 475000 && cpu3_new_freq != 0) || cpu3_new_freq > 1900000)
    		{
    			Toast.makeText(ma.getApplicationContext(), "Wrong CPU3 Frequency Setting\nFrequency must be >= 204000 and <= 1900000!", Toast.LENGTH_SHORT).show();
    			return 1;
    		}
    		if((cpu4_new_freq < 475000 && cpu4_new_freq != 0) || cpu4_new_freq > 1900000)
    		{
    			Toast.makeText(ma.getApplicationContext(), "Wrong CPU4 Frequency Setting\nFrequency must be >= 204000 and <= 1900000!", Toast.LENGTH_SHORT).show();
    			return 1;
    		}
			
    		if(cpu1_new_freq == 0 && cpu2_new_freq == 0 && cpu3_new_freq == 0 && cpu4_new_freq == 0)
    		{
    			ma.showMessageBox("You cannot disable all cores!", 0);
    			return 1;
    		}
    		
	    	ab.writeSuCommand("echo \"" + String.valueOf(cpu1_new_freq) + "\" > /sys/module/cpu_tegra/parameters/pwr_cap_limit_1");
	    	ab.writeSuCommand("echo \"" + String.valueOf(cpu2_new_freq) + "\" > /sys/module/cpu_tegra/parameters/pwr_cap_limit_2");
	    	ab.writeSuCommand("echo \"" + String.valueOf(cpu3_new_freq) + "\" > /sys/module/cpu_tegra/parameters/pwr_cap_limit_3");
	    	ab.writeSuCommand("echo \"" + String.valueOf(cpu4_new_freq) + "\" > /sys/module/cpu_tegra/parameters/pwr_cap_limit_4");
        	
	    	return 0;
		}
    	catch(Exception e)
    	{
        	ma.showMessageBox("Cant write cpu limits...", 0);
        	return 1;
    	}
	}
	
	public int readAvailableGpuFrequencies()
	{
		gpu_voltages.clear();
		gpu_freqs.clear();
     
		String gpu_voltagelist = ab.readSuCommand("cat /sys/kernel/tegra_cap/gpu_voltages", 1);
		String gpu_freqlist = ab.readSuCommand("cat /sys/kernel/tegra_cap/gpu_freqs", 1);
		
		if(gpu_voltagelist.isEmpty())
			return 1;
		
        if(gpu_freqlist.isEmpty())
			return 1;
     
		String[] gpu_voltagelist_split = gpu_voltagelist.split(" ");
		
		for(int i = 0; i < gpu_voltagelist_split.length; i++)
		{
			gpu_voltages.add(gpu_voltagelist_split[i]);
		}
     
        String[] gpu_freqlist_split = gpu_freqlist.split(" ");
		
		for(int i = 0; i < gpu_freqlist_split.length; i++)
		{
			gpu_freqs.add(gpu_freqlist_split[i]);
		}
		
		return 0;
	}
	
	public int readGpuFrequencys()
	{
		
    	try
    	{
    		readAvailableGpuFrequencies();
    		
    		ma.addListToSpinner(ma.gpu_selection_box, gpu_freqs);
            
            ma.current_voltage_gpu = ab.readSuCommand("cat /sys/kernel/tegra_cap/core_cap_level", 1);
           
            if(ma.current_voltage_gpu.isEmpty())
         	    return 1;
            
            ma.gpu_selection_box.setSelection(gpu_voltages.indexOf(ma.current_voltage_gpu.toString()));
            
        	return 0;
    	}
    	catch(Exception e)
    	{
        	ma.showMessageBox("Cant read gpu limits...", 0);
        	return 1;
    	}
	}
	
	public int writeGpuFrequencys()
	{
		try
		{
			String new_voltage = gpu_voltages.get(ma.gpu_selection_box.getSelectedItemPosition());
         
         if(new_voltage.isEmpty())
         	return 1;
         
            ab.writeSuCommand("echo \"" + new_voltage + "\" > /sys/kernel/tegra_cap/core_cap_level");
	    	ab.writeSuCommand("echo \"1\" > /sys/kernel/tegra_cap/core_cap_state");
        	return 0;
		}
    	catch(Exception e)
    	{
        	ma.showMessageBox("Cant write gpu limits...", 0);
        	return 1;
    	}
	}
}
