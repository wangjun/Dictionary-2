package cu.dictionary.preferenceactivity;

import cu.dictionary.app.free.R;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Producer extends DialogPreference{

	//	レイアウトに使うコンポーネントオブジェクト
	public Producer(Context context, AttributeSet attrs) {
		super(context, attrs);
		//	制作者レイアウト
		this.setDialogLayoutResource(R.layout.producer);
	}

	//	ボタンが押された時の処理
	//	ActionScriptと似たような無名関数で生成する
	private final OnClickListener btnAction = new OnClickListener(){
		@Override
		public void onClick(View arg0) {
			Resources res = arg0.getResources();
			Uri uri =Uri.parse(res.getString(R.string.producermailaddress));

			Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
			//	ビューからコンテキストをとらないと呼び出せない
			arg0.getContext().startActivity(intent);
			}
	};
	//	レイヤアウトに設定したオブジェクトの操作権限を取得する
	@Override
	protected void onBindDialogView(View view) {
		super.onBindDialogView(view);
		Button btn;
		btn = (Button)view.findViewById(R.id.ProducerButton_ID);
		btn.setOnClickListener(btnAction);
	}
}
