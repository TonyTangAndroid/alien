package io.github.android.tang.tony.beacon.demo;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import hugo.weaving.DebugLog;
import io.github.android.tang.tony.host.Alien;

@DebugLog
public class BeaconAlien implements Alien {

  private static final String ACTION_BROADCAST_STATUS = "action_broadcast_status";
  public static IntentFilter ALIEN_STATUS_UPDATE = new IntentFilter(ACTION_BROADCAST_STATUS);
  public static final String EXTRA_STATUS = "extra_status";
  private final Context context;

  public BeaconAlien(Context context) {
    this.context = context;
  }

  private boolean alive;

  public static boolean status(Intent intent) {
    return intent.getBooleanExtra(EXTRA_STATUS, false);
  }

  @Override
  public void onStatusUpdate(boolean alive) {
    this.alive = alive;
    broadcast();
  }

  private void broadcast() {
    Intent intent = new Intent(ACTION_BROADCAST_STATUS).putExtra(EXTRA_STATUS, alive);
    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
  }

  public boolean status() {
    return alive;
  }
}
