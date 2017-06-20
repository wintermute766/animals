package ru.sberbank.mobile.common.animal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Тичер on 15.06.2017.
 */
public class EntitiesGenerator {

    private static final int SIZE = 10;
    private static final int START_CHAR = (int) 'A';
    private static final int END_CHAR = (int) 'Z';
    private static final Random RANDOM = new Random();

    protected static String createRandomString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SIZE; i++) {
            int value = START_CHAR + RANDOM.nextInt(
                    END_CHAR - START_CHAR
            );
            sb.append((char) value);
        }
        return sb.toString();
    }

    public static Animal createRandomAnimal(boolean includeId) {
        Animal animal = new Animal();
        if (includeId) {
            animal.setId(Math.abs(RANDOM.nextLong()));
        }
        animal.setName(createRandomString());
        animal.setSpecies(createRandomString());
        animal.setAge(Math.abs(RANDOM.nextInt()));
        return animal;
    }

    public static List<Animal> createRandomAnimalsList(boolean includeId) {
        List<Animal> animals = new ArrayList<>();
        int size = 1 + RANDOM.nextInt(SIZE);
        for (int i = 0; i < size; i++) {
            animals.add(createRandomAnimal(includeId));
        }
        return animals;
    }
}
