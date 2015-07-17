package cn.qingguow.qingguoapp.test;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.test.AndroidTestCase;

public class AccessContentProviderTest extends AndroidTestCase {
	public void testContentProviderInsert(){
		Uri uri=Uri.parse("content://cn.qinguow.providers.person/person");
		ContentResolver contentResolver=this.getContext().getContentResolver();
		ContentValues values=new ContentValues();
		values.put("name", "≤‚ ‘Ã·π©’ﬂ");
		contentResolver.insert(uri, values);
	}
}
