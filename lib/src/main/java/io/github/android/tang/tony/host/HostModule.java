package io.github.android.tang.tony.host;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
class HostModule {

    @Provides
    @HostScope
    Context context(Application application){
        return application;
    }
}
