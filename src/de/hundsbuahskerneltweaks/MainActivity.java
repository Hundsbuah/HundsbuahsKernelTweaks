package de.hundsbuahskerneltweaks;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.stericson.RootTools.*;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {
	
	final int MAX_DVFS_ENTRYS = 42;
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
	public Spinner available_governors;
	public Spinner available_ioschedulers;

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
    
    private Button section4_bt_set_gov;
    private Button section4_bt_set_iosched;
    private Switch section4_bt_set_on_boot;

    private LinearLayout section5_tablelayout;
	public List<TextView> section5_tv = new ArrayList<TextView>();
	public List<SeekBar> section5_sb = new ArrayList<SeekBar>();
    private Button section5_bt_apply;
    private Switch section5_bt_set_on_boot;
    private final int section5_bt_apply_id = 10001;
    private final int section5_bt_set_on_boot_id = 10002;

    private LinearLayout section6_tablelayout;
	public List<TextView> section6_tv = new ArrayList<TextView>();
	public List<SeekBar> section6_sb = new ArrayList<SeekBar>();
    private Button section6_bt_apply;
    private Switch section6_bt_set_on_boot;
    private final int section6_bt_apply_id = 10003;
    private final int section6_bt_set_on_boot_id = 10004;
    
    private Switch section7_bt_set_on_boot;
    private Button section7_bt_apply;
    public SeekBar section7_sb_rt_2;
    public SeekBar section7_sb_rt_3;
    public SeekBar section7_sb_rt_4;
    public TextView section7_tv_rt_2;
    public TextView section7_tv_rt_3;
    public TextView section7_tv_rt_4;
    
    private Button section8_bt_bootlinux;
    public TextView section8_tv_desc;
    public TextView section8_tv_download_url;
    
    private MainActivity ma = this;
    private int first_start = 1;

    private int num_current_messageboxes = 0;
	final int MAX_CURRENT_MESSAGE_BOXES = 2;
	
    public int kernel_info_done = 0;
   
    public enum enCore
    {
	   CPU,
   	   GPU,
    }
    
    public enum enOnBoot
    {
	   DISABLE,
   	   ENABLE,
    }   
    
    public int hotplug_if_not_available = 0;
    public int gpu_voltage_if_not_available = 0;
    public int cpu_voltage_if_not_available = 0;

    private AndroidBash ab;
    private Helper helper;
    private CurrentSettings cs;
    private AsusPowermodes apm;
    private Governor_IOscheduler gov_iosched;
    private InitdHelper initd;
    private Undervolting uv;
    private Hotplugging hotplugging;
    
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
			// Show 8 total pages.
			return 8;
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
			case 3:
				return getString(R.string.title_section4).toUpperCase(l);
			case 4:
				return getString(R.string.title_section5).toUpperCase(l);
			case 5:
				return getString(R.string.title_section6).toUpperCase(l);
			case 6:
				return getString(R.string.title_section7).toUpperCase(l);
			case 7:
				return getString(R.string.title_section8).toUpperCase(l);
			}
			return null;
		}
	}
	
	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	@SuppressLint("ValidFragment")
	public class SectionFragment extends Fragment implements OnClickListener, OnCheckedChangeListener, OnSeekBarChangeListener {
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
            
        	int retries = 25;

			if(first_start == 1)
			{
				
				first_start = 0;

				if (RootTools.isRootAvailable())
				{
					while (RootTools.isAccessGiven() == false && retries != 0)
					{
						SystemClock.sleep(500);
						retries--;
					}
					if(retries == 0)
					{
						showMessageBox("No su rights granted - Exiting!", 1);
						finish();
					}
				}
				else
				{
					showMessageBox("No SU found!", 1);
					finish();
				}
				
			    ab = new AndroidBash(ma);
			    helper = new Helper(ma);
			    cs = new CurrentSettings(ma);
			    apm = new AsusPowermodes(ma);
			    gov_iosched = new Governor_IOscheduler(ma);
			    initd = new InitdHelper(ma);
			    uv = new Undervolting(ma);
			    hotplugging = new Hotplugging(ma);
				
				initd.createInitdIfNotExists();
			}
				    
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

        	else if(getArguments().getInt(ARG_SECTION_NUMBER) == 3)
        	{
                rootView = inflater.inflate(R.layout.fragment_gamingmode, container, false);
        		tv_label = (TextView) rootView.findViewById(R.id.section_label);
        		section3_bt_gamingmode = (Button) rootView.findViewById(R.id.section3_bt_gamingmode);

        		section3_bt_gamingmode.setOnClickListener(this);

        		tv_label.setText(helper.getKernelInfo());
        	}
        	
        	else if(getArguments().getInt(ARG_SECTION_NUMBER) == 4)
        	{
        		rootView = inflater.inflate(R.layout.governor_iosched, container, false);   
        		tv_label = (TextView) rootView.findViewById(R.id.section_label);
        		available_governors = (Spinner) rootView.findViewById(R.id.section4_sp_governors);
        		available_ioschedulers = (Spinner) rootView.findViewById(R.id.section4_sp_ioschedulers);
        		section4_bt_set_gov = (Button) rootView.findViewById(R.id.section4_bt_set_governor);
        		section4_bt_set_iosched = (Button) rootView.findViewById(R.id.section4_bt_set_ioscheduler);
        		section4_bt_set_on_boot = (Switch) rootView.findViewById(R.id.section4_bt_set_on_boot);
        		section4_bt_set_gov.setOnClickListener(this);
        		section4_bt_set_iosched.setOnClickListener(this);
        		tv_label.setText(helper.getKernelInfo());
        		gov_iosched.getGovernors();
        		gov_iosched.getIOSchedulers();
        		
        		if(initd.checkIfInterfaceIsActivatedOnBoot(gov_iosched.ioscheduler_0) == 1)
        		{
        			section4_bt_set_on_boot.setChecked(true);
        		}
        		else
        		{
        			section4_bt_set_on_boot.setChecked(false);
        		}
        		
        		section4_bt_set_on_boot.setOnCheckedChangeListener(this);
        	}
        	
        	else if(getArguments().getInt(ARG_SECTION_NUMBER) == 5)
        	{
        		section5_sb.clear();
        		section5_tv.clear();
        		rootView = inflater.inflate(R.layout.uv_cpu, container, false);
        		tv_label = (TextView) rootView.findViewById(R.id.section_label);
        		tv_label.setText(helper.getKernelInfo());
        		section5_tablelayout = (LinearLayout) rootView.findViewById(R.id.section5_linearlayout);
        		section5_tablelayout.setHorizontalScrollBarEnabled(true);
        		section5_tablelayout.setVerticalScrollBarEnabled(true);
        		
        		int num_cpu_freqs = uv.getNumberOfFrequencies(enCore.CPU);
        		
        		if(num_cpu_freqs < 2)
        		{
        			cpu_voltage_if_not_available = 1;
        			showMessageBox("CPU UV is not available!", 1);
        		}
        		else
        		{
        			cpu_voltage_if_not_available = 0;
        		}
        		
        		LinearLayout ll = new LinearLayout(getApplicationContext());
        		ll.setOrientation(LinearLayout.VERTICAL);
        		
        		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				ll.setLayoutParams(lp);
				
				for(int i = 0; i < num_cpu_freqs; i++)
				{
					section5_tv.add(new TextView(getApplicationContext()));
					section5_tv.get(i).setText("1000000 kHz ( mV)");
					section5_tv.get(i).setTextColor(Color.BLACK);
					section5_tv.get(i).setLayoutParams(lp);
					ll.addView(section5_tv.get(i));
					
					section5_sb.add(new SeekBar(getApplicationContext()));
					//section5_sb.get(i).setLayoutParams(lp);
					section5_sb.get(i).setMax(1400);
					section5_sb.get(i).setMinimumWidth(800);
					section5_sb.get(i).setId(i);
					ll.addView(section5_sb.get(i));
				}
				
				section5_bt_apply = new Button(getApplicationContext());
				section5_bt_apply.setText(R.string.section5_bt_apply);
				section5_bt_apply.setId(section5_bt_apply_id);
				section5_bt_apply.setTextColor(Color.BLACK);
				
				section5_bt_set_on_boot = new Switch(getApplicationContext());
				section5_bt_set_on_boot.setText(R.string.section5_switch_string);
				section5_bt_set_on_boot.setTextColor(Color.BLACK);
				section5_bt_set_on_boot.setLayoutParams(lp);
				section5_bt_set_on_boot.setId(section5_bt_set_on_boot_id);
				
				ll.addView(section5_bt_apply);
				ll.addView(section5_bt_set_on_boot);
				
				section5_tablelayout.addView(ll, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				
				uv.getFrequenciesAndVoltage(enCore.CPU);
				
				if(initd.checkIfInterfaceIsActivatedOnBoot(uv.cpu_uv_if) == 1)
				{
					section5_bt_set_on_boot.setChecked(true);
				}
				else
				{
					section5_bt_set_on_boot.setChecked(false);
				}
				
				for(int i = 0; i < num_cpu_freqs; i++)
				{
					section5_sb.get(i).setOnSeekBarChangeListener(this);
				}
        		if(cpu_voltage_if_not_available == 0)
        		{
    				section5_bt_apply.setOnClickListener(this);
            		section5_bt_set_on_boot.setOnCheckedChangeListener(this);
        		}
        	}
        	else if(getArguments().getInt(ARG_SECTION_NUMBER) == 6)
        	{
        		section6_sb.clear();
        		section6_tv.clear();
        		rootView = inflater.inflate(R.layout.uv_gpu, container, false);
        		tv_label = (TextView) rootView.findViewById(R.id.section_label);
        		tv_label.setText(helper.getKernelInfo());
        		section6_tablelayout = (LinearLayout) rootView.findViewById(R.id.section6_linearlayout);
        		section6_tablelayout.setHorizontalScrollBarEnabled(true);
        		section6_tablelayout.setVerticalScrollBarEnabled(true);
        		
        		int num_gpu_freqs = uv.getNumberOfFrequencies(enCore.GPU);
        		
        		if(num_gpu_freqs < 2)
        		{
        			gpu_voltage_if_not_available = 1;
        			showMessageBox("GPU UV is not available!", 1);
        		}
        		else
        		{
        			gpu_voltage_if_not_available = 0;
        		}
        		
        		LinearLayout ll = new LinearLayout(getApplicationContext());
        		ll.setOrientation(LinearLayout.VERTICAL);
        		
        		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				ll.setLayoutParams(lp);
				
				for(int i = 0; i < num_gpu_freqs; i++)
				{
					section6_tv.add(new TextView(getApplicationContext()));
					section6_tv.get(i).setText("1000000 kHz ( mV)");
					section6_tv.get(i).setTextColor(Color.BLACK);
					section6_tv.get(i).setLayoutParams(lp);
					ll.addView(section6_tv.get(i));
					
					section6_sb.add(new SeekBar(getApplicationContext()));
					//section6_sb.get(i).setLayoutParams(lp);
					section6_sb.get(i).setMax(1500);
					section6_sb.get(i).setMinimumWidth(800);
					section6_sb.get(i).setId(i);
					ll.addView(section6_sb.get(i));
				}
				
				section6_bt_apply = new Button(getApplicationContext());
				section6_bt_apply.setText(R.string.section6_bt_apply);
				section6_bt_apply.setId(section6_bt_apply_id);
				section6_bt_apply.setTextColor(Color.BLACK);
				
				section6_bt_set_on_boot = new Switch(getApplicationContext());
				section6_bt_set_on_boot.setText(R.string.section6_switch_string);
				section6_bt_set_on_boot.setTextColor(Color.BLACK);
				section6_bt_set_on_boot.setLayoutParams(lp);
				section6_bt_set_on_boot.setId(section6_bt_set_on_boot_id);
				
				ll.addView(section6_bt_apply);
				ll.addView(section6_bt_set_on_boot);
				
				section6_tablelayout.addView(ll, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				
				uv.getFrequenciesAndVoltage(enCore.GPU);
				
				if(initd.checkIfInterfaceIsActivatedOnBoot(uv.gpu_uv_if) == 1)
				{
					section6_bt_set_on_boot.setChecked(true);
				}
				else
				{
					section6_bt_set_on_boot.setChecked(false);
				}
				
				for(int i = 0; i < num_gpu_freqs; i++)
				{
					section6_sb.get(i).setOnSeekBarChangeListener(this);
				}
        		if(gpu_voltage_if_not_available == 0)
        		{
    				section6_bt_apply.setOnClickListener(this);
            		section6_bt_set_on_boot.setOnCheckedChangeListener(this);
        		}
        	}
        	else if(getArguments().getInt(ARG_SECTION_NUMBER) == 7)
        	{
        		rootView = inflater.inflate(R.layout.hotplugging, container, false);   
        		tv_label = (TextView) rootView.findViewById(R.id.section_label);
        		section7_bt_apply = (Button) rootView.findViewById(R.id.section7_bt_apply);
        		section7_bt_set_on_boot = (Switch) rootView.findViewById(R.id.section7_bt_set_on_boot);
        		section7_sb_rt_2 = (SeekBar) rootView.findViewById(R.id.section7_sb_rt_2);
        		section7_sb_rt_3 = (SeekBar) rootView.findViewById(R.id.section7_sb_rt_3);
        		section7_sb_rt_4 = (SeekBar) rootView.findViewById(R.id.section7_sb_rt_4);
        		section7_tv_rt_2 = (TextView) rootView.findViewById(R.id.section7_tv_2);
        		section7_tv_rt_3 = (TextView) rootView.findViewById(R.id.section7_tv_3);
        		section7_tv_rt_4 = (TextView) rootView.findViewById(R.id.section7_tv_4);
        		tv_label.setText(helper.getKernelInfo());
        		
        		section7_sb_rt_2.setOnSeekBarChangeListener(this);
        		section7_sb_rt_3.setOnSeekBarChangeListener(this);
        		section7_sb_rt_4.setOnSeekBarChangeListener(this);
        		
        		if(hotplugging.getRtConfig() != 0)
        		{
        			showMessageBox("Cant read hotpluggings runnable thread config", 0);
        			hotplug_if_not_available = 1;
        		}
        		else
        		{
        			hotplug_if_not_available = 0;
        		}
        		
        		/*
        		for (int i=0; i < 6; i++)
        		{
        			Toast.makeText(getApplicationContext(), "As there are 4 cores in the system, you have to divide each of your selected values by 4 to get the real number of running threads when a core should be come online!", Toast.LENGTH_SHORT).show();
        		}*/
        		
        		section7_sb_rt_2.setMax(50);
        		section7_sb_rt_3.setMax(50);
        		section7_sb_rt_4.setMax(50);
        		
        		if(initd.checkIfInterfaceIsActivatedOnBoot(hotplugging.hotplugging_if) == 1)
        		{
        			section7_bt_set_on_boot.setChecked(true);
        		}
        		else
        		{
        			section7_bt_set_on_boot.setChecked(false);
        		}
        		if(hotplug_if_not_available == 0)
        		{
            		section7_bt_set_on_boot.setOnCheckedChangeListener(this);
            		section7_bt_apply.setOnClickListener(this);
        		}
        	}
        	else if(getArguments().getInt(ARG_SECTION_NUMBER) == 8)
        	{
        		rootView = inflater.inflate(R.layout.boot_linux, container, false);   
        		tv_label = (TextView) rootView.findViewById(R.id.section_label);
        		section8_bt_bootlinux = (Button) rootView.findViewById(R.id.section8_bt_bootlinux);
        		section8_tv_desc = (TextView) rootView.findViewById(R.id.section8_tv_desc);
        		section8_tv_download_url = (TextView) rootView.findViewById(R.id.section8_tv_download_url);
        		tv_label.setText(helper.getKernelInfo());
        		
           		section8_bt_bootlinux.setOnClickListener(this);
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
		    			SystemClock.sleep(70);
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
		    case R.id.section4_bt_set_governor:
		    	if(gov_iosched.setGovernor() == 0)
		    	{
			    	SystemClock.sleep(70);
			    	if(gov_iosched.getGovernors() == 0)
		    		{
		    			Toast.makeText(getApplicationContext(), "New Governor " + available_governors.getSelectedItem().toString() + " successfully set!", Toast.LENGTH_SHORT).show();
		    		}
			    	else
			    	{
		    			Toast.makeText(getApplicationContext(), "Error setting new Governor!", Toast.LENGTH_SHORT).show();
			    	}
		    	}
		    	else
		    	{
	    			Toast.makeText(getApplicationContext(), "Error setting new Governor!", Toast.LENGTH_SHORT).show();
		    	}
		    	break;
		    case R.id.section4_bt_set_ioscheduler:
		    	if(gov_iosched.setIOScheduler() == 0)
		    	{
			    	SystemClock.sleep(70);
			    	if(gov_iosched.getIOSchedulers() == 0)
		    		{
		    			Toast.makeText(getApplicationContext(), "New I/O Scheduler " + available_ioschedulers.getSelectedItem().toString() + " successfully set!", Toast.LENGTH_SHORT).show();
		    		}
			    	else
			    	{
		    			Toast.makeText(getApplicationContext(), "Error setting new I/O Scheduler!", Toast.LENGTH_SHORT).show();
			    	}
		    	}
		    	else
		    	{
	    			Toast.makeText(getApplicationContext(), "Error setting new I/O Scheduler!", Toast.LENGTH_SHORT).show();
		    	}
		    	break;
		    case section5_bt_apply_id:
		    {
		    	if(uv.setVoltages(enCore.CPU) == 0)
		    	{
	    			Toast.makeText(getApplicationContext(), "Setting new CPU Voltages successfully!", Toast.LENGTH_SHORT).show();
		    	}
		    	else
		    	{
	    			Toast.makeText(getApplicationContext(), "Error setting new CPU Voltages!", Toast.LENGTH_SHORT).show();
		    	}
		    	break;
		    }
		    case section6_bt_apply_id:
		    {
		    	if(uv.setVoltages(enCore.GPU) == 0)
		    	{
	    			Toast.makeText(getApplicationContext(), "Setting new GPU Voltages successfully!", Toast.LENGTH_SHORT).show();
		    	}
		    	else
		    	{
	    			Toast.makeText(getApplicationContext(), "Error setting new GPU Voltages!", Toast.LENGTH_SHORT).show();
		    	}
		    	break;
		    }
		    case R.id.section7_bt_apply:
		    {
		    	if(hotplugging.setRtConfig() == 0)
		    	{
	    			Toast.makeText(getApplicationContext(), "Setting new Hotplugging runnable thread values successfully!", Toast.LENGTH_SHORT).show();
		    	}
		    	else
		    	{
	    			Toast.makeText(getApplicationContext(), "Error setting new Hotplugging runnable threads values!", Toast.LENGTH_SHORT).show();
		    	}
		    	break;
		    }
		    case R.id.section8_bt_bootlinux:
		    {
		    	if(section8_bt_bootlinux.isClickable())
		    	{
			    	File file_hb = new File("/system/linux_hardboot/hb.sh");
			    	File file_kexec = new File("/system/linux_hardboot/kexec");
			    	File file_ramdisk = new File("/system/linux_hardboot/ramdisk.img");
			    	File file_zImage = new File("/system/linux_hardboot/zImage");
			    	if(file_hb.exists() && file_kexec.exists() && file_ramdisk.exists() && file_zImage.exists())
			    	{
			    		ab.writeSuCommand("cd /system/linux_hardboot && sh hb.sh");
			    		section8_bt_bootlinux.setClickable(false);
			    	}
			    	else
			    		showMessageBox("Missing kexec hardboot files, aborting! - Have you flashed the linux installer zip in recovery?", 1);
		    	}
		    	else
		    	{
		    		showMessageBox("You have already clicked - Please wait... If its not rebooting after 10s something went wrong :(", 1);
		    	}
		    	break;
		    }
		    default:
		       break;
		    }   

		}

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// TODO Auto-generated method stub
	    	switch (arg0.getId())
	    	{
			    case R.id.section4_bt_set_on_boot:
			    	if(section4_bt_set_on_boot.isChecked() == true)
			    	{
				    	if(gov_iosched.saveToInitdFile(enOnBoot.ENABLE) == 0)
				    	{
					    	SystemClock.sleep(70);
					    	Toast.makeText(getApplicationContext(), "\"/system/etc/init.d/99hundsapp\" successfully written!", Toast.LENGTH_SHORT).show();
					    	ab.writeSuCommand("busybox mount -o remount,ro /dev/block/mmcblk0p1 /system");
				    	}
				    	else
				    	{
				    		SystemClock.sleep(70);
			    			Toast.makeText(getApplicationContext(), "Error writing \"/system/etc/init.d/99hundsapp\" file!", Toast.LENGTH_SHORT).show();
			    			ab.writeSuCommand("busybox mount -o remount,ro /dev/block/mmcblk0p1 /system");
				    	}
			    	}
			    	else
			    	{
			    		if(gov_iosched.saveToInitdFile(enOnBoot.DISABLE) == 0)
			    		{
					    	SystemClock.sleep(70);
					    	Toast.makeText(getApplicationContext(), "\"/system/etc/init.d/99hundsapp\" successfully written!", Toast.LENGTH_SHORT).show();
					    	ab.writeSuCommand("busybox mount -o remount,ro /dev/block/mmcblk0p1 /system");
			    		}
			    		else
			    		{
				    		SystemClock.sleep(70);
			    			Toast.makeText(getApplicationContext(), "Error deleting \"/system/etc/init.d/99hundsapp\" file!", Toast.LENGTH_SHORT).show();
			    			ab.writeSuCommand("busybox mount -o remount,ro /dev/block/mmcblk0p1 /system");
			    		}
			    	}
			    	break;
			    case section5_bt_set_on_boot_id:
			    	if(section5_bt_set_on_boot.isChecked() == true)
			    	{
				    	if(uv.saveToInitdFile(enCore.CPU, enOnBoot.ENABLE) == 0)
				    	{
					    	SystemClock.sleep(70);
					    	Toast.makeText(getApplicationContext(), "\"/system/etc/init.d/99hundsapp\" successfully written!", Toast.LENGTH_SHORT).show();
					    	ab.writeSuCommand("busybox mount -o remount,ro /dev/block/mmcblk0p1 /system");
				    	}
				    	else
				    	{
				    		SystemClock.sleep(70);
			    			Toast.makeText(getApplicationContext(), "Error writing \"/system/etc/init.d/99hundsapp\" file!", Toast.LENGTH_SHORT).show();
			    			ab.writeSuCommand("busybox mount -o remount,ro /dev/block/mmcblk0p1 /system");
				    	}
			    	}
			    	else
			    	{
			    		if(uv.saveToInitdFile(enCore.CPU, enOnBoot.DISABLE) == 0)
			    		{
					    	SystemClock.sleep(70);
					    	Toast.makeText(getApplicationContext(), "\"/system/etc/init.d/99hundsapp\" successfully written!", Toast.LENGTH_SHORT).show();
					    	ab.writeSuCommand("busybox mount -o remount,ro /dev/block/mmcblk0p1 /system");
			    		}
			    		else
			    		{
				    		SystemClock.sleep(70);
			    			Toast.makeText(getApplicationContext(), "Error deleting \"/system/etc/init.d/99hundsapp\" file!", Toast.LENGTH_SHORT).show();
			    			ab.writeSuCommand("busybox mount -o remount,ro /dev/block/mmcblk0p1 /system");
			    		}
			    	}
			    	break;
			    case section6_bt_set_on_boot_id:
			    	if(section6_bt_set_on_boot.isChecked() == true)
			    	{
				    	if(uv.saveToInitdFile(enCore.GPU, enOnBoot.ENABLE) == 0)
				    	{
					    	SystemClock.sleep(70);
					    	Toast.makeText(getApplicationContext(), "\"/system/etc/init.d/99hundsapp\" successfully written!", Toast.LENGTH_SHORT).show();
					    	ab.writeSuCommand("busybox mount -o remount,ro /dev/block/mmcblk0p1 /system");
				    	}
				    	else
				    	{
				    		SystemClock.sleep(70);
			    			Toast.makeText(getApplicationContext(), "Error writing \"/system/etc/init.d/99hundsapp\" file!", Toast.LENGTH_SHORT).show();
			    			ab.writeSuCommand("busybox mount -o remount,ro /dev/block/mmcblk0p1 /system");
				    	}
			    	}
			    	else
			    	{
			    		if(uv.saveToInitdFile(enCore.GPU, enOnBoot.DISABLE) == 0)
			    		{
					    	SystemClock.sleep(70);
					    	Toast.makeText(getApplicationContext(), "\"/system/etc/init.d/99hundsapp\" successfully written!", Toast.LENGTH_SHORT).show();
					    	ab.writeSuCommand("busybox mount -o remount,ro /dev/block/mmcblk0p1 /system");
			    		}
			    		else
			    		{
				    		SystemClock.sleep(70);
			    			Toast.makeText(getApplicationContext(), "Error deleting \"/system/etc/init.d/99hundsapp\" file!", Toast.LENGTH_SHORT).show();
			    			ab.writeSuCommand("busybox mount -o remount,ro /dev/block/mmcblk0p1 /system");
			    		}
			    	}
			    	break;
			    case R.id.section7_bt_set_on_boot:
			    	if(section7_bt_set_on_boot.isChecked() == true)
			    	{
				    	if(hotplugging.saveToInitdFile(enOnBoot.ENABLE) == 0)
				    	{
					    	SystemClock.sleep(70);
					    	Toast.makeText(getApplicationContext(), "\"/system/etc/init.d/99hundsapp\" successfully written!", Toast.LENGTH_SHORT).show();
					    	ab.writeSuCommand("busybox mount -o remount,ro /dev/block/mmcblk0p1 /system");
				    	}
				    	else
				    	{
				    		SystemClock.sleep(70);
			    			Toast.makeText(getApplicationContext(), "Error writing \"/system/etc/init.d/99hundsapp\" file!", Toast.LENGTH_SHORT).show();
			    			ab.writeSuCommand("busybox mount -o remount,ro /dev/block/mmcblk0p1 /system");
				    	}
			    	}
			    	else
			    	{
			    		if(hotplugging.saveToInitdFile(enOnBoot.DISABLE) == 0)
			    		{
					    	SystemClock.sleep(70);
					    	Toast.makeText(getApplicationContext(), "\"/system/etc/init.d/99hundsapp\" successfully written!", Toast.LENGTH_SHORT).show();
					    	ab.writeSuCommand("busybox mount -o remount,ro /dev/block/mmcblk0p1 /system");
			    		}
			    		else
			    		{
				    		SystemClock.sleep(70);
			    			Toast.makeText(getApplicationContext(), "Error deleting \"/system/etc/init.d/99hundsapp\" file!", Toast.LENGTH_SHORT).show();
			    			ab.writeSuCommand("busybox mount -o remount,ro /dev/block/mmcblk0p1 /system");
			    		}
			    	}
			    	break;
			    default:
			       break;
	    	}
		}

		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			// TODO Auto-generated method stub
			
			int caller = 0;

			if(section6_sb != null)
			{
				for(caller = 0; caller < section6_sb.size(); caller++)
				{
					if(arg0.getId() == section6_sb.get(caller).getId())
					{
						section6_sb.get(caller).setProgress(((int)Math.round(arg1/25))*25);
						StringBuilder label = new StringBuilder(new String(section6_tv.get(caller).getText().toString()));
						section6_tv.get(caller).setText(label.substring(0, label.lastIndexOf("(")).toString() + "(" + String.valueOf(section6_sb.get(caller).getProgress()) + " mV)");
					}
				}
			}
			
			if(section7_sb_rt_4 != null)
			{
				if(arg0.getId() == section7_sb_rt_2.getId())
				{
					section7_sb_rt_2.setProgress(arg1);
					section7_tv_rt_2.setText(this.getString(R.string.section7_rt_2) + String.valueOf(section7_sb_rt_2.getProgress() + this.getString(R.string.section7_rt_endstring)));
				}
			}

			if(section7_sb_rt_3 != null)
			{
				if(arg0.getId() == section7_sb_rt_3.getId())
				{
					section7_sb_rt_3.setProgress(arg1);
					section7_tv_rt_3.setText(this.getString(R.string.section7_rt_3) + String.valueOf(section7_sb_rt_3.getProgress() + this.getString(R.string.section7_rt_endstring)));
				}
			}

			if(section7_sb_rt_4 != null)
			{
				if(arg0.getId() == section7_sb_rt_4.getId())
				{
					section7_sb_rt_4.setProgress(arg1);
					section7_tv_rt_4.setText(this.getString(R.string.section7_rt_4) + String.valueOf(section7_sb_rt_4.getProgress() + this.getString(R.string.section7_rt_endstring)));
				}
			}
			
			if(section5_sb != null)
			{

				for(caller = 0; caller < section5_sb.size(); caller++)
				{
					if(arg0.getId() == section5_sb.get(caller).getId())
					{
						section5_sb.get(caller).setProgress(((int)Math.round(arg1/25))*25);
						StringBuilder label = new StringBuilder(new String(section5_tv.get(caller).getText().toString()));
						section5_tv.get(caller).setText(label.substring(0, label.lastIndexOf("(")).toString() + "(" + String.valueOf(section5_sb.get(caller).getProgress()) + " mV)");
					}
				}	
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
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
