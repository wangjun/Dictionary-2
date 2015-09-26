package cu.dictionary.app.free;

import cu.dictionary.app.free.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class StartUp extends BroadcastReceiver{
	private SharedPreferences pref;
	private SharedPreferences.Editor editor;
	private NotificationManager nManager;
	private Notification n;
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("StartUp", "onReceiveが呼ばれているか");
		pref = PreferenceManager.getDefaultSharedPreferences(context);
		editor = pref.edit();
		if(pref.getBoolean(context.getString(R.string.broadcastreceiver_key), true))
		{
	        String action = intent.getAction();
	        if (action.equals(Intent.ACTION_BOOT_COMPLETED))
	        {
	        	//	ステータスバーに登録する
	            nManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

	            n = new Notification();
	            n.icon = R.drawable.icon;
	            n.tickerText = context.getString(R.string.app_name);
	            n.flags = Notification.FLAG_ONGOING_EVENT;

	            Intent i = new Intent(context, MainActivity.class);
	            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, i, 0);
	            n.setLatestEventInfo(context, context.getString(R.string.app_name), context.getString(R.string.notify_message), contentIntent);
	            nManager.notify(R.string.app_name, n);
	            //	通知領域に登録済み
	            editor.putBoolean(context.getString(R.string.broadcastreceiver_key), true);
	            editor.putBoolean(context.getString(R.string.notification_key), true);
	            editor.commit();
	        }
		}
	}
}
