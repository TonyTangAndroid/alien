package org.altbeacon.beacon.demo.monitor;

import android.app.Application;
import hugo.weaving.DebugLog;
import io.github.android.tang.tony.file.logger.FileLogger;
import io.github.android.tang.tony.host.Alien;
import io.github.android.tang.tony.host.Host;
import io.github.android.tang.tony.host.config.Config;
import io.github.android.tang.tony.host.config.NotificationConfig;
import io.github.android.tang.tony.host.config.NotificationConfigBuilder;

@DebugLog
public class App extends Application {

  private Alien beaconAlien;

  @Override
  public void onCreate() {
    super.onCreate();
    FileLogger.init(this);
    Host.init(config());
    beaconAlien = new BeaconAlien(this);
  }

  private Config config() {
    NotificationConfig notificationConfig = NotificationConfigBuilder.defaultConfig(this);
    return Config.builder().notificationConfig(notificationConfig).application(this).build();
  }

  public Alien beaconAlien() {
    return beaconAlien;
  }
}
