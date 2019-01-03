package io.github.android.tang.tony.host;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import javax.inject.Inject;

public class Host {
    @Inject
    Context context;
    @Inject
    HostStatusTracker hostStatusTracker;
    @Inject
    SharedPreferenceHelper sharedPreferenceHelper;
    private HostComponent hostComponent;

    private Host() {

    }

    public HostComponent hostComponent() {
        return hostComponent;
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
            startDemoServiceOnForeground();
        } else {
            stopDemoService();
        }
    }

    public boolean alive() {
        return hostStatusTracker.alive();
    }

    private void startDemoServiceOnForeground() {
        context.startService(HostService.constructHostIntent(context));
    }

    private void stopDemoService() {
        context.stopService(HostService.constructHostIntent(context));
    }
}
