package de.hundsbuahskerneltweaks;

import de.hundsbuahskerneltweaks.MainActivity.enOnBoot;

public class Hotplugging {
    private MainActivity ma;
    private AndroidBash ab;
    private InitdHelper initd;
    public final String hotplugging_if = "/sys/kernel/rt_config/rt_config";
    
	public Hotplugging(MainActivity ma)
    {
    	this.ma = ma;
    	ab = new AndroidBash(this.ma);
    	initd = new InitdHelper(this.ma);
    }
	
	public int getRtConfig()
	{
		String[] rt_config = ab.readSuCommand("cat /sys/kernel/rt_config/rt_config", 1).split(" ");
		if(rt_config.length < 2)
			return 1;
		
		ma.section7_sb_rt_2.setProgress(Integer.valueOf(rt_config[0]));
		ma.section7_sb_rt_3.setProgress(Integer.valueOf(rt_config[1]));
		ma.section7_sb_rt_4.setProgress(Integer.valueOf(rt_config[2]));
	
		return 0;
	}
	
	public int saveToInitdFile(enOnBoot status)
	{
		StringBuilder parameter = new StringBuilder();
		
		parameter.append(String.valueOf(ma.section7_sb_rt_2.getProgress()) + " ");
		parameter.append(String.valueOf(ma.section7_sb_rt_3.getProgress()) + " ");
		parameter.append(String.valueOf(ma.section7_sb_rt_4.getProgress()));
		
    	return (initd.updateInitd(parameter.toString(), hotplugging_if, status));
	}
	
	public int setRtConfig()
	{
		StringBuilder parameter = new StringBuilder();
		
		parameter.append(String.valueOf(ma.section7_sb_rt_2.getProgress()) + " ");
		parameter.append(String.valueOf(ma.section7_sb_rt_3.getProgress()) + " ");
		parameter.append(String.valueOf(ma.section7_sb_rt_4.getProgress()));
		
		ab.writeSuCommand("echo \"" + parameter + "\" > "+ hotplugging_if);
		
		return 0;
	}

}
