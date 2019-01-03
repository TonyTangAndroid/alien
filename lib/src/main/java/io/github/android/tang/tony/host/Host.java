package io.github.android.tang.tony.host;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import timber.log.Timber;

public class Host implements ServiceStatusBroadcastReceiver.Callback {
    @Inject
    Context context;
    @Inject
    SharedPreferenceHelper sharedPreferenceHelper;
    private HostComponent hostComponent;

    Map<ServiceStatusBroadcastReceiver.Callback, BroadcastReceiver> map = new HashMap<>();
    private boolean alive;

    private Host() {

    }


    public void register(ServiceStatusBroadcastReceiver.Callback callback) {
        if (!map.containsKey(callback)) {
            IntentFilter filter = new IntentFilter(BuildConfig.ACTION_STOP_FOREGROUND_SERVICE);
            BroadcastReceiver receiver = new ServiceStatusBroadcastReceiver(callback);
            LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filter);
            map.put(callback, receiver);
        } else {
            Timber.w("Callback has already been registered.");
        }
    }

    public void deregister(ServiceStatusBroadcastReceiver.Callback callback) {
        BroadcastReceiver registered = map.remove(callback);
        if (registered != null) {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(registered);
        } else {
            Timber.w("Callback has not been registered.");
        }
    }


    public HostComponent hostComponent() {
        return hostComponent;
    }

    @Override
    public void onUpdate(boolean alive) {
        this.alive = alive;
    }

    private static class HostHolder {
        @SuppressLint("StaticFieldLeak")
        private static final Host INSTANCE = new Host();
    }

    public static Host get() {
        return HostHolder.INSTANCE;
    }

    public static void init(Application application) {
        get().initialize(application);
    }

    private void initialize(Application application) {
        hostComponent = DaggerHostComponent.builder().application(application).build();
        hostComponent.inject(this);
        mutate(sharedPreferenceHelper.enabled());
    }

    public void toggleStatus() {
        boolean previousStatus = sharedPreferenceHelper.enabled();
        boolean newStatus = !previousStatus;
        mutate(newStatus);
        sharedPreferenceHelper.update(newStatus);
    }

    private void mutate(boolean newStatus) {
        if (newStatus) {
            deliver();
        } else {
            destruct();
        }
    }

    public boolean alive() {
        return alive;
    }

    private void deliver() {
        context.startService(HostService.constructHostIntent(context));
    }

    private void destruct() {
        context.stopService(HostService.constructHostIntent(context));
    }
}
