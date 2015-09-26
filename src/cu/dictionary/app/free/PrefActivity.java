package cu.dictionary.app.free;

import cu.dictionary.app.free.R;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class PrefActivity extends PreferenceActivity{
	private SharedPreferences pref;
	private SharedPreferences.Editor editor;
	private CheckBoxPreference notification, broadcastReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.layout.preference);

		//pref = PreferenceManager.getDefaultSharedPreferences(this);
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		editor = pref.edit();
		//	通知領域登録変更
		notification = (CheckBoxPreference)findPreference(getString(R.string.notification_key));
		notification.setChecked(pref.getBoolean(getString(R.string.notification_key), true));
		notification.setOnPreferenceChangeListener(new OnPreferenceChangeListener(){

			@Override
			public boolean onPreferenceChange(Preference arg0, Object arg1) {
				boolean checked = ((Boolean)arg1).booleanValue();
				editor.putBoolean(getString(R.string.notification_key), checked);
				editor.commit();
				return true;
			}
		});

		//	スタートアップ
		broadcastReceiver = (CheckBoxPreference)findPreference(getString(R.string.broadcastreceiver_key));
		broadcastReceiver.setChecked(pref.getBoolean(getString(R.string.broadcastreceiver_key), true));
		broadcastReceiver.setOnPreferenceChangeListener(new OnPreferenceChangeListener(){

			@Override
			public boolean onPreferenceChange(Preference arg0, Object arg1) {
				boolean checked = ((Boolean)arg1).booleanValue();
				editor.putBoolean(getString(R.string.broadcastreceiver_key), checked);
				editor.commit();
				return true;
			}});
	}

}
