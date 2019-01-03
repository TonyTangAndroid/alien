#A full example to demonstrate foreground service, broadcast receiver and notification channel.


###Command to test reboot broadcast receiver for emulator
####It is only for emulator without Google Play prebuilt.

step 1 : adb root

step 2 : adb shell am broadcast -a android.intent.action.BOOT_COMPLETED -p edu.cs4730.foregroundservicedemo

That's it.

(Optional) If you want to be more specific:

adb shell am broadcast -a android.intent.action.BOOT_COMPLETED -n edu.cs4730.foregroundservicedemo/edu.cs4730.foregroundservicedemo.DeviceRebootedBroadcastReceiver