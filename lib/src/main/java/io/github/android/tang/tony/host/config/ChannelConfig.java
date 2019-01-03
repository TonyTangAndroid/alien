package io.github.android.tang.tony.host.config;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ChannelConfig {

    public abstract String channelId();

    public abstract String channelName();

    public abstract int importance();

    public static Builder builder() {
        return new AutoValue_ChannelConfig.Builder();
    }


    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder channelId(String channelId);

        public abstract Builder channelName(String channelName);

        public abstract Builder importance(int importance);

        public abstract ChannelConfig build();
    }
}