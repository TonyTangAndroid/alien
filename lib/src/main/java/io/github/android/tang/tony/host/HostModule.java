package io.github.android.tang.tony.host;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import dagger.Module;
import dagger.Provides;
import io.github.android.tang.tony.host.config.Config;
import io.github.android.tang.tony.host.config.NotificationConfig;
import javax.inject.Named;

@Module
class HostModule {

  @Provides
  @HostScope
  Application application(Config config) {
    return config.application();
  }

  @Provides
  @HostScope
  NotificationConfig notificationConfig(Config config) {
    return config.notificationConfig();
  }

  @Provides
  @HostScope
  NotificationManager notificationManager(Context context) {
    return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
  }

  @Provides
  @HostScope
  Context context(Application application) {
    return application;
  }

  @Provides
  @HostScope
  @Named("application_id")
  String applicationId(Context context) {
    return context.getApplicationContext().getPackageName();
  }

  @Provides
  @HostScope
  SharedPreferences sharedPreferences(Context context) {
    return context.getSharedPreferences("host.xml", Context.MODE_PRIVATE);
  }
}
