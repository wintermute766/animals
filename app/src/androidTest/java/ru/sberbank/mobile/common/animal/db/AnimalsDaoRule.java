package ru.sberbank.mobile.common.animal.db;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import org.junit.rules.ExternalResource;

/**
 * Created by Тичер on 17.06.2017.
 */
public class AnimalsDaoRule extends ExternalResource {

    private static final String TAG = "AnimalsDaoRule";
    private static final String TEST_NAME = "test.db";

    private SQLiteAnimalsDao mSqliteAnimalsDao;

    @Override
    protected void before() throws Throwable {
        super.before();
        Context context = InstrumentationRegistry.getTargetContext();
        mSqliteAnimalsDao = new SQLiteAnimalsDao(
                context, TEST_NAME, SQLiteAnimalsDao.CURRENT_VERSION
        );
        Log.e(TAG, "before");
    }

    @Override
    protected void after() {
        Log.e(TAG, "after");
        super.after();
        mSqliteAnimalsDao = null;
        InstrumentationRegistry.getTargetContext().deleteDatabase(TEST_NAME);
    }

    public SQLiteAnimalsDao getSqliteAnimalsDao() {
        return mSqliteAnimalsDao;
    }
}
