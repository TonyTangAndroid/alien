package io.github.android.tang.tony.host.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hugo.weaving.DebugLog;
import io.github.android.tang.tony.host.Host;
import io.github.android.tang.tony.host.HostStatusCallback;
import io.github.android.tang.tony.host.HostStatus;
import io.github.android.tang.tony.host.Status;

@DebugLog
public class MainActivity extends AppCompatActivity implements HostStatusCallback {

    @BindView(R.id.tv_hint)
    TextView tv_hint;
    @BindView(R.id.btn_activate)
    Button btn_activate;
    @BindView(R.id.btn_deactivate)
    Button btn_deactivate;
    @BindView(R.id.btn_destruct)
    Button btn_destruct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Host.get().restore();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Host.get().registerHostStatusCallback(this);
        reduce(Host.get().status());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Host.get().deregisterHostStatusCallback(this);
    }

    @DebugLog
    private void reduce(int status) {
        switch (status) {
            case Status.NONE:
                toBeActivated();
                break;
            case Status.DEACTIVATED:
                toBeWakenUp();
                break;
            case Status.ACTIVATED:
                bindAliveState();
                break;
        }
    }

    private void bindAliveState() {
        tv_hint.setText(R.string.tv_hint_stop_service);
        btn_activate.setEnabled(false);
        btn_destruct.setEnabled(true);
        btn_deactivate.setEnabled(true);
    }

    private void toBeActivated() {
        tv_hint.setText(R.string.tv_hint_activate_service_for_the_first_time);
        btn_activate.setEnabled(true);
        btn_destruct.setEnabled(false);
        btn_deactivate.setEnabled(false);
    }

    private void toBeWakenUp() {
        tv_hint.setText(R.string.tv_hint_activate_service_from_sleep_mode);
        btn_activate.setEnabled(true);
        btn_destruct.setEnabled(true);
        btn_deactivate.setEnabled(false);
    }


    @OnClick(R.id.btn_destruct)
    public void destruct(View v) {
        Host.get().destruct();
    }

    @OnClick({R.id.btn_activate})
    public void activate(View v) {
        Host.get().activate();
    }

    @OnClick(R.id.btn_deactivate)
    public void deactivate(View v) {
        Host.get().deactivate();
    }

    @MainThread
    @Override
    public void onMutate(@HostStatus int newStatus) {
        reduce(newStatus);
    }

}
