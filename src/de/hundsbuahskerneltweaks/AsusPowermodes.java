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
import de.hundsbuahskerneltweaks.MainActivity.enCore;

public class AsusPowermodes
{
	private MainActivity ma;
	private AndroidBash ab;
	private CurrentSettings cs;
    private List<String> cpuxsh_content = new ArrayList<String>();

	public AsusPowermodes(MainActivity ma)
	{
		this.ma = ma;
		cs = new CurrentSettings(ma);
		ab = new AndroidBash(ma);
	}
	
	public int readFromAsusPowerModes(int asus_powermode_selection, enCore core)
	{
        InputStream cpuxsh_inputstream = null;
		InputStreamReader cpuxsh_inputstreamreader = null;
		BufferedReader buffreader = null;

		try
		{
			switch(core)
			{
			case CPU:
				cs.readAvailableCpuFrequencies();
				switch(asus_powermode_selection)
				{
				case 1:
		    		ma.addListToSpinner(ma.cpu1_selection_box_asuspowersavermode, cs.cpu_frequencys_core1);
		    		ma.addListToSpinner(ma.cpu2_selection_box_asuspowersavermode, cs.cpu_frequencys_core2);
		    		ma.addListToSpinner(ma.cpu3_selection_box_asuspowersavermode, cs.cpu_frequencys_core3);
		    		ma.addListToSpinner(ma.cpu4_selection_box_asuspowersavermode, cs.cpu_frequencys_core4);
					ab.writeSuCommand("chmod 777 /system/etc/cpu1.sh");
					SystemClock.sleep(50);
					cpuxsh_inputstream = new FileInputStream("/system/etc/cpu1.sh");
					break;
				case 2:
		    		ma.addListToSpinner(ma.cpu1_selection_box_asusbalancedmode, cs.cpu_frequencys_core1);
		    		ma.addListToSpinner(ma.cpu2_selection_box_asusbalancedmode, cs.cpu_frequencys_core2);
		    		ma.addListToSpinner(ma.cpu3_selection_box_asusbalancedmode, cs.cpu_frequencys_core3);
		    		ma.addListToSpinner(ma.cpu4_selection_box_asusbalancedmode, cs.cpu_frequencys_core4);
					ab.writeSuCommand("chmod 777 /system/etc/cpu2.sh");
					SystemClock.sleep(50);
					cpuxsh_inputstream = new FileInputStream("/system/etc/cpu2.sh");
					break;
				case 3:
		    		ma.addListToSpinner(ma.cpu1_selection_box_asusperformancemode, cs.cpu_frequencys_core1);
		    		ma.addListToSpinner(ma.cpu2_selection_box_asusperformancemode, cs.cpu_frequencys_core2);
		    		ma.addListToSpinner(ma.cpu3_selection_box_asusperformancemode, cs.cpu_frequencys_core3);
		    		ma.addListToSpinner(ma.cpu4_selection_box_asusperformancemode, cs.cpu_frequencys_core4);
					ab.writeSuCommand("chmod 777 /system/etc/cpu3.sh");
					SystemClock.sleep(50);
					cpuxsh_inputstream = new FileInputStream("/system/etc/cpu3.sh");
					break;
				}
				break;
			case GPU:
				cs.readAvailableGpuFrequencies();
				switch(asus_powermode_selection)
				{
				case 1:
		    		ma.addListToSpinner(ma.gpu_selection_box_asuspowersavermode, cs.gpu_freqs);
					ab.writeSuCommand("chmod 777 /system/etc/cpu1.sh");
					SystemClock.sleep(50);
					cpuxsh_inputstream = new FileInputStream("/system/etc/cpu1.sh");
					break;
				case 2:
		    		ma.addListToSpinner(ma.gpu_selection_box_asusbalancedmode, cs.gpu_freqs);
					ab.writeSuCommand("chmod 777 /system/etc/cpu2.sh");
					SystemClock.sleep(50);
					cpuxsh_inputstream = new FileInputStream("/system/etc/cpu2.sh");
					break;
				case 3:
		    		ma.addListToSpinner(ma.gpu_selection_box_asusperformancemode, cs.gpu_freqs);
					ab.writeSuCommand("chmod 777 /system/etc/cpu3.sh");
					SystemClock.sleep(50);
					cpuxsh_inputstream = new FileInputStream("/system/etc/cpu3.sh");
					break;
				}
				break;
		    }
	    }
		catch(Exception e)
		{
			ma.showMessageBox("Error reading valid values!", 0);
			return 1;
		}
						
		try
		{	
			// if file the available for reading
			if (cpuxsh_inputstream != null)
			{
			  // prepare the file for reading
			  cpuxsh_inputstreamreader = new InputStreamReader(cpuxsh_inputstream);
			  buffreader = new BufferedReader(cpuxsh_inputstreamreader);
			  String line = null;
			  // read every line of the file into the line-variable, on line at the time
			  
			  switch(core)
			  {
			  case CPU:
				  do
				  {
				     line = buffreader.readLine();
				     if(line != null)
				     {
					     if(line.contains("/sys/module/cpu_tegra/parameters/pwr_cap_limit_1") && line.contains("echo") && (line.contains("logi") == false))
				    	 {
					    	 String[] cpu_frequency = line.replace("\"",  "").split(" ");
						     switch(asus_powermode_selection)
						     {
						     case 1:
						    	 ma.cpu1_selection_box_asuspowersavermode.setSelection(cs.cpu_frequencys_core1.indexOf(cpu_frequency[1]));
						    	 break;
						     case 2:
						    	 ma.cpu1_selection_box_asusbalancedmode.setSelection(cs.cpu_frequencys_core1.indexOf(cpu_frequency[1]));
						    	 break;
						     case 3:
						    	 ma.cpu1_selection_box_asusperformancemode.setSelection(cs.cpu_frequencys_core1.indexOf(cpu_frequency[1]));
						    	 break;
						     }
				    	 }
					     if(line.contains("/sys/module/cpu_tegra/parameters/pwr_cap_limit_2") && line.contains("echo") && (line.contains("logi") == false))
				    	 {
					    	 String[] cpu_frequency = line.replace("\"",  "").split(" ");
						     switch(asus_powermode_selection)
						     {
						     case 1:
						    	 ma.cpu2_selection_box_asuspowersavermode.setSelection(cs.cpu_frequencys_core1.indexOf(cpu_frequency[1]));
						    	 break;
						     case 2:
						    	 ma.cpu2_selection_box_asusbalancedmode.setSelection(cs.cpu_frequencys_core1.indexOf(cpu_frequency[1]));
						    	 break;
						     case 3:
						    	 ma.cpu2_selection_box_asusperformancemode.setSelection(cs.cpu_frequencys_core1.indexOf(cpu_frequency[1]));
						    	 break;
						     }
				    	 }
					     if(line.contains("/sys/module/cpu_tegra/parameters/pwr_cap_limit_3") && line.contains("echo") && (line.contains("logi") == false))
				    	 {
					    	 String[] cpu_frequency = line.replace("\"",  "").split(" ");
						     switch(asus_powermode_selection)
						     {
						     case 1:
						    	 ma.cpu3_selection_box_asuspowersavermode.setSelection(cs.cpu_frequencys_core1.indexOf(cpu_frequency[1]));
						    	 break;
						     case 2:
						    	 ma.cpu3_selection_box_asusbalancedmode.setSelection(cs.cpu_frequencys_core1.indexOf(cpu_frequency[1]));
						    	 break;
						     case 3:
						    	 ma.cpu3_selection_box_asusperformancemode.setSelection(cs.cpu_frequencys_core1.indexOf(cpu_frequency[1]));
						    	 break;
						     }
				    	 }
					     if(line.contains("/sys/module/cpu_tegra/parameters/pwr_cap_limit_4") && line.contains("echo") && (line.contains("logi") == false))
				    	 {
					    	 String[] cpu_frequency = line.replace("\"",  "").split(" ");
						     switch(asus_powermode_selection)
						     {
						     case 1:
						    	 ma.cpu4_selection_box_asuspowersavermode.setSelection(cs.cpu_frequencys_core1.indexOf(cpu_frequency[1]));
						    	 break;
						     case 2:
						    	 ma.cpu4_selection_box_asusbalancedmode.setSelection(cs.cpu_frequencys_core1.indexOf(cpu_frequency[1]));
						    	 break;
						     case 3:
						    	 ma.cpu4_selection_box_asusperformancemode.setSelection(cs.cpu_frequencys_core1.indexOf(cpu_frequency[1]));
						    	 break;
						     }
				    	 }

				     }
				  }
				  while(line != null);
				  break;
			  case GPU:
				  do
				  {
				     line = buffreader.readLine();
				     if(line != null)
				     {
					     if(line.contains("/sys/kernel/tegra_cap/core_cap_level") && line.contains("echo") && (line.contains("logi") == false))
				    	 {
					    	 String[] gpu_frequency = line.replace("\"",  "").split(" ");
						     switch(asus_powermode_selection)
						     {
						     case 1:
						    	 ma.gpu_selection_box_asuspowersavermode.setSelection(cs.gpu_voltages.indexOf(gpu_frequency[1]));
						    	 break;
						     case 2:
						    	 ma.gpu_selection_box_asusbalancedmode.setSelection(cs.gpu_voltages.indexOf(gpu_frequency[1]));
						    	 break;
						     case 3:
						    	 ma.gpu_selection_box_asusperformancemode.setSelection(cs.gpu_voltages.indexOf(gpu_frequency[1]));
						    	 break;
						     }
				    	 }
				     }
				  }
				  while(line != null);
				  break;
			  }

              buffreader.close();
              cpuxsh_inputstreamreader.close();
			}
		}
		catch (Exception e)
		{
			ma.showMessageBox("Cant read cpu(x).sh file!", 0);
			return 1;
		}
		finally
		{
			// close the file.
			try
			{
				cpuxsh_inputstream.close();
			}
			catch (Exception e)
			{
				ma.showMessageBox("Cant close file input stream for cpu(x).sh file!", 0);
				return 1;
			}
		}
		/*
		switch(core)
		{
		case CPU:
			if(cpu1_new_freq == 0 && cpu2_new_freq == 0 && cpu3_new_freq == 0 && cpu4_new_freq == 0)
			{
				ma.showMessageBox("Dont disable all cores and/or select valid values!");
				return 1;
			}
			break;
		case GPU:
			if(gpu_new_voltage == 0)
			{
				ma.showMessageBox("Please select a gpu value!");
				return 1;
			}
			break;
		}
		*/
		return 0;
	}
	
