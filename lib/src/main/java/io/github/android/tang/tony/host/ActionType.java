package io.github.android.tang.tony.host;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

@IntDef({Action.STOP, Action.PAUSE, Action.RESUME})
@Retention(RetentionPolicy.SOURCE)
public @interface ActionType {
}
