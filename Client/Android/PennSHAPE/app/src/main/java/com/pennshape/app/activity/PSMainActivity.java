package com.pennshape.app.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;

import com.pennshape.app.R;
import com.pennshape.app.fragment.PSAnalyticsFragmentTab;
import com.pennshape.app.fragment.PSDailyUpdateFragmentTab;
import com.pennshape.app.fragment.PSFriendsFragmentTab;


public class PSMainActivity extends FragmentActivity {
    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ps_activity_main);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(
                mTabHost.newTabSpec("tabData").setIndicator(null, ContextCompat.getDrawable(this, R.drawable.tab_data_selector)),
                PSAnalyticsFragmentTab.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tabFriends").setIndicator(null, ContextCompat.getDrawable(this, R.drawable.tab_friends_selector)),
                PSFriendsFragmentTab.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tabAdd").setIndicator(null, ContextCompat.getDrawable(this, R.drawable.tab_add_selector)),
                PSDailyUpdateFragmentTab.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tabChat").setIndicator(null, ContextCompat.getDrawable(this, R.drawable.tab_chat_selector)),
                PSAnalyticsFragmentTab.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tabSetting").setIndicator(null, ContextCompat.getDrawable(this, R.drawable.tab_setting_selector)),
                PSAnalyticsFragmentTab.class, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ps_menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
