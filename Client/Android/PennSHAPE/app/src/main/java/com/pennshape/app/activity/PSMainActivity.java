package com.pennshape.app.activity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;

import com.pennshape.app.R;
import com.pennshape.app.fragment.PSAnalyticsFragmentTab;
import com.pennshape.app.fragment.PSChatMessageFragmentTab;
import com.pennshape.app.fragment.PSDailyUpdateFragmentTab;
import com.pennshape.app.fragment.PSFriendsFragmentTab;
import com.pennshape.app.fragment.PSSettingFragmentTab;
import com.pennshape.app.location.PSLocationManager;
import com.pennshape.app.model.PSDataStore;
import com.pennshape.app.service.PSMessagesService;

import java.util.Calendar;


public class PSMainActivity extends FragmentActivity {
    private FragmentTabHost mTabHost;
    private static final long REPEAT_TIME = 1000 * 60;
    private BroadcastReceiver receiver =
            new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String message = intent.getStringExtra(PSMessagesService.IntentExtra);
                    alertNewMessages(message);
                }
            };

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
                PSChatMessageFragmentTab.class, null);
//        mTabHost.addTab(
//                mTabHost.newTabSpec("tabSetting").setIndicator(null, ContextCompat.getDrawable(this, R.drawable.tab_setting_selector)),
//                PSSettingFragmentTab.class, null);

        //Init Database
        PSDataStore.getInstance().initDatabase(PSMainActivity.this);
        //Init Messages Service
        startMessagesService(REPEAT_TIME);
        //Get intent extra
        triggeredFromNotification(getIntent());
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

    @Override
    public void onResume(){
        super.onResume();
        //upload location
        PSLocationManager.sharedManager().fireLocation();
        //Broadcast receiver
        registerReceiver(receiver, new IntentFilter(PSMessagesService.NewMessageAction));
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        triggeredFromNotification(intent);
    }

    private void startMessagesService(long interval) {
        Context context = PSMainActivity.this;
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, PSMessagesService.class);
        PendingIntent pending = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 5);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), interval, pending);
    }

    private void triggeredFromNotification(Intent i){
        String extra = i.getStringExtra(Intent.EXTRA_TEXT);
        if(extra != null && extra.equals(getString(R.string.ps_service_message_intent_extra))){
            mTabHost.setCurrentTabByTag("tabChat");
        }
    }

    private void alertNewMessages(String message) {
        if(mTabHost.getCurrentTabTag().equals("tabChat")){
            PSChatMessageFragmentTab chatFragTab = (PSChatMessageFragmentTab)getSupportFragmentManager().findFragmentByTag("tabChat");
            chatFragTab.updateMessage();
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle(message)
                .setMessage("Check your messages now?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mTabHost.setCurrentTabByTag("tabChat");
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(R.mipmap.ic_launcher)
                .show();
    }
}
