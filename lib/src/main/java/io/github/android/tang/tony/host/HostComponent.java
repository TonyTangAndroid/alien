package io.github.android.tang.tony.host;

import android.app.Application;
import android.content.Context;

import dagger.BindsInstance;
import dagger.Component;

@HostScope
@Component(modules = HostModule.class)
interface HostComponent {

    void inject(Host host);

    Context context();

    @Component.Builder
    interface Builder {
        HostComponent build();

        @BindsInstance
        Builder application(Application application);

    }
}
