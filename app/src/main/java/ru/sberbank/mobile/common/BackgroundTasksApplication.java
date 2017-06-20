package ru.sberbank.mobile.common;

import android.app.Application;

import ru.sberbank.mobile.common.animal.AnimalsStorage;
import ru.sberbank.mobile.common.animal.AnimalsStorageProvider;
import ru.sberbank.mobile.common.animal.db.AnimalsDao;
import ru.sberbank.mobile.common.animal.db.SQLiteAnimalsDao;

/**
 * @author QuickNick.
 */

public class BackgroundTasksApplication extends Application implements AnimalsStorageProvider {

    private AnimalsStorage mAnimalsStorage;

    @Override
    public void onCreate() {
        super.onCreate();
        AnimalsDao animalsDao = new SQLiteAnimalsDao(this);
        mAnimalsStorage = new AnimalsStorage(animalsDao);
    }

    @Override
    public AnimalsStorage getAnimalsStorage() {
        return mAnimalsStorage;
    }
}
