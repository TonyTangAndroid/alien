package io.github.android.tang.tony.host;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import javax.inject.Inject;

public class Host {
    @Inject
    Context context;
    @Inject
    SharedPreferenceHelper sharedPreferenceHelper;

    private Host() {

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
        DaggerHostComponent.builder().application(application).build().inject(this);
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
            startDemoServiceOnForeground();
        } else {
            stopDemoService();
        }
    }

    public boolean isLive() {
        return sharedPreferenceHelper.enabled();
    }

    private void startDemoServiceOnForeground() {
        context.startService(HostService.constructDemoService(context));
    }

    private void stopDemoService() {
        context.stopService(HostService.constructDemoService(context));
    }
}
