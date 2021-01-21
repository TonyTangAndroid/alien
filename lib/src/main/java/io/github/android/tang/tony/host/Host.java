package io.github.android.tang.tony.host;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.annotation.MainThread;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import hugo.weaving.DebugLog;
import io.github.android.tang.tony.host.config.Config;
import io.github.android.tang.tony.host.config.NotificationConfig;
import io.github.android.tang.tony.host.config.NotificationConfigBuilder;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Named;
import timber.log.Timber;

@DebugLog
public class Host implements HostStatusCallback {
  @Inject Agent agent;
  @Inject Config config;
  @Inject Context context;

  @Inject
  @Named("application_id")
  String applicationId;

  private final Set<HostStatusCallback> hostStatusCallbacks = new HashSet<>();
  private final Set<Alien> aliens = new HashSet<>();
  private HostComponent hostComponent;
  @HostStatus private int status;

  @DebugLog
  private Host() {}

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

  public void registerHostStatusCallback(HostStatusCallback callback) {
    if (!hostStatusCallbacks.add(callback)) {
      Timber.w("Callback has already been registered.");
    }
  }

  public void activateAlien(Alien alien) {
    if (!aliens.add(alien)) {
      Timber.w("Alien has already been registered.");
    }
    if (status() != Status.ACTIVATED) {
      activate();
    } else {
      alien.onStatusUpdate(true);
    }
  }

  public void deactivateAlien(Alien alien) {
    if (!aliens.remove(alien)) {
      Timber.w("Alien has never been registered.");
    } else {
      alien.onStatusUpdate(false);
      if (aliens.size() == 0) {
        destruct();
      }
    }
  }

  private void register() {
    IntentFilter filter = new IntentFilter(BuildConfig.ACTION_MUTATE_HOST);
    BroadcastReceiver receiver = new HostStatusBroadcastReceiver(this);
    LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filter);
  }

  public void deregisterHostStatusCallback(HostStatusCallback callback) {
    if (!hostStatusCallbacks.remove(callback)) {
      Timber.w("Callback has not been registered.");
    }
  }

  HostComponent hostComponent() {
    return hostComponent;
  }

  @MainThread
  @Override
  public void onMutate(@HostStatus int newStatus) {
    this.status = newStatus;
    for (HostStatusCallback callback : hostStatusCallbacks) {
      callback.onMutate(newStatus);
    }
    boolean alive = status() == Status.ACTIVATED;
    for (Alien alien : aliens) {
      alien.onStatusUpdate(alive);
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
      Timber.v("Host has already been activated.");
    } else {
      agent.revive();
    }
  }

  public void revive() {
    Timber.d("Attempting to revive host.");
    if (enabled()) {
      agent.revive();
    } else {
      Timber.d("Revive is not enabled.");
    }
  }

  private boolean alive() {
    return status() == Status.ACTIVATED;
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
