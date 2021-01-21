package io.github.android.tang.tony.host;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import dagger.Subcomponent;
import hugo.weaving.DebugLog;
import javax.inject.Inject;

@DebugLog
public class HostService extends Service {

  @Inject NotificationHelper notificationHelper;

  public static Intent constructHostIntent(Context context) {
    return new Intent(context, HostService.class);
  }

  @Override
  public void onCreate() {
    inject();
    super.onCreate();
    startForegroundService();
  }

  private void inject() {
    Host.get().hostComponent().hostServiceComponentBuilder().build().inject(this);
  }

  private void startForegroundService() {
    notificationHelper.showOngoingStatus(this);
  }

  @DebugLog
  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    return START_STICKY;
  }

  @DebugLog
  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    // We don't provide binding, so return null
    return null;
  }

  @Subcomponent
  interface HostServiceComponent {
    void inject(HostService service);

    @Subcomponent.Builder
    interface Builder {
      HostServiceComponent build();
    }
  }
}
