package cu.dictionary.preferenceactivity;

import cu.dictionary.app.free.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class SetFontSize extends DialogPreference{

	private final String FONT_SETTING_KEY;

	private Context context;
	private TextView text;
	private SharedPreferences pref;
	private SharedPreferences.Editor editor;
	private int temp;

	public SetFontSize(Context _context, AttributeSet attrs) {
		super(_context, attrs);
		setDialogLayoutResource(R.layout.setfontsize);
		context = _context;

		pref = PreferenceManager.getDefaultSharedPreferences(context);
		editor = pref.edit();

		FONT_SETTING_KEY = context.getString(R.string.setfontsize_key);
	}

	@Override
	protected void onBindDialogView(View view) {
		super.onBindDialogView(view);

		text = (TextView)view.findViewById(R.id.setfongsize_textview);
		setTextViewTextSize(pref.getInt(FONT_SETTING_KEY, 1));


		int[] size = context.getResources().getIntArray(R.array.font_size);
		SeekBar seek;
		seek = (SeekBar)view.findViewById(R.id.setfontsize_seekbar);
		seek.setMax(size.length - 1);
		seek.setProgress(pref.getInt(FONT_SETTING_KEY, 1));
		seek.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				temp = arg0.getProgress();
				setTextViewTextSize(temp);
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {

			}});
	}

	private void setTextViewTextSize(int seekBar_values){
		int[] size = context.getResources().getIntArray(R.array.font_size);
		String[] name = context.getResources().getStringArray(R.array.font_name);
		text.setText(name[seekBar_values]);
		text.setTextSize(size[seekBar_values]);
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		if(positiveResult){
			editor.putInt(FONT_SETTING_KEY, temp);
			editor.commit();
		} else {
			setTextViewTextSize(pref.getInt(FONT_SETTING_KEY, 1));
		}
	}

}
