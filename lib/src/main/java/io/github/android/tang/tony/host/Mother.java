package io.github.android.tang.tony.host;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import javax.inject.Inject;

@HostScope
class Mother {

    private final Context context;

    @Inject
    public Mother(Context context) {
        this.context = context;
    }

    private void deliverThroughCreator() {
        Creator.kickOff(context, conceive());
    }

    private Intent conceive() {
        return HostService.constructHostIntent(context);
    }

    void deliver() {
        if (selfReviveSupported()) {
            deliverDirectly();
        } else {
            deliverThroughCreator();
        }
    }

    private void deliverDirectly() {
        context.startService(conceive());
    }

    private boolean selfReviveSupported() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.O;
    }


}
