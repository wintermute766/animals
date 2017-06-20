package ru.sberbank.mobile.common.animal.db;

import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestName;
import org.junit.rules.TestRule;

import java.util.List;

import ru.sberbank.mobile.common.animal.Animal;
import ru.sberbank.mobile.common.animal.EntitiesGenerator;

import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.isIn;
import static org.junit.Assert.*;

/**
 * Created by Тичер on 15.06.2017.
 */
public class SQLiteAnimalsDaoTest {

    private static final String TAG = "DaoTest";

    private AnimalsDaoRule mDaoRule = new AnimalsDaoRule();
    private TestName mTestNameRule = new TestName();

    @Rule
    public TestRule rule = RuleChain
            .outerRule(mDaoRule)
            .around(mTestNameRule);

    @Before
    public void before() {
        Log.e(TAG, "before");
    }

    @Test
    public void testGetAnimals() {
        Log.e(TAG, "method name = " + mTestNameRule.getMethodName());
        List<Animal> expected = EntitiesGenerator.createRandomAnimalsList(false);
        for (Animal animal : expected) {
            animal.setId(mDaoRule.getSqliteAnimalsDao().insertAnimal(animal));
        }

        List<Animal> actual = mDaoRule.getSqliteAnimalsDao().getAnimals();
        assertThat(actual, everyItem(isIn(expected)));
    }

    @Test
    public void testGetAnimalById() {
        Log.e(TAG, "method name = " + mTestNameRule.getMethodName());
        Animal expected = EntitiesGenerator.createRandomAnimal(true);
        long id = mDaoRule.getSqliteAnimalsDao().insertAnimal(expected);
        expected.setId(id);
        Animal actual = mDaoRule.getSqliteAnimalsDao().getAnimalById(id);

        assertThat(actual, is(expected));
    }

    @Test
    public void testInsertAnimal() {
        Log.e(TAG, "method name = " + mTestNameRule.getMethodName());
        Animal animal = EntitiesGenerator.createRandomAnimal(false);
        long id = mDaoRule.getSqliteAnimalsDao().insertAnimal(animal);

        assertThat(true, is(id > 0));
    }

    @Test
    public void testUpdateAnimal() {
        Log.e(TAG, "method name = " + mTestNameRule.getMethodName());
        Animal animal1 = EntitiesGenerator.createRandomAnimal(true);
        Animal animal2 = EntitiesGenerator.createRandomAnimal(true);
        long id = mDaoRule.getSqliteAnimalsDao().insertAnimal(animal1);
        animal2.setId(id);
        int updatedRows = mDaoRule.getSqliteAnimalsDao().updateAnimal(animal2);

        assertThat(updatedRows, is(1));
    }

    @Test
    public void testDeleteAnimal() {
        Log.e(TAG, "method name = " + mTestNameRule.getMethodName());
        Animal animal = EntitiesGenerator.createRandomAnimal(true);
        long id = mDaoRule.getSqliteAnimalsDao().insertAnimal(animal);

        assertThat(mDaoRule.getSqliteAnimalsDao().deleteAnimal(animal), is(1));
    }

    @After
    public void after() {
        Log.e(TAG, "after");
    }
}
