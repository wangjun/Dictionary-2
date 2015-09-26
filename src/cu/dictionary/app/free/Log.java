package cu.dictionary.app.free;

public class Log {
	private final static boolean OUT_LOG = false;
//	private final static boolean OUT_LOG = true;
	private final static Loger loger;
	static{
		if(OUT_LOG){
			loger = new Loger(){
				@Override
				public void i(String tag, String message) {
					android.util.Log.i(tag, message);
				}
				@Override
				public void e(String tag, String message) {
					android.util.Log.e(tag, message);
				}
				@Override
				public void w(String tag, String message) {
					android.util.Log.w(tag, message);
				}
			};
		} else {
			loger = new Loger(){
				@Override
				public void i(String tag, String message) {}
				@Override
				public void e(String tag, String message) {}
				@Override
				public void w(String tag, String message) {}
			};
		}
	}
	/*
	 * 情報ログの出力
	 * 2012.10.7	実装
	 */
	public static void i(String tag, String message){
		loger.i(tag, message);
	}

	/*
	 * エラーログの出力
	 * 2012.10.7	実装
	 */
	public static void e(String tag, String message){
		loger.e(tag, message);
	}

	/*
	 * 警告ログの出力
	 * 2012.10.7	実装
	 */
	public static void w(String tag, String message){
		loger.w(tag, message);
	}


	private interface Loger{
		public void i(String tag, String message);
		public void e(String tag, String message);
		public void w(String tag, String message);
	}
}
