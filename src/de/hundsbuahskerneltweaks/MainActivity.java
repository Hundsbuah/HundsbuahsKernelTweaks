package de.hundsbuahskerneltweaks;

import java.util.List;
import java.util.Locale;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {
		
	public String current_freq_core1;
	public String current_freq_core2;
	public String current_freq_core3;
	public String current_freq_core4;
	public String current_voltage_gpu;
	
	public Spinner cpu1_selection_box;
	public Spinner cpu2_selection_box;
	public Spinner cpu3_selection_box;
	public Spinner cpu4_selection_box;

	public Spinner cpu1_selection_box_asuspowersavermode;
	public Spinner cpu2_selection_box_asuspowersavermode;
	public Spinner cpu3_selection_box_asuspowersavermode;
	public Spinner cpu4_selection_box_asuspowersavermode;
	
	public Spinner cpu1_selection_box_asusbalancedmode;
	public Spinner cpu2_selection_box_asusbalancedmode;
	public Spinner cpu3_selection_box_asusbalancedmode;
	public Spinner cpu4_selection_box_asusbalancedmode;
	
	public Spinner cpu1_selection_box_asusperformancemode;
	public Spinner cpu2_selection_box_asusperformancemode;
	public Spinner cpu3_selection_box_asusperformancemode;
	public Spinner cpu4_selection_box_asusperformancemode;
	
	public Spinner gpu_selection_box_asuspowersavermode;
	public Spinner gpu_selection_box_asusbalancedmode;
	public Spinner gpu_selection_box_asusperformancemode;

	public Spinner gpu_selection_box;
	
	private Button section1_bt_apply;
	private Button section1_bt_asuspowersavermode;
	private Button section1_bt_asusbalancedmode;
	private Button section1_bt_asusperformancemode;
	private Button section1_bt_asuspowersavermode_read;
	private Button section1_bt_asusbalancedmode_read;
	private Button section1_bt_asusperformancemode_read;
	private Button section1_bt_read;
	private TextView tv_label;
    
    private Button section2_bt_apply;
	private Button section2_bt_read;
	private Button section2_bt_asuspowersavermode;
	private Button section2_bt_asusbalancedmode;
	private Button section2_bt_asusperformancemode;
	private Button section2_bt_asuspowersavermode_read;
	private Button section2_bt_asusbalancedmode_read;
	private Button section2_bt_asusperformancemode_read;
	
    private Button section3_bt_gamingmode;
	private int num_current_messageboxes = 0;
	final int MAX_CURRENT_MESSAGE_BOXES = 2;
	
    public int kernel_info_done = 0;
   
    public enum enCore
    {
	   CPU,
   	   GPU,
    }
    
    private AndroidBash ab = new AndroidBash(this);
    private Helper helper = new Helper(this);
    private CurrentSettings cs = new CurrentSettings(this);
    private AsusPowermodes apm = new AsusPowermodes(this);
    
	public void showMessageBox(String str, int showAlways)
	{
		num_current_messageboxes++;
		if(num_current_messageboxes <= MAX_CURRENT_MESSAGE_BOXES || showAlways == 1)
		{
			Builder ad = new AlertDialog.Builder(this);  
			ad.setCancelable(false);
			ad.setTitle("An Error occured...");
			ad.setMessage(str);  
			ad.setPositiveButton("OK", new DialogInterface.OnClickListener()
			{  
			    @Override  
			    public void onClick(DialogInterface dialog, int which)
			    {  
			        dialog.dismiss();
			        num_current_messageboxes--; 
			    }  
			});  
			ad.show();
		}
	}
	
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a SectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new SectionFragment();
			Bundle args = new Bundle();
			args.putInt(SectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}
	
	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	@SuppressLint("ValidFragment")
	public class SectionFragment extends Fragment implements OnClickListener {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private View rootView = null;
		public static final String ARG_SECTION_NUMBER = "section_number";

		public SectionFragment()
		{

		}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	
            
        	if(getArguments().getInt(ARG_SECTION_NUMBER) == 1)
        	{
        		rootView = inflater.inflate(R.layout.fragment_cpu, container, false);
        		section1_bt_apply = (Button) rootView.findViewById(R.id.section1_bt_apply);
        		section1_bt_read = (Button) rootView.findViewById(R.id.section1_bt_read);
        		section1_bt_asuspowersavermode = (Button) rootView.findViewById(R.id.section1_bt_asuspowersavermode);
        		section1_bt_asusbalancedmode = (Button) rootView.findViewById(R.id.section1_bt_asusbalancedmode);
        		section1_bt_asusperformancemode = (Button) rootView.findViewById(R.id.section1_bt_asusperformancemode);
        		section1_bt_asuspowersavermode_read = (Button) rootView.findViewById(R.id.section1_bt_asuspowersavermode_read);
        		section1_bt_asusbalancedmode_read = (Button) rootView.findViewById(R.id.section1_bt_asusbalancedmode_read);
        		section1_bt_asusperformancemode_read = (Button) rootView.findViewById(R.id.section1_bt_asusperformancemode_read);
        		tv_label = (TextView) rootView.findViewById(R.id.section_label);
        		cpu1_selection_box = (Spinner) rootView.findViewById(R.id.section1_sp_cpu1);
        		cpu2_selection_box = (Spinner) rootView.findViewById(R.id.section1_sp_cpu2);
        		cpu3_selection_box = (Spinner) rootView.findViewById(R.id.section1_sp_cpu3);
        		cpu4_selection_box = (Spinner) rootView.findViewById(R.id.section1_sp_cpu4);         
                
        		cpu1_selection_box_asuspowersavermode = (Spinner) rootView.findViewById(R.id.section1_sp_cpu1_asuspowersavermode);
        		cpu2_selection_box_asuspowersavermode = (Spinner) rootView.findViewById(R.id.section1_sp_cpu2_asuspowersavermode);
        		cpu3_selection_box_asuspowersavermode = (Spinner) rootView.findViewById(R.id.section1_sp_cpu3_asuspowersavermode);
        		cpu4_selection_box_asuspowersavermode = (Spinner) rootView.findViewById(R.id.section1_sp_cpu4_asuspowersavermode);    
        		
        		cpu1_selection_box_asusbalancedmode = (Spinner) rootView.findViewById(R.id.section1_sp_cpu1_asusbalancedmode);
        		cpu2_selection_box_asusbalancedmode = (Spinner) rootView.findViewById(R.id.section1_sp_cpu2_asusbalancedmode);
        		cpu3_selection_box_asusbalancedmode = (Spinner) rootView.findViewById(R.id.section1_sp_cpu3_asusbalancedmode);
        		cpu4_selection_box_asusbalancedmode = (Spinner) rootView.findViewById(R.id.section1_sp_cpu4_asusbalancedmode);    
        		
        		cpu1_selection_box_asusperformancemode = (Spinner) rootView.findViewById(R.id.section1_sp_cpu1_asusperformancemode);
        		cpu2_selection_box_asusperformancemode = (Spinner) rootView.findViewById(R.id.section1_sp_cpu2_asusperformancemode);
        		cpu3_selection_box_asusperformancemode = (Spinner) rootView.findViewById(R.id.section1_sp_cpu3_asusperformancemode);
        		cpu4_selection_box_asusperformancemode = (Spinner) rootView.findViewById(R.id.section1_sp_cpu4_asusperformancemode);    
        		
            	section1_bt_apply.setOnClickListener(this);
            	section1_bt_read.setOnClickListener(this);
            	section1_bt_asuspowersavermode.setOnClickListener(this);
            	section1_bt_asusbalancedmode.setOnClickListener(this);
            	section1_bt_asusperformancemode.setOnClickListener(this);

            	section1_bt_asuspowersavermode_read.setOnClickListener(this);
            	section1_bt_asusbalancedmode_read.setOnClickListener(this);
            	section1_bt_asusperformancemode_read.setOnClickListener(this);

            	apm.readFromAsusPowerModes(1, enCore.CPU);
            	apm.readFromAsusPowerModes(2, enCore.CPU);
            	apm.readFromAsusPowerModes(3, enCore.CPU);
            	
            	cs.readCpuFrequencys();
            	
            	tv_label.setText(helper.getKernelInfo());
                
            }
        	else if(getArguments().getInt(ARG_SECTION_NUMBER) == 2)
        	{
                rootView = inflater.inflate(R.layout.fragment_gpu, container, false);
        		section2_bt_apply = (Button) rootView.findViewById(R.id.section2_bt_apply);
        		section2_bt_read = (Button) rootView.findViewById(R.id.section2_bt_read);
        		gpu_selection_box = (Spinner) rootView.findViewById(R.id.section2_sp_gpu);
        		gpu_selection_box_asuspowersavermode = (Spinner) rootView.findViewById(R.id.section2_sp_gpu_asuspowersavermode);
        		gpu_selection_box_asusbalancedmode = (Spinner) rootView.findViewById(R.id.section2_sp_gpu_asusbalancedmode);
        		gpu_selection_box_asusperformancemode = (Spinner) rootView.findViewById(R.id.section2_sp_gpu_asusperformancemode);
        		section2_bt_asuspowersavermode = (Button) rootView.findViewById(R.id.section2_bt_asuspowersavermode);
        		section2_bt_asusbalancedmode = (Button) rootView.findViewById(R.id.section2_bt_asusbalancedmode);
        		section2_bt_asusperformancemode = (Button) rootView.findViewById(R.id.section2_bt_asusperformancemode);
        		section2_bt_asuspowersavermode_read = (Button) rootView.findViewById(R.id.section2_bt_asuspowersavermode_read);
        		section2_bt_asusbalancedmode_read = (Button) rootView.findViewById(R.id.section2_bt_asusbalancedmode_read);
        		section2_bt_asusperformancemode_read = (Button) rootView.findViewById(R.id.section2_bt_asusperformancemode_read);
        		tv_label = (TextView) rootView.findViewById(R.id.section_label);            	

        		section2_bt_apply.setOnClickListener(this);
                section2_bt_read.setOnClickListener(this);
            	section2_bt_asuspowersavermode.setOnClickListener(this);
            	section2_bt_asusbalancedmode.setOnClickListener(this);
            	section2_bt_asusperformancemode.setOnClickListener(this);
            	section2_bt_asuspowersavermode_read.setOnClickListener(this);
            	section2_bt_asusbalancedmode_read.setOnClickListener(this);
            	section2_bt_asusperformancemode_read.setOnClickListener(this);

            	apm.readFromAsusPowerModes(1, enCore.GPU);
            	apm.readFromAsusPowerModes(2, enCore.GPU);
            	apm.readFromAsusPowerModes(3, enCore.GPU);
            	
            	cs.readGpuFrequencys();
                
                tv_label.setText(helper.getKernelInfo());
        	}

        	else
        	{
                rootView = inflater.inflate(R.layout.fragment_gamingmode, container, false);
        		tv_label = (TextView) rootView.findViewById(R.id.section_label);
        		section3_bt_gamingmode = (Button) rootView.findViewById(R.id.section3_bt_gamingmode);

        		section3_bt_gamingmode.setOnClickListener(this);

        		tv_label.setText(helper.getKernelInfo());
        	}
        	
            return rootView;
        }

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
		    case R.id.section1_bt_apply:
		    	try
		    	{
		    		if(cs.writeCpuFrequencys() == 0)
		    		{
		    			SystemClock.sleep(500);
		    			if(cs.readCpuFrequencys() == 0)
		    			{
		    				
		    				Toast.makeText(getApplicationContext(), "Successful...\n"
		    						+ "CPU1: " + current_freq_core1 + "KHz\n"
		    						+ "CPU2: " + current_freq_core2 + "KHz\n"
		    						+ "CPU3: " + current_freq_core3 + "KHz\n"
		    						+ "CPU4: " + current_freq_core4 + "KHz", Toast.LENGTH_SHORT).show();
		    					
		    			}
		    		}
		    	}
		    	catch(Exception e)
		    	{
		    		Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
		    	}
		        break;
		    case R.id.section1_bt_asuspowersavermode:
		    	if(apm.saveToAsusPowerModes(1, enCore.CPU) == 0)
		    	{
			    	if(apm.readFromAsusPowerModes(1, enCore.CPU) == 0)
			    	{
			    		Toast.makeText(getApplicationContext(), "Successful...", Toast.LENGTH_SHORT).show();
			    	}
			    	else
			    	{
			    		Toast.makeText(getApplicationContext(), "Error...", Toast.LENGTH_SHORT).show();
			    	}
		    	}
		    	else
		    	{
		    		Toast.makeText(getApplicationContext(), "Error...", Toast.LENGTH_SHORT).show();
		    	}
				ab.writeSuCommand("busybox mount -o remount,ro /dev/block/mmcblk0p1 /system");
				SystemClock.sleep(50);
		    	break;
		    case R.id.section1_bt_asusbalancedmode:
		    	if(apm.saveToAsusPowerModes(2, enCore.CPU) == 0)
		    	{
			    	if(apm.readFromAsusPowerModes(2, enCore.CPU) == 0)
			    	{
			    		Toast.makeText(getApplicationContext(), "Successful...", Toast.LENGTH_SHORT).show();
			    	}
			    	else
			    	{
			    		Toast.makeText(getApplicationContext(), "Error...", Toast.LENGTH_SHORT).show();
			    	}
		    	}
		    	else
		    	{
		    		Toast.makeText(getApplicationContext(), "Error...", Toast.LENGTH_SHORT).show();
		    	}
				ab.writeSuCommand("busybox mount -o remount,ro /dev/block/mmcblk0p1 /system");
				SystemClock.sleep(50);
		    	break;
		    case R.id.section1_bt_asusperformancemode:
		    	if(apm.saveToAsusPowerModes(3, enCore.CPU) == 0)
		    	{
			    	if(apm.readFromAsusPowerModes(3, enCore.CPU) == 0)
			    	{
			    		Toast.makeText(getApplicationContext(), "Successful...", Toast.LENGTH_SHORT).show();
			    	}
			    	else
			    	{
			    		Toast.makeText(getApplicationContext(), "Error...", Toast.LENGTH_SHORT).show();
			    	}
		    	}
		    	else
		    	{
		    		Toast.makeText(getApplicationContext(), "Error...", Toast.LENGTH_SHORT).show();
		    	}
				ab.writeSuCommand("busybox mount -o remount,ro /dev/block/mmcblk0p1 /system");
				SystemClock.sleep(50);
		    	break;
		    case R.id.section1_bt_asuspowersavermode_read:
		    	if(apm.readFromAsusPowerModes(1, enCore.CPU) == 0)
		    	{
		    		Toast.makeText(getApplicationContext(), "Successful...", Toast.LENGTH_SHORT).show();
		    	}
		    	else
		    	{
		    		Toast.makeText(getApplicationContext(), "Error...", Toast.LENGTH_SHORT).show();
		    	}
		    	break;
		    case R.id.section1_bt_asusbalancedmode_read:
		    	if(apm.readFromAsusPowerModes(2, enCore.CPU) == 0)
		    	{
		    		Toast.makeText(getApplicationContext(), "Successful...", Toast.LENGTH_SHORT).show();
		    	}
		    	else
		    	{
		    		Toast.makeText(getApplicationContext(), "Error...", Toast.LENGTH_SHORT).show();
		    	}
		    	break;
		    case R.id.section1_bt_asusperformancemode_read:
		    	if(apm.readFromAsusPowerModes(3, enCore.CPU) == 0)
		    	{
		    		Toast.makeText(getApplicationContext(), "Successful...", Toast.LENGTH_SHORT).show();
		    	}
		    	else
		    	{
		    		Toast.makeText(getApplicationContext(), "Error...", Toast.LENGTH_SHORT).show();
		    	}
		    	break;
		    case R.id.section1_bt_read:
		    	try
		    	{
		    		if(cs.readCpuFrequencys() == 0)
		    		{
		    			Toast.makeText(getApplicationContext(), "Successful...", Toast.LENGTH_SHORT).show();
		    		}
		    		else
		    		{
		    			Toast.makeText(getApplicationContext(), "Error...", Toast.LENGTH_SHORT).show();
		    		}
		    	}
		    	catch(Exception e)
		    	{
		    		Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
		    	}
		        break;
		    case R.id.section2_bt_asuspowersavermode_read:
		    	if(apm.readFromAsusPowerModes(1, enCore.GPU) == 0)
		    	{
		    		Toast.makeText(getApplicationContext(), "Successful...", Toast.LENGTH_SHORT).show();
		    	}
		    	else
		    	{
		    		Toast.makeText(getApplicationContext(), "Error...", Toast.LENGTH_SHORT).show();
		    	}
		    	break;
		    case R.id.section2_bt_asusbalancedmode_read:
		    	if(apm.readFromAsusPowerModes(2, enCore.GPU) == 0)
		    	{
		    		Toast.makeText(getApplicationContext(), "Successful...", Toast.LENGTH_SHORT).show();
		    	}
		    	else
		    	{
		    		Toast.makeText(getApplicationContext(), "Error...", Toast.LENGTH_SHORT).show();
		    	}
		    	break;
		    case R.id.section2_bt_asusperformancemode_read:
		    	if(apm.readFromAsusPowerModes(3, enCore.GPU) == 0)
		    	{
		    		Toast.makeText(getApplicationContext(), "Successful...", Toast.LENGTH_SHORT).show();
		    	}
		    	else
		    	{
		    		Toast.makeText(getApplicationContext(), "Error...", Toast.LENGTH_SHORT).show();
		    	}
		    	break;
		    case R.id.section2_bt_asuspowersavermode:
		    	if(apm.saveToAsusPowerModes(1, enCore.GPU) == 0)
		    	{
			    	if(apm.readFromAsusPowerModes(1, enCore.GPU) == 0)
			    	{
			    		Toast.makeText(getApplicationContext(), "Successful...", Toast.LENGTH_SHORT).show();
			    	}
			    	else
			    	{
			    		Toast.makeText(getApplicationContext(), "Error...", Toast.LENGTH_SHORT).show();
			    	}
		    	}
		    	else
		    	{
		    		Toast.makeText(getApplicationContext(), "Error...", Toast.LENGTH_SHORT).show();
		    	}
				ab.writeSuCommand("busybox mount -o remount,ro /dev/block/mmcblk0p1 /system");
				SystemClock.sleep(50);
		    	break;
		    case R.id.section2_bt_asusbalancedmode:
		    	if(apm.saveToAsusPowerModes(2, enCore.GPU) == 0)
		    	{
			    	if(apm.readFromAsusPowerModes(2, enCore.GPU) == 0)
			    	{
			    		Toast.makeText(getApplicationContext(), "Successful...", Toast.LENGTH_SHORT).show();
			    	}
			    	else
			    	{
			    		Toast.makeText(getApplicationContext(), "Error...", Toast.LENGTH_SHORT).show();
			    	}
		    	}
		    	else
		    	{
		    		Toast.makeText(getApplicationContext(), "Error...", Toast.LENGTH_SHORT).show();
		    	}
				ab.writeSuCommand("busybox mount -o remount,ro /dev/block/mmcblk0p1 /system");
				SystemClock.sleep(50);
		    	break;
		    case R.id.section2_bt_asusperformancemode:
		    	if(apm.saveToAsusPowerModes(3, enCore.GPU) == 0)
		    	{
			    	if(apm.readFromAsusPowerModes(3, enCore.GPU) == 0)
			    	{
			    		Toast.makeText(getApplicationContext(), "Successful...", Toast.LENGTH_SHORT).show();
			    	}
			    	else
			    	{
			    		Toast.makeText(getApplicationContext(), "Error...", Toast.LENGTH_SHORT).show();
			    	}
		    	}
		    	else
		    	{
		    		Toast.makeText(getApplicationContext(), "Error...", Toast.LENGTH_SHORT).show();
		    	}
				ab.writeSuCommand("busybox mount -o remount,ro /dev/block/mmcblk0p1 /system");
				SystemClock.sleep(50);
		    	break;
		    case R.id.section2_bt_apply:
		    	try
		    	{
		    		if(cs.writeGpuFrequencys() == 0)
		    		{
		    			SystemClock.sleep(200);
		    			if(cs.readGpuFrequencys() == 0)
		    			{
		    				Toast.makeText(getApplicationContext(), "Successful...\nGPU Voltage: " + current_voltage_gpu + "mV", Toast.LENGTH_SHORT).show();
		    			}
		    		}
		    	}
		    	catch(Exception e)
		    	{
		    		Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
		    	}
		        break;
		    case R.id.section2_bt_read:
		    	try
		    	{
		    		if(cs.readGpuFrequencys() == 0)
		    		{
		    			Toast.makeText(getApplicationContext(), "Successful...", Toast.LENGTH_SHORT).show();
		    		}
		    		else
		    		{
		    			Toast.makeText(getApplicationContext(), "Error...", Toast.LENGTH_SHORT).show();
		    		}
		    	}
		    	catch(Exception e)
		    	{
		    		Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
		    	}
		        break;
		    case R.id.section3_bt_gamingmode:
		    	try
		    	{
		    		ab.writeSuCommand("echo \"1350\" > /sys/kernel/tegra_cap/core_cap_level");
	    	    	ab.writeSuCommand("echo \"1\" > /sys/kernel/tegra_cap/core_cap_state");
	    	    	ab.writeSuCommand("echo \"1700000\" > /sys/module/cpu_tegra/parameters/pwr_cap_limit_1");
	    	    	ab.writeSuCommand("echo \"1600000\" > /sys/module/cpu_tegra/parameters/pwr_cap_limit_2");
	    	    	ab.writeSuCommand("echo \"1600000\" > /sys/module/cpu_tegra/parameters/pwr_cap_limit_3");
	    	    	ab.writeSuCommand("echo \"1600000\" > /sys/module/cpu_tegra/parameters/pwr_cap_limit_4");
		    		
	    	    	if(
		    				   (Integer.valueOf(ab.readSuCommand("cat /sys/kernel/tegra_cap/core_cap_level", 1)) == 1350)
		    				&& (Integer.valueOf(ab.readSuCommand("cat /sys/module/cpu_tegra/parameters/pwr_cap_limit_1", 1)) == 1700000)
		    				&& (Integer.valueOf(ab.readSuCommand("cat /sys/module/cpu_tegra/parameters/pwr_cap_limit_2", 1)) == 1600000)
		    				&& (Integer.valueOf(ab.readSuCommand("cat /sys/module/cpu_tegra/parameters/pwr_cap_limit_3", 1)) == 1600000)
		    				&& (Integer.valueOf(ab.readSuCommand("cat /sys/module/cpu_tegra/parameters/pwr_cap_limit_4", 1)) == 1600000))
		    		{
		    			Toast.makeText(getApplicationContext(), "Successfully activated gaming mode!", Toast.LENGTH_SHORT).show();
		    		}
		    		else
		    		{
		    			Toast.makeText(getApplicationContext(), "Error... Could not activate gaming mode...", Toast.LENGTH_SHORT).show();
		    		}
		    	}
		    	catch(Exception e)
		    	{
		    		Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
		    	}
		        break;
		    default:
		       break;
		    }   

		}
	}

	public void addListToSpinner(Spinner sp, List<String> list)
	{
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sp.setAdapter(dataAdapter);
	}
	
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	}	
}
