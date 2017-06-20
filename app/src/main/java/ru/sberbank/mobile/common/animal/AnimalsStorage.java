package ru.sberbank.mobile.common.animal;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ru.sberbank.mobile.common.animal.db.AnimalsDao;
import ru.sberbank.mobile.common.animal.db.SQLiteAnimalsDao;

/**
 * @author QuickNick
 */
public class AnimalsStorage {

    private static final String TAG = AnimalsStorage.class.getSimpleName();

    private final AnimalsDao mAnimalsDao;
    private final List<OnContentChangeListener> mOnContentChangeListeners;

    public AnimalsStorage(AnimalsDao animalsDao) {
        mAnimalsDao = animalsDao;
        mOnContentChangeListeners = new ArrayList<>();
    }

    public List<Animal> getAnimals() {
        return mAnimalsDao.getAnimals();
    }

    Animal getAnimalById(long id) {
        return mAnimalsDao.getAnimalById(id);
    }

    public void addAnimal(Animal animal) {
        mAnimalsDao.insertAnimal(animal);
        for (OnContentChangeListener listener : mOnContentChangeListeners) {
            listener.onAnimalAdded(this, animal);
        }
    }

    public void updateAnimal(Animal animal) {
        mAnimalsDao.updateAnimal(animal);
    }

    public void deleteAnimal(Animal animal) {
        mAnimalsDao.deleteAnimal(animal);
    }

    public List<OnContentChangeListener> getContentChangeListeners() {
        return mOnContentChangeListeners;
    }

    public void addOnContentChangeListener(OnContentChangeListener listener) {
        mOnContentChangeListeners.add(listener);
    }

    public void removeOnContentChangeListener(OnContentChangeListener listener) {
        mOnContentChangeListeners.remove(listener);
    }

    public interface OnContentChangeListener {
        void onAnimalAdded(AnimalsStorage sender, Animal animal);
    }
}
