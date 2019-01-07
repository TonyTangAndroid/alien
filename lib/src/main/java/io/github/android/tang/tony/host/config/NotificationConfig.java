package io.github.android.tang.tony.host.config;

import android.content.Intent;

import com.google.auto.value.AutoValue;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

@AutoValue
public abstract class NotificationConfig {

    public static Builder builder() {
        return new AutoValue_NotificationConfig.Builder();
    }

    public abstract Channel channel();

    public abstract UI ui();

    public abstract Intent launchIntent();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder channel(Channel channel);

        public abstract Builder ui(UI ui);

        public abstract Builder launchIntent(Intent launchIntent);

        public abstract NotificationConfig build();
    }

    @AutoValue
    public abstract static class UI {

        public static Builder builder() {
            return new AutoValue_NotificationConfig_UI.Builder();
        }

        public abstract String title();

        public abstract String body();

        @DrawableRes
        public abstract int smallIcon();

        public abstract ActionConfig stop();

        @Nullable
        public abstract ActionConfig pause();

        @Nullable
        public abstract ActionConfig resume();


        @AutoValue.Builder
        public abstract static class Builder {
            public abstract Builder title(String title);

            public abstract Builder body(String body);

            public abstract Builder smallIcon(@DrawableRes int smallIcon);

            public abstract Builder stop(ActionConfig stop);

            public abstract Builder pause(ActionConfig pause);

            public abstract Builder resume(ActionConfig resume);

            public abstract UI build();


        }
    }

    @AutoValue
    public abstract static class Channel {

        public static Builder builder() {
            return new AutoValue_NotificationConfig_Channel.Builder();
        }

        public abstract String channelId();

        public abstract String channelName();

        public abstract int importance();

        @AutoValue.Builder
        public abstract static class Builder {
            public abstract Builder channelId(String channelId);

            public abstract Builder channelName(String channelName);

            public abstract Builder importance(int importance);

            public abstract Channel build();
        }
    }

    @AutoValue
    public abstract static class ActionConfig {

        public static Builder builder() {
            return new AutoValue_NotificationConfig_ActionConfig.Builder();
        }

        @DrawableRes
        public abstract int drawableId();

        public abstract String actionTitle();

        @Nullable
        public abstract String body();

        @AutoValue.Builder
        public abstract static class Builder {
            public abstract Builder drawableId(int drawableId);

            public abstract Builder actionTitle(String actionTitle);

            public abstract Builder body(String body);

            public abstract ActionConfig build();
        }
    }
}