	public int saveToAsusPowerModes(int asus_powermode_selection, enCore core)
	{
        InputStream cpuxsh_inputstream = null;
		InputStreamReader cpuxsh_inputstreamreader = null;
		BufferedReader buffreader = null;
		BufferedWriter cpuxsh_bufferedwriter = null;

		int cpu1_new_freq = 0;
		int cpu2_new_freq = 0;
		int cpu3_new_freq = 0;
		int cpu4_new_freq = 0;
		int gpu_new_voltage = 0;
		
		ab.writeSuCommand("busybox mount -o remount,rw /dev/block/mmcblk0p1 /system");
		SystemClock.sleep(70);
		try
		{
			switch(core)
			{
			case CPU:

				switch(asus_powermode_selection)
				{
				case 1:
					if(ma.cpu1_selection_box_asuspowersavermode.getSelectedItem() != null && ma.cpu2_selection_box_asuspowersavermode.getSelectedItem() != null && ma.cpu3_selection_box_asuspowersavermode.getSelectedItem() != null && ma.cpu4_selection_box_asuspowersavermode.getSelectedItem() != null)
					{
						cpu1_new_freq = Integer.valueOf(ma.cpu1_selection_box_asuspowersavermode.getSelectedItem().toString());
						cpu2_new_freq = Integer.valueOf(ma.cpu2_selection_box_asuspowersavermode.getSelectedItem().toString());
						cpu3_new_freq = Integer.valueOf(ma.cpu3_selection_box_asuspowersavermode.getSelectedItem().toString());
						cpu4_new_freq = Integer.valueOf(ma.cpu4_selection_box_asuspowersavermode.getSelectedItem().toString());
					}
					break;
				case 2:
					if(ma.cpu1_selection_box_asusbalancedmode.getSelectedItem() != null && ma.cpu2_selection_box_asusbalancedmode.getSelectedItem() != null && ma.cpu3_selection_box_asusbalancedmode.getSelectedItem() != null && ma.cpu4_selection_box_asusbalancedmode.getSelectedItem() != null)
					{
						cpu1_new_freq = Integer.valueOf(ma.cpu1_selection_box_asusbalancedmode.getSelectedItem().toString());
						cpu2_new_freq = Integer.valueOf(ma.cpu2_selection_box_asusbalancedmode.getSelectedItem().toString());
						cpu3_new_freq = Integer.valueOf(ma.cpu3_selection_box_asusbalancedmode.getSelectedItem().toString());
						cpu4_new_freq = Integer.valueOf(ma.cpu4_selection_box_asusbalancedmode.getSelectedItem().toString());
					}
					break;
				case 3:
					if(ma.cpu1_selection_box_asusperformancemode.getSelectedItem() != null && ma.cpu2_selection_box_asusperformancemode.getSelectedItem() != null && ma.cpu3_selection_box_asusperformancemode.getSelectedItem() != null && ma.cpu4_selection_box_asusperformancemode.getSelectedItem() != null)
					{
						cpu1_new_freq = Integer.valueOf(ma.cpu1_selection_box_asusperformancemode.getSelectedItem().toString());
						cpu2_new_freq = Integer.valueOf(ma.cpu2_selection_box_asusperformancemode.getSelectedItem().toString());
						cpu3_new_freq = Integer.valueOf(ma.cpu3_selection_box_asusperformancemode.getSelectedItem().toString());
						cpu4_new_freq = Integer.valueOf(ma.cpu4_selection_box_asusperformancemode.getSelectedItem().toString());
					}
					break;
				}
				break;
			case GPU:
				switch(asus_powermode_selection)
				{
				case 1:
					if(ma.gpu_selection_box_asuspowersavermode.getSelectedItem() != null)
					{
						gpu_new_voltage = Integer.valueOf(cs.gpu_voltages.get(cs.gpu_freqs.indexOf(ma.gpu_selection_box_asuspowersavermode.getSelectedItem().toString())));
					}
					break;
				case 2:
					if(ma.gpu_selection_box_asusbalancedmode.getSelectedItem() != null)
					{
						gpu_new_voltage = Integer.valueOf(cs.gpu_voltages.get(cs.gpu_freqs.indexOf(ma.gpu_selection_box_asusbalancedmode.getSelectedItem().toString())));
					}
					break;
				case 3:
					if(ma.gpu_selection_box_asusperformancemode.getSelectedItem() != null)
					{
						gpu_new_voltage = Integer.valueOf(cs.gpu_voltages.get(cs.gpu_freqs.indexOf(ma.gpu_selection_box_asusperformancemode.getSelectedItem().toString())));
					}
					break;
				}
				break;
		    }
	    }
		catch(Exception e)
		{
			ma.showMessageBox("Error reading valid cpu values!", 0);
			return 1;
		}
		switch(core)
		{
		case CPU:
			if(cpu1_new_freq == 0 && cpu2_new_freq == 0 && cpu3_new_freq == 0 && cpu4_new_freq == 0)
			{
				ma.showMessageBox("Dont disable all cores and/or select valid values!", 0);
				return 1;
			}
			break;
		case GPU:
			if(gpu_new_voltage == 0)
			{
				ma.showMessageBox("Please select a gpu value!", 0);
				return 1;
			}
			break;
		}

				
		cpuxsh_content.clear();
		
		try
		{
			// open the file for reading
			switch(asus_powermode_selection)
			{
				case 1:
					ab.writeSuCommand("chmod 777 /system/etc/cpu1.sh");
					SystemClock.sleep(50);
					cpuxsh_inputstream = new FileInputStream("/system/etc/cpu1.sh");
					break;
				case 2:
					ab.writeSuCommand("chmod 777 /system/etc/cpu2.sh");
					SystemClock.sleep(50);
					cpuxsh_inputstream = new FileInputStream("/system/etc/cpu2.sh");
					break;
				case 3:
					ab.writeSuCommand("chmod 777 /system/etc/cpu3.sh");
					SystemClock.sleep(50);
					cpuxsh_inputstream = new FileInputStream("/system/etc/cpu3.sh");
					break;
				default:
					ma.showMessageBox("No Asus Power Mode was selected!", 0);
			}
			
			// if file the available for reading
			if (cpuxsh_inputstream != null)
			{
			  // prepare the file for reading
			  cpuxsh_inputstreamreader = new InputStreamReader(cpuxsh_inputstream);
			  buffreader = new BufferedReader(cpuxsh_inputstreamreader);
			  String line = null;
			  // read every line of the file into the line-variable, on line at the time
			  switch(core)
			  {
			  case CPU:
				  do
				  {
				     line = buffreader.readLine();
				     if(line != null)
				     {
					     if(line.contains("/sys/module/cpu_tegra/parameters/pwr_cap_limit_1") && line.contains("echo") && (line.contains("logi") == false))
				    	 {
				    	     line = "echo \"" + String.valueOf(cpu1_new_freq) + "\" > /sys/module/cpu_tegra/parameters/pwr_cap_limit_1";
				    	 }
					     if(line.contains("/sys/module/cpu_tegra/parameters/pwr_cap_limit_2") && line.contains("echo") && (line.contains("logi") == false))
				    	 {
					    	 line = "echo \"" + String.valueOf(cpu2_new_freq) + "\" > /sys/module/cpu_tegra/parameters/pwr_cap_limit_2";
				    	 }
					     if(line.contains("/sys/module/cpu_tegra/parameters/pwr_cap_limit_3") && line.contains("echo") && (line.contains("logi") == false))
				    	 {
					    	 line = "echo \"" + String.valueOf(cpu3_new_freq) + "\" > /sys/module/cpu_tegra/parameters/pwr_cap_limit_3";
				    	 }
					     if(line.contains("/sys/module/cpu_tegra/parameters/pwr_cap_limit_4") && line.contains("echo") && (line.contains("logi") == false))
				    	 {
					    	 line = "echo \"" + String.valueOf(cpu4_new_freq) + "\" > /sys/module/cpu_tegra/parameters/pwr_cap_limit_4";
				    	 }
					     cpuxsh_content.add(line);
				     }
				  }
				  while(line != null);
				  break;
			  case GPU:
				  do
				  {
				     line = buffreader.readLine();
				     if(line != null)
				     {
					     if(line.contains("/sys/kernel/tegra_cap/core_cap_level") && line.contains("echo") && (line.contains("logi") == false))
				    	 {
				    	     line = "echo \"" + String.valueOf(gpu_new_voltage) + "\" > /sys/kernel/tegra_cap/core_cap_level";
				    	 }
					     cpuxsh_content.add(line);
				     }
				  }
				  while(line != null);
				  break;
			  }

              buffreader.close();
              cpuxsh_inputstreamreader.close();
			}
		}
		catch (Exception e)
		{
			ma.showMessageBox("Cant read cpu(x).sh file!", 0);
			return 1;
		}
		finally
		{
			// close the file.
			try
			{
				cpuxsh_inputstream.close();
			}
			catch (Exception e)
			{
				ma.showMessageBox("Cant close file input stream for cpu(x).sh file!", 0);
				return 1;
			}
		}
		
		try
		{
			// open the file for reading
			switch(asus_powermode_selection)
			{
				case 1:
					ab.writeSuCommand("chmod 777 /system/etc/cpu1.sh");
					SystemClock.sleep(50);
					cpuxsh_bufferedwriter = new BufferedWriter(new FileWriter(new File("/system/etc/cpu1.sh")));
					break;
				case 2:
					ab.writeSuCommand("chmod 777 /system/etc/cpu2.sh");
					SystemClock.sleep(50);
					cpuxsh_bufferedwriter = new BufferedWriter(new FileWriter(new File("/system/etc/cpu2.sh")));
					break;
				case 3:
					ab.writeSuCommand("chmod 777 /system/etc/cpu3.sh");
					SystemClock.sleep(50);
					cpuxsh_bufferedwriter = new BufferedWriter(new FileWriter(new File("/system/etc/cpu3.sh")));
					break;
				default:
					ma.showMessageBox("Cant write to Asus Power Mode!", 0);
			}
			
			for(int i = 0; i < cpuxsh_content.size(); i++)
			{
				cpuxsh_bufferedwriter.write(cpuxsh_content.get(i) + "\n");
			}
			cpuxsh_bufferedwriter.flush();
		}
		catch (Exception ex)
		{
			ma.showMessageBox("Cant write to cpu(x).sh file!", 0);
			return 1;
		}
		finally
		{
			// close the file.
			try
			{
				cpuxsh_bufferedwriter.close();
			}
			catch (IOException e)
			{
				ma.showMessageBox("Cant close file output stream for cpu(x).sh file!", 0);
				return 1;
			}
		}

		return 0;
	}
}
