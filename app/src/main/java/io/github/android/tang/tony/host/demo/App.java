package io.github.android.tang.tony.host.demo;

import android.app.Application;

import hugo.weaving.DebugLog;
import io.github.android.tang.tony.file.logger.FileLogger;
import io.github.android.tang.tony.host.Host;
import io.github.android.tang.tony.host.HostServiceStatusTracker;

@DebugLog
public class App extends Application {
    private HostServiceStatusTracker helper;

    @Override
    public void onCreate() {
        super.onCreate();
        FileLogger.init(this);
        Host.init(this);
        helper = new HostServiceStatusTracker(this);
    }

    public HostServiceStatusTracker getHelper() {
        return helper;
    }
}
