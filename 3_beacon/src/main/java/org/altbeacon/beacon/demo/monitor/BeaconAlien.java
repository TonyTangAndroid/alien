package org.altbeacon.beacon.demo.monitor;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.RemoteException;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import hugo.weaving.DebugLog;
import io.github.android.tang.tony.host.Alien;
import java.util.List;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;
import timber.log.Timber;

@DebugLog
public class BeaconAlien implements Alien, MonitorNotifier, BeaconConsumer {

  public static final String EXTRA_STATUS = "extra_status";
  private static final String ACTION_BROADCAST_STATUS = "action_broadcast_status";
  public static IntentFilter ALIEN_STATUS_UPDATE = new IntentFilter(ACTION_BROADCAST_STATUS);
  private final Context context;
  private final BeaconNotificationHelper beaconNotificationHelper;
  private boolean alive;

  public BeaconAlien(Context context) {
    this.context = context;
    this.beaconNotificationHelper = new BeaconNotificationHelper(context);
    initBeaconScanSettings();
    bindBeaconManager();
  }

  public static boolean status(Intent intent) {
    return intent.getBooleanExtra(EXTRA_STATUS, false);
  }

  @Override
  public void onStatusUpdate(boolean alive) {
    this.alive = alive;
    broadcast();
    if (alive) {
      scanBeacon();
    } else {
      stopBeacon();
    }
  }

  private void broadcast() {
    Intent intent = new Intent(ACTION_BROADCAST_STATUS).putExtra(EXTRA_STATUS, alive);
    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
  }

  public boolean status() {
    return alive;
  }

  private void initBeaconScanSettings() {
    BeaconManager.setRegionExitPeriod(5000);
  }

  private void bindBeaconManager() {
    BeaconManager beaconManager = BeaconManager.getInstanceForApplication(context);
    beaconManager.addMonitorNotifier(this);
    beaconManager.bind(this);
  }

  private void stopBeacon() {
    deregisterBeaconToBeMonitored(UuidProvider.beaconToMonitored());
  }

  private void scanBeacon() {
    registerBeaconToBeMonitored(UuidProvider.beaconToMonitored());
  }

  @Override
  public void didEnterRegion(Region region) {
    Timber.v("enter:" + region.getUniqueId() + "\n");
    beaconNotificationHelper.showBeaconNotification(region.getUniqueId());
  }

  @Override
  public void didExitRegion(Region region) {
    Timber.v("exit:" + region.getUniqueId() + "\n");
    beaconNotificationHelper.cancel();
  }

  @Override
  public void didDetermineStateForRegion(int state, Region region) {}

  @Override
  public void onBeaconServiceConnect() {}

  @Override
  public Context getApplicationContext() {
    return context;
  }

  @Override
  public void unbindService(ServiceConnection serviceConnection) {
    context.unbindService(serviceConnection);
  }

  @Override
  public boolean bindService(Intent intent, ServiceConnection conn, int arg2) {
    return context.bindService(intent, conn, arg2);
  }

  private void registerBeaconToBeMonitored(List<String> beacons) {
    try {
      BeaconManager beaconManager = BeaconManager.getInstanceForApplication(context);
      for (String beacon : beacons) {
        Timber.v("monitor beacon : " + beacon + "\n");
        beaconManager.startMonitoringBeaconsInRegion(UuidMapper.constructRegion(beacon));
      }
    } catch (RemoteException e) {
      throw new RuntimeException(e);
    }
  }

  private void deregisterBeaconToBeMonitored(List<String> beacons) {
    try {
      BeaconManager beaconManager = BeaconManager.getInstanceForApplication(context);
      for (String beacon : beacons) {
        Timber.v("stop monitoring beacon : " + beacon + "\n");
        beaconManager.stopMonitoringBeaconsInRegion(UuidMapper.constructRegion(beacon));
      }
    } catch (RemoteException e) {
      throw new RuntimeException(e);
    }
  }
}
