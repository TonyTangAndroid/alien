package io.github.android.tang.tony.host;

import android.content.Context;

public class Host {

    public static void init(Context context) {
        if (new SharedPreferenceHelper(context).enabled()) {
            Host.startDemoServiceOnForeground(context);
        }
    }

    public static void startDemoServiceOnForeground(Context context) {
        context.startService(HostService.constructDemoService(context));
    }

    public static void stopDemoService(Context context) {
        context.stopService(HostService.constructDemoService(context));
    }
}
