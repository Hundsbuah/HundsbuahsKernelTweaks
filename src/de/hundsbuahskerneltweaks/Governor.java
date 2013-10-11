package de.hundsbuahskerneltweaks;

import java.util.ArrayList;
import java.util.List;

public class Governor
{
	private MainActivity ma;
	private AndroidBash ab;
	public List<String> governors = new ArrayList<String>();

	public Governor(MainActivity ma)
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
}
