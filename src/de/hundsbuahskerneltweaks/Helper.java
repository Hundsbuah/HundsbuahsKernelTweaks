package de.hundsbuahskerneltweaks;

public class Helper
{
	private MainActivity ma;
	private AndroidBash ab;
	private String kernel_info;
	private int kernel_info_done = 0;
	
	public Helper(MainActivity ma)
	{
		this.ma = ma;
		ab = new AndroidBash(ma);
		
	    kernel_info = ab.readCommand("cat /proc/version", 0).toString();
        if(kernel_info.isEmpty())
        	kernel_info = "cant retrieve kernel information - app might not work correct!";
	}
	
	public String getKernelInfo()
	{
        if(!kernel_info.contains("hundsbuah") && kernel_info_done == 0)
        {
            ma.showMessageBox("No Hundsbuah Kernel found - app might not work correct!", 1);
            kernel_info_done = 1;
        }
        return kernel_info;
	}
}
