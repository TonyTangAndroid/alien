package io.github.android.tang.tony.host.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import io.github.android.tang.tony.host.Host;
import io.github.android.tang.tony.host.ServiceStatusBroadcastReceiver;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ServiceStatusBroadcastReceiver.Callback {

    private TextView tv_hint;
    private Button btn_action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Host.get().register(this);
        updateUI(Host.get().alive());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Host.get().deregister(this);
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
        Host.get().toggleStatus();
    }

    @Override
    public void onUpdate(boolean alive) {
        updateUI(alive);
    }
}
