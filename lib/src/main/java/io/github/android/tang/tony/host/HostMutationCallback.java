package io.github.android.tang.tony.host;

import androidx.annotation.MainThread;

public interface HostMutationCallback {
    @MainThread
    void onMutate(@HostStatus int newStatus);
}
