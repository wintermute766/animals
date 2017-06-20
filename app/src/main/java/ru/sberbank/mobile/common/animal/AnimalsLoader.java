package ru.sberbank.mobile.common.animal;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

/**
 * @author QuickNick.
 */

public class AnimalsLoader extends AsyncTaskLoader<List<Animal>> implements AnimalsStorage.OnContentChangeListener {

    private final AnimalsStorage mAnimalsStorage;
    private List<Animal> mCachedResult;

    public AnimalsLoader(Context context, AnimalsStorage animalsStorage) {
        super(context);
        mAnimalsStorage = animalsStorage;
        mAnimalsStorage.addOnContentChangeListener(this);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if ((mCachedResult == null) || takeContentChanged()) {
            forceLoad();
        } else {
            deliverResult(mCachedResult);
        }
    }

    @Override
    public void deliverResult(List<Animal> data) {
        super.deliverResult(data);
        mCachedResult = data;
    }

    @Override
    public List<Animal> loadInBackground() {
        return mAnimalsStorage.getAnimals();
    }

    @Override
    protected void onReset() {
        super.onReset();
        mAnimalsStorage.removeOnContentChangeListener(this);
    }

    // AnimalsStorage.OnContentChangeListener >>>

    @Override
    public void onAnimalAdded(AnimalsStorage sender, Animal animal) {
        onContentChanged();
    }

    // <<< AnimalsStorage.OnContentChangeListener
}
