package io.github.android.tang.tony.host.demo;

import android.app.Application;

import hugo.weaving.DebugLog;
import io.github.android.tang.tony.file.logger.FileLogger;
import io.github.android.tang.tony.host.Host;
import io.github.android.tang.tony.host.IHost;
import io.github.android.tang.tony.host.config.Config;
import io.github.android.tang.tony.host.config.NotificationConfig;
import io.github.android.tang.tony.host.config.NotificationConfigBuilder;
import timber.log.Timber;

@DebugLog
public class App extends Application implements IHost {

    @Override
    public void onCreate() {
        super.onCreate();
        FileLogger.init(this);
        Host.init(config());
    }

    private Config config() {
        NotificationConfig notificationConfig = NotificationConfigBuilder.defaultConfig(this);
        return Config.builder().notificationConfig(notificationConfig)
                .application(this).build();
    }

    @Override
    public void onAlive() {
        Timber.i(getString(R.string.on_alive_log));
    }

    @Override
    public void onDeceased() {
        Timber.i(getString(R.string.on_deceased_log));
    }
}
