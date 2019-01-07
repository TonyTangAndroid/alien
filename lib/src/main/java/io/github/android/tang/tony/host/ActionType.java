package io.github.android.tang.tony.host;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

@IntDef({Action.DESTRUCT, Action.DEACTIVATE, Action.ACTIVATE})
@Retention(RetentionPolicy.SOURCE)
public @interface ActionType {
}
