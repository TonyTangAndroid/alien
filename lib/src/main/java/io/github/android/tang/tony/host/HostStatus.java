package io.github.android.tang.tony.host;

import androidx.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({Status.NONE, Status.DEACTIVATED, Status.ACTIVATED})
@Retention(RetentionPolicy.SOURCE)
public @interface HostStatus {}
