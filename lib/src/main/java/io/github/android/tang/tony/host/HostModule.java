package io.github.android.tang.tony.host;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;

@Module
class HostModule {

    @Provides
    @HostScope
    Context context(Application application) {
        return application;
    }

    @Provides
    @HostScope
    SharedPreferences sharedPreferences(Context context) {
        return context.getSharedPreferences("host.xml", Context.MODE_PRIVATE);
    }


}
