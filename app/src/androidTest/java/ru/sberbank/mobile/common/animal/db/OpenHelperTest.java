package ru.sberbank.mobile.common.animal.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

import android.support.test.InstrumentationRegistry;
import ru.sberbank.mobile.common.animal.db.AnimalsContract;
import ru.sberbank.mobile.common.animal.db.SQLiteAnimalsDao;
import ru.sberbank.mobile.common.animal.Animal;

/**
 * Created by Eugene on 20.06.2017.
 */

public class OpenHelperTest {

    private static final String DATABASE_NAME = "test.db";
    private static final int VERSION = 1;
    private final Context mContext = InstrumentationRegistry.getTargetContext();

    @Test
    public void testCreateTable() throws Exception {
        SQLiteAnimalsDao mDao = new SQLiteAnimalsDao(mContext, DATABASE_NAME, VERSION);
        SQLiteDatabase db = mDao.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + SQLiteAnimalsDao.TABLE_NAME, null);
        AnimalsContract.Animals animal = new AnimalsContract.Animals();
        List<String> expected = new ArrayList<>();
        expected.add(BaseColumns._ID);
        for (Field field : animal.getClass().getDeclaredFields()) {
            expected.add((String) field.get(animal));
        }
        List<String> actual = Arrays.asList(cursor.getColumnNames());

        assertThat(actual, containsInAnyOrder(expected.toArray(new String[expected.size()])));
    }

}
