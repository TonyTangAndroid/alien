package io.github.android.tang.tony.host;

import androidx.annotation.MainThread;

public interface HostStatusCallback {
  @MainThread
  void onMutate(@HostStatus int newStatus);
}
