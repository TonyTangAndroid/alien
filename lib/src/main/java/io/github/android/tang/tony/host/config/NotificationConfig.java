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

        public abstract ActionConfig destruct();

        @Nullable
        public abstract ActionConfig deactivate();

        @Nullable
        public abstract ActionConfig activate();


        @AutoValue.Builder
        public abstract static class Builder {
            public abstract Builder title(String title);

            public abstract Builder body(String body);

            public abstract Builder smallIcon(@DrawableRes int smallIcon);

            public abstract Builder destruct(ActionConfig destruct);

            public abstract Builder deactivate(ActionConfig deactivate);

            public abstract Builder activate(ActionConfig activate);

            public abstract UI build();


        }
    }

    @AutoValue
    public abstract static class Channel {

        public static Builder builder() {
            return new AutoValue_NotificationConfig_Channel.Builder();
        }

        public abstract String id();

        public abstract String name();

        public abstract int importance();

        @AutoValue.Builder
        public abstract static class Builder {
            public abstract Builder id(String id);

            public abstract Builder name(String name);

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

        public abstract String title();

        @Nullable
        public abstract String body();

        @AutoValue.Builder
        public abstract static class Builder {
            public abstract Builder drawableId(int drawableId);

            public abstract Builder title(String title);

            public abstract Builder body(String body);

            public abstract ActionConfig build();
        }
    }
}