package io.github.android.tang.tony.host.config;

import android.app.Application;

import com.google.auto.value.AutoValue;

import io.github.android.tang.tony.host.Status;

@AutoValue
public abstract class Config {

    public static Builder builder() {
        return new AutoValue_Config.Builder().disableRevive(false);
    }

    public abstract boolean disableRevive();

    public abstract Application application();

    public abstract NotificationConfig notificationConfig();

    @AutoValue.Builder
    public abstract static class Builder {

        /**
         * @param disableRevive true if you want to disable the host service automatically startup
         *                      on device reboot.
         *                      <p>
         *                      By default, it is false, which means that as
         *                      long as the host has been activated, it will always run as a
         *                      foreground service. If you reboot the device, the host service will
         *                      automatically be started when the device finishes the reboot process.
         *                      <p>
         *                      If you set this value to true, the host service will not revive
         *                      until it is activated manually.
         *                      <p>
         *                      No matter what's the config of {@link #disableRevive(boolean)}
         *                      If you have manually put the host into {@link Status#DEACTIVATED},
         *                      it will be under such state until you manually wake it up.
         * @return the builder.
         */
        @SuppressWarnings("JavaDoc")
        public abstract Builder disableRevive(boolean disableRevive);

        public abstract Builder application(Application application);

        public abstract Builder notificationConfig(NotificationConfig notificationConfig);

        public abstract Config build();


    }
}