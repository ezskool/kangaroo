/**
 * 
 */
package com.kangaroo.gui;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.android.kangaroo.R;

/**
 * @author mrtazz
 *
 */
public class MainWindow extends TabActivity {
	
	 private TabHost myTabHost;

	 /** Called when the activity is first created.*/
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.mainwindow);
	    myTabHost = getTabHost();
	    // get dayplan tab
	    TabSpec tabDayPlan = myTabHost.newTabSpec("Dayplan");
	    tabDayPlan.setIndicator("Dayplan");
	    Context ctx = this.getApplicationContext();
	    Intent intentDayplan = new Intent(ctx, DayPlan.class);
	    tabDayPlan.setContent(intentDayplan);
	    myTabHost.addTab(tabDayPlan);
	    // get tasklist tab
	    TabSpec tabTasklist = myTabHost.newTabSpec("Tasklist");
	    tabTasklist.setIndicator("Tasklist");
	    Intent intentTasklist = new Intent(ctx, TaskList.class);
	    tabTasklist.setContent(intentTasklist);
	    myTabHost.addTab(tabTasklist);
	    // set tab view
	    myTabHost.setCurrentTab(0);
	 }

}
