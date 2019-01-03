package io.github.android.tang.tony.host.config;

import android.app.PendingIntent;

import com.google.auto.value.AutoValue;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

@AutoValue
public abstract class UIConfig {


    public abstract String title();

    public abstract String body();

    @DrawableRes
    public abstract int smallIcon();

    public abstract boolean autoCancel();

    @Nullable
    public abstract PendingIntent defaultContentIntent();

    public static Builder builder() {
        return new AutoValue_UIConfig.Builder();
    }


    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder title(String title);

        public abstract Builder body(String body);

        public abstract Builder smallIcon(int smallIcon);

        public abstract Builder autoCancel(boolean autoCancel);

        public abstract Builder defaultContentIntent(PendingIntent defaultContentIntent);

        public abstract UIConfig build();
    }
}