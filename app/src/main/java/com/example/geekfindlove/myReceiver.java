package com.example.geekfindlove;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class myReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, MyNewIntentService.class);
        context.startService(intent1);
    }
}
