package io.github.android.tang.tony.host;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

@IntDef({Status.NONE, Status.DEACTIVATED, Status.ACTIVATED})
@Retention(RetentionPolicy.SOURCE)
public @interface HostStatus {
}
