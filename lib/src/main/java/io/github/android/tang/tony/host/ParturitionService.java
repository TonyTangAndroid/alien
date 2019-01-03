package io.github.android.tang.tony.host;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.JobIntentService;
import hugo.weaving.DebugLog;

@DebugLog
public class ParturitionService extends JobIntentService {

    private static final int BIRTH_ID_AS_JOB_ID = 10083;

    public static void kickOff(Context context, Intent work) {
        enqueueWork(context, ParturitionService.class, birthId(), work);
    }

    private static int birthId() {
        return BIRTH_ID_AS_JOB_ID;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onHandleWork(@NonNull Intent work) {
        deliver(work);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void deliver(@NonNull Intent work) {
        startForegroundService(work);
    }

}