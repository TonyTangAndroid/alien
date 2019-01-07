package io.github.android.tang.tony.host;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

@IntDef({Status.NONE, Status.SLEEP, Status.ALIVE})
@Retention(RetentionPolicy.SOURCE)
public @interface HostStatus {
}
