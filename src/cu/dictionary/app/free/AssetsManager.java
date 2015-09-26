package cu.dictionary.app.free;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;
import android.content.res.AssetManager;

/*
 * アセットのデータを扱うためのユーティリティ
 */
public class AssetsManager{

	private final static int MAX_BUFFER = 1024;

	/*
	 * assetsのzipファイルをdata領域にインストールする
	 * pathはアプリケーションデータ領域(data/data/)内に作成されるpathです。
	 * 2012.9.25	実装
	 */
	public static boolean installAssetFile(Context context, String asset, String path){
		AssetManager	am = context.getResources().getAssets();
		InputStream is;
		String installPath = null;
		try {
			is = am.open(asset, AssetManager.ACCESS_STREAMING);
			ZipInputStream	zis = new ZipInputStream(is);
			ZipEntry		ze = null;
			if(path != null){
				new File(path).mkdir();
			}
			while((ze = zis.getNextEntry()) != null){
				installPath = path + ze.getName();
				Log.i("", installPath);
				FileOutputStream fos = new FileOutputStream(installPath, false);

				byte[] buffer = new byte[MAX_BUFFER];
				int size = 0;
				//	データをコピーする
				while((size = zis.read(buffer)) >= 0){
					fos.write(buffer, 0, size);
				}
				fos.close();
				zis.closeEntry();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
}