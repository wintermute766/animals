package ru.sberbank.mobile.common.animal.db;

import android.provider.BaseColumns;

/**
 * Created by Тичер on 08.06.2017.
 */
public class AnimalsContract {

    public static class Animals implements BaseColumns {

        public static final String SPECIES = "species";
        public static final String AGE = "age";
        public static final String NAME = "name";

    }
}
