package cu.dictionary.app.free;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;

public class ViewMover implements OnTouchListener {

	private float downX;
	private float downY;
	private float downLeftMargin;
	private float downTopMargin;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		final ViewGroup.MarginLayoutParams viewGroup = (ViewGroup.MarginLayoutParams)v.getLayoutParams();
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			downX = event.getRawX();
			downY = event.getRawY();
			downLeftMargin = viewGroup.leftMargin;
			downTopMargin = viewGroup.topMargin;
			return true;
		}
		if(event.getAction() ==MotionEvent.ACTION_MOVE)
		{
			viewGroup.leftMargin = (int)(downLeftMargin + (event.getRawX() - downX));
			viewGroup.topMargin = (int)(downTopMargin + (event.getRawY() - downY));

			//	移動された後の座標設定
			v.layout(viewGroup.leftMargin,
					viewGroup.topMargin,
					viewGroup.leftMargin + v.getWidth(),
					viewGroup.topMargin + v.getHeight());
			return true;
		}
		return false;
	}
	/*
	 * Viewを指定した位置に移動させる
	 * 2012.10.7	実装
	 */
	public static void moveTo(View view, int x, int y){
		final ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams)view.getLayoutParams();
		param.leftMargin = x;
		param.topMargin = y;
		view.layout(	param.leftMargin,
						param.topMargin,
						param.leftMargin + view.getWidth(),
						param.topMargin + view.getHeight());

	}
}
