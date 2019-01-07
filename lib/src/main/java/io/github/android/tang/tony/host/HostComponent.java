package io.github.android.tang.tony.host;

import android.content.Context;

import dagger.BindsInstance;
import dagger.Component;
import io.github.android.tang.tony.host.config.Config;

@HostScope
@Component(modules = HostModule.class)
interface HostComponent {

    void inject(Host host);

    Config config();

    Context context();

    HostService.HostServiceComponent.Builder hostServiceComponentBuilder();

    void inject(ServiceAbortionActionBroadcastReceiver broadcastReceiver);

    @Component.Builder
    interface Builder {
        HostComponent build();

        @BindsInstance
        Builder config(Config config);

    }
}
