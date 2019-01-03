package io.github.android.tang.tony.host;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.JobIntentService;
import hugo.weaving.DebugLog;

@DebugLog
public class DeviceRebootedJobSchedulerService extends JobIntentService {

    private static final int JOB_ID_TO_START_FOREGROUND_SERVICE = 10083;

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, DeviceRebootedJobSchedulerService.class, hashcodeClassNameAsJobId(), work);
    }

    private static int hashcodeClassNameAsJobId() {
        return JOB_ID_TO_START_FOREGROUND_SERVICE;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onHandleWork(@NonNull Intent work) {
        startForegroundService(work);
    }

}