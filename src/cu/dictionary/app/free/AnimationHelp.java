package cu.dictionary.app.free;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;


public class AnimationHelp extends Animation{
	private static final int TIME = 300;

	public Animation inFromRightAnimation()
	{
	    Animation anim = new TranslateAnimation(
	    		Animation.RELATIVE_TO_PARENT, 1.0f,
	    		Animation.RELATIVE_TO_PARENT, 0.0f,
	    		Animation.RELATIVE_TO_PARENT, 0.0f,
	    		Animation.RELATIVE_TO_PARENT, 0.0f);
	    anim.setDuration(TIME);
	    anim.setInterpolator(new AccelerateInterpolator());

	    return anim;
	}

	public Animation inFromLeftAnimation()
	{
		Animation anim = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		anim.setDuration(TIME);
		anim.setInterpolator(new AccelerateInterpolator());

		return anim;
	}

	public Animation outToLeftAnimation()
	{
	    Animation anim = new TranslateAnimation(
	    		Animation.RELATIVE_TO_PARENT, 0.0f,
	    		Animation.RELATIVE_TO_PARENT, -1.0f,
	    		Animation.RELATIVE_TO_PARENT, 0.0f,
	    		Animation.RELATIVE_TO_PARENT, 0.0f);
	    anim.setDuration(TIME);
	    anim.setInterpolator(new AccelerateInterpolator());

	    return anim;
	}

	public Animation outToRightAnimation()
	{
		 Animation anim = new TranslateAnimation(
			      Animation.RELATIVE_TO_PARENT, 0.0f,
			      Animation.RELATIVE_TO_PARENT, 1.0f,
			      Animation.RELATIVE_TO_PARENT, 0.0f,
			      Animation.RELATIVE_TO_PARENT, 0.0f);
		 anim.setDuration(TIME);
		 anim.setInterpolator(new AccelerateInterpolator());
		 return anim;
	}
}
