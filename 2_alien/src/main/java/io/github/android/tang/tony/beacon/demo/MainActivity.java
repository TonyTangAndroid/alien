package io.github.android.tang.tony.beacon.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hugo.weaving.DebugLog;
import io.github.android.tang.tony.host.Alien;
import io.github.android.tang.tony.host.Host;

@DebugLog
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_hint)
    TextView tv_hint;
    @BindView(R.id.btn_scan)
    Button btn_scan;
    @BindView(R.id.btn_stop)
    Button btn_stop;
    private BroadcastReceiver alienStatusBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onAlienStatusUpdated(BeaconAlien.status(intent));
        }
    };

    private void onAlienStatusUpdated(boolean alive) {
        if (alive) {
            alienIsActivated();
        } else {
            alienToBeActivated();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshAlienStatus();
        registerAlienStatusUpdate();
    }

    private void refreshAlienStatus() {
        onAlienStatusUpdated(beaconAlien().status());
    }

    private void registerAlienStatusUpdate() {
        LocalBroadcastManager.getInstance(this).registerReceiver(alienStatusBroadcastReceiver, BeaconAlien.ALIEN_STATUS_UPDATE);
    }

    private void unregisterAlienStatusUpdate() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(alienStatusBroadcastReceiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterAlienStatusUpdate();
    }

    private void alienIsActivated() {
        tv_hint.setText(R.string.beacon_being_scanned);
        btn_scan.setEnabled(false);
        btn_stop.setEnabled(true);
    }

    private void alienToBeActivated() {
        tv_hint.setText(R.string.beacon_not_being_scanned);
        btn_scan.setEnabled(true);
        btn_stop.setEnabled(false);
    }

    @OnClick({R.id.btn_scan})
    public void register(View v) {
        Host.get().activateAlien(beaconAlien());
    }

    private Alien beaconAlien() {
        return ((App) getApplication()).beaconAlien();
    }

    @OnClick(R.id.btn_stop)
    public void deregister(View v) {
        Host.get().deactivateAlien(beaconAlien());
    }
}
