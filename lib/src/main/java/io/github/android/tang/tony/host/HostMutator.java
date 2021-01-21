package io.github.android.tang.tony.host;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import hugo.weaving.DebugLog;
import javax.inject.Inject;

@DebugLog
public class HostMutator extends BroadcastReceiver {

  private static final String EXTRA_ACTION_TYPE = "extra_action_type";
  @Inject Agent agent;

  public static Intent constructIntent(String applicationId, @ActionType int action) {
    Intent intent = new Intent(BuildConfig.ACTION_MUTATE_HOST);
    intent.putExtra(EXTRA_ACTION_TYPE, action);
    intent.setPackage(applicationId); // Must be set to support Android Oreo.
    return intent;
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    Host.get().hostComponent().inject(this);
    onActionTriggered(intent.getIntExtra(EXTRA_ACTION_TYPE, Action.DESTRUCT));
  }

  private void onActionTriggered(@ActionType int action) {
    agent.mutate(action);
  }
}
