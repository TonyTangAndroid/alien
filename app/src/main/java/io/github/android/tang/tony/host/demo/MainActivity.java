package io.github.android.tang.tony.host.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import io.github.android.tang.tony.host.Host;
import io.github.android.tang.tony.host.ServiceStatusBroadcastReceiver;
import io.github.android.tang.tony.host.SharedPreferenceHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ServiceStatusBroadcastReceiver.Callback {

    private TextView tv_hint;
    private Button btn_action;
    private ServiceStatusBroadcastReceiver receiver = new ServiceStatusBroadcastReceiver(this);

    public static Intent constructIntent(Context context) {
        return new Intent(context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ServiceStatusBroadcastReceiver.register(this, receiver);
        updateUI(((App) getApplication()).getHelper().started());
    }

    @Override
    protected void onPause() {
        super.onPause();
        ServiceStatusBroadcastReceiver.unregister(this, receiver);
    }

    private void startForegroundService() {
        Host.startDemoServiceOnForeground(this);
    }

    private void stopForegroundService() {
        Host.stopDemoService(this);
    }

    private void updateUI(boolean started) {
        if (started) {
            tv_hint.setText(R.string.tv_hint_stop_service);
            btn_action.setText(R.string.stop);
        } else {
            tv_hint.setText(R.string.tv_hint_start_service);
            btn_action.setText(R.string.start);
        }
    }

    private void bindView() {
        tv_hint = findViewById(R.id.tv_hint);
        btn_action = findViewById(R.id.btn_action);
        btn_action.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        boolean started = ((App) getApplication()).getHelper().started();
        if (started) {
            stopForegroundService();
        } else {
            startForegroundService();
        }
        updateServiceEnabledStatus(!started);
    }

    private void updateServiceEnabledStatus(boolean enabled) {
        new SharedPreferenceHelper(getApplicationContext()).update(enabled);
    }

    @Override
    public void onUpdate(boolean started) {
        updateUI(started);
    }
}
