package com.rbricks.appsharing;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void onShare(View v){
        shareDialog();
    }
    public void shareDialog(){
        final List<String> packages=new ArrayList<String>();
        Intent shareIntent=new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        final List<ResolveInfo> resInfosNew=new ArrayList<ResolveInfo>();
        final List<ResolveInfo> resInfos=getPackageManager().queryIntentActivities(shareIntent, 0);
        resInfosNew.addAll(resInfos);
        if(!resInfos.isEmpty()) {
            System.out.println("Have package");
            int count=0;
            for (ResolveInfo resInfo : resInfos) {
                String packageName=resInfo.activityInfo.packageName;
                if(packageName.contains("com.facebook.katana")){
                    resInfosNew.remove(count);
                }else
                    packages.add(packageName);
                count++;
            }
        }

        if (packages.size() > 1) {
            ArrayAdapter<String> adapter = new ChooserArrayAdapter(this, android.R.layout.select_dialog_item, android.R.id.text1, packages);

            new AlertDialog.Builder(this)
                    .setTitle("Share offer with...")
                    .setAdapter(adapter, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item ) {
                            invokeApplication(packages.get(item),resInfosNew.get(item));
                        }
                    })
                    .show();
        } else if (packages.size() == 1) {
            invokeApplication(packages.get(0), resInfos.get(0));
        }
    }

    private void invokeApplication(String packageName, ResolveInfo resolveInfo) {
        // if(packageName.contains("com.twitter.android") || packageName.contains("com.facebook.katana") || packageName.contains("com.kakao.story")) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(packageName, resolveInfo.activityInfo.name));
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Hi guys, I found amazing thing to share. Send your love and care in form of gifts to your loved ones from anywhere in world. Log on to giftjaipur.com or download app" + "https://goo.gl/YslIVT" +"and use coupon code app50 to get Rs 50 off on your first purchase.");
        intent.putExtra(Intent.EXTRA_SUBJECT, "GiftJaipur 50 Rs Coupon...");
        intent.setPackage(packageName);

        startActivity(intent);
        // }
    }


}
