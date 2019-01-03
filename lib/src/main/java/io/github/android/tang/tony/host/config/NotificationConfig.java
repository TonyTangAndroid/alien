package io.github.android.tang.tony.host.config;

import com.google.auto.value.AutoValue;

import androidx.annotation.Nullable;

@AutoValue
public abstract class NotificationConfig {

    @Nullable
    public abstract ChannelConfig notificationChannelConfig();

    @Nullable
    public abstract ActionConfig stop();

    @Nullable
    public abstract ActionConfig pause();

    @Nullable
    public abstract ActionConfig resume();

    @Nullable
    public abstract UIConfig UIConfig();


}