package ru.sberbank.mobile.common.animal;

import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by Тичер on 15.06.2017.
 */
public class AnimalParcelabilityTest {

    @Test
    public void testParcelability() {
        Parcel parcel = Parcel.obtain();
        try {
            Animal expected = EntitiesGenerator.createRandomAnimal(true);
            parcel.writeParcelable(expected, 0);
            parcel.setDataPosition(0);

            Animal actual = parcel.readParcelable(getClass().getClassLoader());
            assertThat(actual, is(expected));
        } finally {
            parcel.recycle();
        }
    }
}
