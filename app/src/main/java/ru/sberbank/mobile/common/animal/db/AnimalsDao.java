package ru.sberbank.mobile.common.animal.db;

import java.util.List;

import ru.sberbank.mobile.common.animal.Animal;

/**
 * Created by Тичер on 08.06.2017.
 */
public interface AnimalsDao {

    long insertAnimal(Animal animal);

    List<Animal> getAnimals();

    Animal getAnimalById(long id);

    int updateAnimal(Animal animal);

    int deleteAnimal(Animal animal);
}
