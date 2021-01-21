package io.github.android.tang.tony.host;

import androidx.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({Action.DESTRUCT, Action.DEACTIVATE, Action.ACTIVATE})
@Retention(RetentionPolicy.SOURCE)
@interface ActionType {}
