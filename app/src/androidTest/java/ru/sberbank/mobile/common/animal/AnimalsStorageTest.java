package ru.sberbank.mobile.common.animal;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

import java.util.List;

import ru.sberbank.mobile.common.animal.db.StubAnimalsDao;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import ru.sberbank.mobile.common.animal.Animal;
import ru.sberbank.mobile.common.animal.AnimalsStorage;
import ru.sberbank.mobile.common.animal.db.AnimalsDao;
import ru.sberbank.mobile.common.animal.db.SQLiteAnimalsDao;
import ru.sberbank.mobile.common.animal.EntitiesGenerator;

public class AnimalsStorageTest {

    private static List<Animal> sAnimals = EntitiesGenerator.createRandomAnimalsList(true);
    private static Animal sAnimal = EntitiesGenerator.createRandomAnimal(true);

    @Mock
    public AnimalsDao mAnimalsDao;
    @Mock
    public AnimalsStorage.OnContentChangeListener mChangeListener;

    private AnimalsStorage mAnimalsStorage;


    @Before
    public void setUp() {
        mAnimalsDao = mock(AnimalsDao.class);
        mChangeListener = mock(AnimalsStorage.OnContentChangeListener.class);
        mAnimalsStorage = new AnimalsStorage(mAnimalsDao);
    }

    @Test
    public void testGetAnimals() {
        doReturn(sAnimals).when(mAnimalsDao).getAnimals();
        List<Animal> actual = mAnimalsStorage.getAnimals();

        assertThat(actual, is(sAnimals));
        verify(mAnimalsDao).getAnimals();
    }

    @Test
    public void testGetAnimalById() throws Exception {
        doReturn(sAnimal).when(mAnimalsDao).getAnimalById(sAnimal.getId());
        Animal actual = mAnimalsStorage.getAnimalById(sAnimal.getId());

        assertThat(actual, is(sAnimal));
        verify(mAnimalsDao).getAnimalById(sAnimal.getId());
    }

    @Test
    public void testAddAndRemoveOnContentChangeListener() throws Exception {
        mAnimalsStorage.addOnContentChangeListener(mChangeListener);
        assertThat(mAnimalsStorage.getContentChangeListeners().size(), is(1));

        mAnimalsStorage.removeOnContentChangeListener(mChangeListener);
        assertThat(mAnimalsStorage.getContentChangeListeners().size(), is(0));
    }
}
