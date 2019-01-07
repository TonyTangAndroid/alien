package io.github.android.tang.tony.host;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import androidx.annotation.MainThread;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import hugo.weaving.DebugLog;
import io.github.android.tang.tony.host.config.Config;
import io.github.android.tang.tony.host.config.NotificationConfig;
import io.github.android.tang.tony.host.config.NotificationConfigBuilder;
import timber.log.Timber;

@DebugLog
public class Host implements HostMutationCallback {
    @Inject
    Agent agent;
    @Inject
    Config config;
    @Inject
    Context context;
    @Inject
    @Named("application_id")
    String applicationId;


    Set<HostMutationCallback> hostMutationCallbacks = new HashSet<>();
    private HostComponent hostComponent;
    @HostStatus
    private int status;

    @DebugLog
    private Host() {
    }

    public static Host get() {
        return HostHolder.INSTANCE;
    }

    @SuppressWarnings("unused")
    public static void init(Application application) {
        get().initialize(defaultConfig(application));
    }

    private static Config defaultConfig(Application application) {
        NotificationConfig notificationConfig = NotificationConfigBuilder.defaultConfig(application);
        return Config.builder().notificationConfig(notificationConfig).application(application).build();
    }

    public static void init(Config config) {
        get().initialize(config);
    }

    public void addRegister(HostMutationCallback callback) {
        if (!hostMutationCallbacks.add(callback)) {
            Timber.w("Callback has already been registered.");
        }
    }

    private void register() {
        IntentFilter filter = new IntentFilter(BuildConfig.ACTION_MUTATE_HOST);
        BroadcastReceiver receiver = new HostStatusBroadcastReceiver(this);
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filter);
    }

    public void removeRegister(HostMutationCallback callback) {
        if (!hostMutationCallbacks.remove(callback)) {
            Timber.w("Callback has not been registered.");
        }
    }

    public HostComponent hostComponent() {
        return hostComponent;
    }

    @MainThread
    @Override
    public void onMutate(@HostStatus int newStatus) {
        this.status = newStatus;
        for (HostMutationCallback callback : hostMutationCallbacks) {
            callback.onMutate(newStatus);
        }
    }

    private void initialize(Config config) {
        hostComponent = DaggerHostComponent.builder().config(config).build();
        hostComponent.inject(this);
        register();

    }

    public void activate() {
        broadcast(Action.ACTIVATE);
    }

    private void broadcast(int activate) {
        Intent intent = HostMutator.constructIntent(applicationId, activate);
        context.sendBroadcast(intent);
    }

    public void deactivate() {
        broadcast(Action.DEACTIVATE);
    }

    public void destruct() {
        broadcast(Action.DESTRUCT);
    }

    public void restore() {
        Timber.d("Attempting to restore host.");
        if (alive()) {
            Timber.v("Host has already become alive.");
        } else {
            restore(agent.status());
        }

    }

    public void revive() {
        Timber.d("Attempting to revive host.");
        if (enabled()) {
            revive(agent.status());
        } else {
            Timber.d("Revive is not enabled.");
        }
    }

    private void revive(HostStatusCache.InternalStatus status) {
        switch (status) {
            case NONE:
                toBeActivated();
                break;
            case ON_CALL:
                toBeWakenUp();
                break;
            case ACTIVATED:
                reviveThroughAgent();
                break;
        }
    }

    private boolean alive() {
        return status() == Status.ALIVE;
    }

    private void restore(HostStatusCache.InternalStatus status) {
        switch (status) {
            case NONE:
                toBeActivated();
                break;
            case ON_CALL:
                toBeWakenUp();
                break;
            case ACTIVATED:
                reviveThroughAgent();
                break;
        }
    }

    private void reviveThroughAgent() {
        agent.revive();
    }

    private void toBeWakenUp() {
        onMutate(Status.SLEEP);
        Timber.d("Host had been manually put into sleep. Hence it requires to be waken up manually to become alive.");
    }

    private void toBeActivated() {
        onMutate(Status.NONE);
        Timber.d("Host has not been born yet. It requires to be activated to become alive.");
    }

    private boolean enabled() {
        return !config.disableRevive();
    }


    @HostStatus
    public int status() {
        return status;
    }

    private static class HostHolder {
        @SuppressLint("StaticFieldLeak")
        private static final Host INSTANCE = new Host();
    }
}
