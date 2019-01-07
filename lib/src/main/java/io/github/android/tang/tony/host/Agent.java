package io.github.android.tang.tony.host;

import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

@HostScope
public class Agent {

    private final Mother mother;
    private final Context context;
    private final HostStatusPersister sharedPreferenceHelper;

    @Inject
    public Agent(Mother mother, Context context, HostStatusPersister sharedPreferenceHelper) {
        this.mother = mother;
        this.context = context;
        this.sharedPreferenceHelper = sharedPreferenceHelper;
    }


    public void activate() {
        sharedPreferenceHelper.update(HostStatusPersister.InternalStatus.ACTIVATED);
        mother.deliver();
    }

    public void sleep() {
        sharedPreferenceHelper.update(HostStatusPersister.InternalStatus.ON_CALL);
        context.stopService(instance());
    }

    public void destruct() {
        sharedPreferenceHelper.update(HostStatusPersister.InternalStatus.NONE);
        context.stopService(instance());

    }

    public HostStatusPersister.InternalStatus status() {
        return sharedPreferenceHelper.status();
    }

    private Intent instance() {
        return HostService.constructHostIntent(context);
    }


    public void revive() {
        mother.deliver();
    }
}
