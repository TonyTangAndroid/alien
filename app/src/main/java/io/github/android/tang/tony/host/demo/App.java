package io.github.android.tang.tony.host.demo;

import android.app.Application;

import hugo.weaving.DebugLog;
import io.github.android.tang.tony.file.logger.FileLogger;
import io.github.android.tang.tony.host.DemoServiceStatusTracker;

@DebugLog
public class App extends Application {
    private DemoServiceStatusTracker helper;

    @Override
    public void onCreate() {
        super.onCreate();
        FileLogger.init(this);
        helper = new DemoServiceStatusTracker(this);
    }

    public DemoServiceStatusTracker getHelper() {
        return helper;
    }
}
