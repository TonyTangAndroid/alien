package io.github.android.tang.tony.host.config;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.Objects;

import androidx.annotation.DrawableRes;
import io.github.android.tang.tony.host.R;
import timber.log.Timber;

public abstract class NotificationConfigBuilder {

    public static NotificationConfig defaultConfig(Context context) {
        return NotificationConfig.builder()
                .channel(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? defaultChannel(context) : null)
                .launchIntent(defaultLaunchIntent(context))
                .ui(defaultUI(context))
                .build();
    }

    private static NotificationConfig.UI defaultUI(Context context) {
        return NotificationConfig.UI.builder()
                .title(defaultAppName(context))
                .body(context.getString(R.string.host_alive))
                .smallIcon(defaultIcon(context))
                .destruct(stop(context))
                .deactivate(pause(context))
                .activate(resume(context))
                .build();
    }

    private static NotificationConfig.ActionConfig stop(Context context) {
        return NotificationConfig.ActionConfig.builder()
                .title(context.getString(R.string.destruct))
                .drawableId(R.drawable.ic_stop_black_24dp)
                .build();
    }

    private static NotificationConfig.ActionConfig pause(Context context) {
        return NotificationConfig.ActionConfig.builder()
                .title(context.getString(R.string.deactivate))
                .body(context.getString(R.string.host_sleep))
                .drawableId(R.drawable.ic_pause_black_24dp)
                .build();
    }

    private static NotificationConfig.ActionConfig resume(Context context) {
        return NotificationConfig.ActionConfig.builder()
                .title(context.getString(R.string.activate))
                .body(context.getString(R.string.host_sleep))
                .drawableId(R.drawable.ic_resume_black_24dp)
                .build();
    }

    @DrawableRes
    private static int defaultIcon(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        return applicationInfo.icon;
    }

    /**
     * from https://stackoverflow.com/a/17489988/4068957
     */
    private static String defaultAppName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            String packageName = context.getPackageName();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            CharSequence applicationLabel = packageManager.getApplicationLabel(applicationInfo);
            return ((applicationInfo != null) ? String.valueOf(applicationLabel)
                    : defaultNameAsFallback(context));
        } catch (final PackageManager.NameNotFoundException e) {
            Timber.e(e);
            return defaultNameAsFallback(context);
        }
    }

    /**
     * from https://stackoverflow.com/a/15114434/4068957
     */
    private static String defaultNameAsFallback(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }


    public static Intent defaultLaunchIntent(Context context) {
        PackageManager pm = context.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(context.getPackageName());
        return Objects.requireNonNull(intent)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    public static NotificationConfig.Channel defaultChannel(Context context) {
        return NotificationConfig.Channel.builder().id("android_foreground_service_host")
                .name(context.getString(R.string.channel_name))
                .importance(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? NotificationManager.IMPORTANCE_DEFAULT : 0)
                .build();
    }
}