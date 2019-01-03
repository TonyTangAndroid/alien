package io.github.android.tang.tony.host;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import hugo.weaving.DebugLog;

@DebugLog
public class HostService extends Service {

    private NotificationHelper notificationHelper;


    static Intent constructDemoService(Context context) {
        return new Intent(context, HostService.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ServiceStatusBroadcastReceiver.broadcast(this, true);
        notificationHelper = new NotificationHelper(this);
        startForegroundService();
        onBorn();
    }

    private void onBorn() {
        Application application = getApplication();
        if (application instanceof IHost) {
            ((IHost) application).onBorn();
        }
    }

    private void startForegroundService() {
        notificationHelper.bindAsForegroundService(this);
    }

    @DebugLog
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @DebugLog
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ServiceStatusBroadcastReceiver.broadcast(this, false);
        onDeceased();
    }

    private void onDeceased() {
        Application application = getApplication();
        if (application instanceof IHost) {
            ((IHost) application).onDeceased();
        }
    }
}
