package io.github.android.tang.tony.host.config;

import com.google.auto.value.AutoValue;

import androidx.annotation.DrawableRes;

@AutoValue
public abstract class ActionConfig {

    @DrawableRes
    public abstract int drawableId();

    public abstract String actionTitle();

    public static Builder builder() {
        return new AutoValue_ActionConfig.Builder();
    }


    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder drawableId(int drawableId);

        public abstract Builder actionTitle(String actionTitle);

        public abstract ActionConfig build();
    }
}