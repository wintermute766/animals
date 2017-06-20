package ru.sberbank.mobile.common.animal.db;

import java.util.ArrayList;
import java.util.List;

import ru.sberbank.mobile.common.animal.Animal;

/**
 * Created by Тичер on 17.06.2017.
 */
public class StubAnimalsDao implements AnimalsDao {

    public List<Animal> animals;
    public boolean getAnimalsCalled;
    private long mLastAddedId;

    public StubAnimalsDao() {
        animals = new ArrayList<>();
    }

    @Override
    public long insertAnimal(Animal animal) {
        mLastAddedId++;
        animal.setId(mLastAddedId);
        animals.add(animal);
        return mLastAddedId;
    }

    @Override
    public List<Animal> getAnimals() {
        getAnimalsCalled = true;
        return new ArrayList<>(animals);
    }

    @Override
    public Animal getAnimalById(long id) {
        return null;
    }

    @Override
    public int updateAnimal(Animal animal) {
        return 0;
    }

    @Override
    public int deleteAnimal(Animal animal) {
        return 0;
    }

    public void insertAnimalsForTest(List<Animal> animals) {
        for (Animal animal : animals) {
            mLastAddedId++;
            animal.setId(mLastAddedId);
        }
        this.animals.addAll(animals);
    }
}
