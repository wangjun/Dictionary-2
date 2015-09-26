package cu.dictionary.app.free;

import java.util.ArrayList;
import java.util.List;

import mediba.ad.sdk.android.openx.MasAdView;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class MainActivity extends Activity implements OnSharedPreferenceChangeListener{
	private ListView listView;
	private TextView textView;
	private TextView textWord;
	private ViewFlipper viewFlipper;
	private AnimationHelp anm;
	private SharedPreferences			pref;
	private SharedPreferences.Editor	editor;
	private DataBase database;
	private Context context;
	private Cursor cursor;
	private ProgressDialog proDialog;
	private int states;

	private final static int IN_TEXTVIEW = 0;
	private final static int IN_LISTVIEW = 1;
	private boolean notification_flag;

	private float downPosX;
	private float downPosY;

	private MasAdView adview;

	private boolean atFirst = false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		//	設定の取得準備
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		editor = pref.edit();
		pref.registerOnSharedPreferenceChangeListener(this);

		//-------------------------------------------------------------------------------------
		//	ウィンドウのドラッグ処理
		LinearLayout layout;
		layout = (LinearLayout)findViewById(R.id.main_layout);
		layout.setOnTouchListener(new ViewMover());


		context = getApplicationContext();
		anm = new AnimationHelp();

		//	辞書バージョン更新の確認
		updateDictionary();

		//	データベースをオープン
		database = new DataBase(this);

		//通知領域に登録する
		setNotification();

		//-------------------------------------------------------------------------------------

		//	テキストエディットの構築
		EditText edit;
		edit = (EditText)findViewById(R.id.editText1);
		edit.setInputType(InputType.TYPE_CLASS_TEXT);
		edit.selectAll();
		edit.addTextChangedListener(new EditTextChangedListener());

		//	意味表示フィールドの設定
		textView = (TextView)findViewById(R.id.textView1);
		textWord = (TextView)findViewById(R.id.text_word);
		textView.setScroller(new Scroller(this));

		ScrollView scroll = (ScrollView)findViewById(R.id.scrollView1);
		scroll.setClickable(true);
		scroll.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				int action = arg1.getAction();
				switch(action){
				case MotionEvent.ACTION_DOWN:
					downPosX = arg1.getX();
					downPosY = arg1.getY();
					break;

				case MotionEvent.ACTION_UP:
					float disX = downPosX - arg1.getX();
					float disY = downPosY - arg1.getY();
					if(Math.abs(disX) > Math.abs(disY) && disX < 0 && viewFlipper.getInAnimation().hasEnded()){
						if(states != IN_LISTVIEW)
						{
							viewFlipper.setInAnimation(anm.inFromLeftAnimation());
							viewFlipper.setOutAnimation(anm.outToRightAnimation());
							viewFlipper.showPrevious();
							states = IN_LISTVIEW;
						}
					}
					break;
				}
				return false;
			}
		});


		viewFlipper = (ViewFlipper)findViewById(R.id.viewFlipper1);
		viewFlipper.setAutoStart(false);

		listView = (ListView)findViewById(R.id.listView1);
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int listViewNumber, long arg3) {
				String data = (String)arg0.getItemAtPosition(listViewNumber);
				textView.setText(data);
				//	選択されたワードの意味を取得
				final String sql = "select mean, word from items where word=\"" + data + "\"";
				SQLiteDatabase db = database.getReadableDatabase();
				Cursor cursor = db.rawQuery(sql, null);
				cursor.moveToFirst();

				String mean = cursor.getString(0);
				mean = mean.replace("/", "\n\n");
				textView.setText(mean);
				textWord.setText(cursor.getString(1));
				Animation anim = viewFlipper.getInAnimation();
				if(anim == null || anim.hasEnded()){
					viewFlipper.setInAnimation(anm.inFromRightAnimation());
					viewFlipper.setOutAnimation(anm.outToLeftAnimation());
					viewFlipper.showNext();
				}
				states = IN_TEXTVIEW;
			}
		});

		states = IN_LISTVIEW;

		proDialog = new ProgressDialog(this);
		proDialog.setMessage(getString(R.string.search_message));
		proDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		proDialog.setCancelable(true);

		ImageView d_button;
		d_button = (ImageView)findViewById(R.id.d_button);
		d_button.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this, PrefActivity.class);
				startActivity(intent);
			}
		});
		//	広告の設定 IDが通るまではコメントアウト
		adview = (MasAdView)findViewById(R.id.adview);
		adview.setAuid("302602");
		adview.start();
    }

	/*
	 * 通知領域に登録する
	 */
	private void setNotification(){
		boolean check = pref.getBoolean(getString(R.string.notification_key), true);
		if(check){
			//	ステータスバーに登録する
			NotificationManager nManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
			Notification notify = new Notification();
			notify.icon = R.drawable.icon;
			notify.tickerText = getString(R.string.app_name);
			notify.flags = Notification.FLAG_ONGOING_EVENT;

			Intent intent = new Intent(this, MainActivity.class);
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
			notify.setLatestEventInfo(context, getString(R.string.app_name), getString(R.string.notify_message), contentIntent);
			nManager.notify(R.string.app_name, notify);
			//	通知領域に登録済み
			notification_flag = true;
    	}
    }

	/*
	 * PreferenceActivityの項目変更
	 */
	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		NotificationManager nManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		if(arg1.equals(getString(R.string.notification_key))){
			if(pref.getBoolean(arg1, true) == false){
				nManager.cancel(R.string.app_name);
				nManager = null;
				notification_flag = false;
			} else {
				if(notification_flag == false){
					setNotification();
				}
			}
		}
		if(arg1.equals(getString(R.string.setfontsize_key))){
			int i = pref.getInt(getString(R.string.setfontsize_key), 1);
			int[] size = context.getResources().getIntArray(R.array.font_size);
			textView.setTextSize(size[i]);
			textWord.setTextSize(size[i]);
			Log.i("", "フォントサイズが変更された");
		}
	}

	 /*
	  * 辞書を更新する
	  * 2012.9.27	実装
	  */
	private void updateDictionary(){
		final int installedVersion = pref.getInt(Values.DIC_VERSION_KEY, -1);
		final int havingVersion = Integer.valueOf(getString(R.string.dictionary_version));
		String path = this.getDatabasePath(" ").getPath();
		path = path.substring(0, path.length() - 1);
		Log.i("", "[" + path + "]");
		if(installedVersion < havingVersion){
			if(AssetsManager.installAssetFile(this, getString(R.string.dictionary_file) + ".zip", path)){
				editor.putInt(Values.DIC_VERSION_KEY, havingVersion);
				editor.commit();
				//	成功通知
				Toast.makeText(this, R.string.dictionary_update_message, Toast.LENGTH_SHORT)
				.show();
			} else {
				Log.i("", "インストール失敗");
			}
		}
    }
	/*
	 * 前回記録した場所に画面を移動させる。
	 * 2012.10.15	実装
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		//	Viewを真ん中に持っていく
		if(atFirst){
			View layout = findViewById(R.id.main_layout);
			final int x = pref.getInt(getString(R.string.window_pos_x), 0);
			final int y = pref.getInt(getString(R.string.window_pos_y), 0);

			ViewMover.moveTo(layout, x, y);
			layout.setVisibility(View.VISIBLE);
			super.onWindowFocusChanged(hasFocus);
			atFirst = false;
		}

	}
	/*
	 * 画面の消失
	 * 2012.10.15	実装
	 */
	@Override
	protected void onPause() {
		super.onPause();

		View layout = findViewById(R.id.main_layout);
		final int x = layout.getLeft();
		final int y = layout.getTop();
		editor.putInt(getString(R.string.window_pos_x), x);
		editor.putInt(getString(R.string.window_pos_y), y);
		editor.commit();
	}
	class EditTextChangedListener implements TextWatcher
	{
		@Override
		public void afterTextChanged(Editable arg0) {

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

		}

		@Override
		public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3)
		{
			proDialog.show();
			if(!String.valueOf(s).equals("")){
				//	リストビューを作るための配列
				ArrayAdapter<String> adapter = null;
				String[] list;
				//	試しにデータを取り出したりしてみる
				final String sql = "select word from items where word like \"" + s + "%\" order by level desc, item_id, word, mean;";
				SQLiteDatabase data = database.getReadableDatabase();
				cursor = data.rawQuery(sql, null);
				cursor.moveToFirst();
				int count = cursor.getCount();
				//	上限値
				final int MAX_COUNT = context.getResources().getInteger(R.integer.max_list_count);
				if(count > MAX_COUNT){
					count = MAX_COUNT;
				}
				list = new String[count];
				if(cursor.getCount() > 0){
					for(int i = 0; i < count ; ++i){
						list[i] = cursor.getString(0);
						cursor.moveToNext();
					}
				}
				adapter = new ListAdapter(context, list);
				listView.setAdapter(adapter);

			}
			else if(String.valueOf(s).equals("")){
				//	リストビューを作るための配列
				ArrayAdapter<String> adapter = null;
				List<String> list = new ArrayList<String>();
				adapter = new ArrayAdapter<String>(context, android.R.layout.simple_expandable_list_item_1, list);
				listView.setAdapter(adapter);
			}
			if(states == IN_TEXTVIEW){
				viewFlipper.setInAnimation(anm.inFromLeftAnimation());
				viewFlipper.setOutAnimation(anm.outToRightAnimation());
				viewFlipper.showPrevious();
				states = IN_LISTVIEW;
			}

			proDialog.cancel();
		}

	}
}
