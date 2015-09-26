package cu.dictionary.app.free;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {

	private final static String EXTENSION = ".sqlite3";

	public DataBase(Context context){
		super(context, context.getString(R.string.dictionary_file) + EXTENSION, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//db.
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

}
