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
import io.github.android.tang.tony.host.HostStatus;
import io.github.android.tang.tony.host.HostStatusCallback;
import io.github.android.tang.tony.host.Status;

@DebugLog
public class MainActivity extends AppCompatActivity implements HostStatusCallback {

    @BindView(R.id.tv_hint)
    TextView tv_hint;
    @BindView(R.id.btn_activate)
    Button btn_activate;
    @BindView(R.id.btn_pause)
    Button btn_pause;
    @BindView(R.id.btn_resume)
    Button btn_resume;
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
        Host.get().addRegister(this);
        reduce(Host.get().status());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Host.get().removeRegister(this);
    }

    @DebugLog
    private void reduce(int status) {
        switch (status) {
            case Status.NONE:
                bindToBeBorn();
                break;
            case Status.SLEEP:
                bindDeactivated();
                break;
            case Status.ALIVE:
                bindAliveState();
                break;
        }
    }

    private void bindAliveState() {
        tv_hint.setText(R.string.tv_hint_stop_service);
        btn_activate.setEnabled(false);
        btn_resume.setEnabled(false);
        btn_destruct.setEnabled(true);
        btn_pause.setEnabled(true);
    }

    private void bindToBeBorn() {
        tv_hint.setText(R.string.tv_hint_start_service);
        btn_activate.setEnabled(true);
        btn_resume.setEnabled(false);
        btn_destruct.setEnabled(false);
        btn_pause.setEnabled(false);
    }

    private void bindDeactivated() {
        tv_hint.setText(R.string.tv_hint_start_service);
        btn_activate.setEnabled(false);
        btn_resume.setEnabled(true);
        btn_destruct.setEnabled(false);
        btn_pause.setEnabled(false);
    }


    @OnClick(R.id.btn_destruct)
    public void destruct(View v) {
        Host.get().destruct();
    }

    @OnClick({R.id.btn_activate, R.id.btn_resume})
    public void activate(View v) {
        Host.get().activate();
    }

    @OnClick(R.id.btn_pause)
    public void deactivate(View v) {
        Host.get().sleep();
    }

    @MainThread
    @Override
    public void onUpdate(@HostStatus int status) {
        reduce(status);
    }

}
