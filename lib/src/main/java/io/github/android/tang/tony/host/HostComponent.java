package io.github.android.tang.tony.host;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import dagger.BindsInstance;
import dagger.Component;

@HostScope
@Component(modules = HostModule.class)
interface HostComponent {

    void inject(Host host);

    Context context();

    SharedPreferences sharedPreferences();

    void inject(ReviveSignalReceiver broadcastReceiver);

    void inject(ServiceAbortionActionBroadcastReceiver broadcastReceiver);

    @Component.Builder
    interface Builder {
        HostComponent build();

        @BindsInstance
        Builder application(Application application);

    }
}
