package ru.sberbank.mobile.common.animal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ru.sberbank.backgroundtaskssample.R;

/**
 * @author QuickNick.
 */

public class AddAnimalActivity extends AppCompatActivity {

    private AnimalsStorage mAnimalsStorage;

    private TextInputLayout mSpeciesTextInput;
    private EditText mSpeciesEditText;
    private TextInputLayout mAgeTextInput;
    private EditText mAgeEditText;
    private TextInputLayout mNameTextInput;
    private EditText mNameEditText;
    private Button mAddButton;
    private EditText[] mEditTexts;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, AddAnimalActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AnimalsStorageProvider provider = (AnimalsStorageProvider) getApplication();
        mAnimalsStorage = provider.getAnimalsStorage();

        setContentView(R.layout.add_animal_activity);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        mSpeciesTextInput = (TextInputLayout) findViewById(R.id.species_text_input);
        mSpeciesEditText = (EditText) findViewById(R.id.species_edit_text);
        mAgeTextInput = (TextInputLayout) findViewById(R.id.age_text_input);
        mAgeEditText = (EditText) findViewById(R.id.age_edit_text);
        mNameTextInput = (TextInputLayout) findViewById(R.id.name_text_input);
        mNameEditText = (EditText) findViewById(R.id.name_edit_text);
        mAddButton = (Button) findViewById(R.id.add_animal_button);
        mEditTexts = new EditText[] {mSpeciesEditText, mAgeEditText, mNameEditText};
        for (EditText editText : mEditTexts) {
            editText.addTextChangedListener(new TextWatcherImpl());
        }

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAnimal();
            }
        });
    }

    private void createAnimal() {
        String species = mSpeciesEditText.getText().toString();
        int age = Integer.valueOf(mAgeEditText.getText().toString());
        String name = mNameEditText.getText().toString();
        Animal animal = new Animal(species, age, name);
        mAnimalsStorage.addAnimal(animal);
        finish();
    }

    private class TextWatcherImpl implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            boolean buttonEnabled = true;
            for (EditText editText : mEditTexts) {
                if (TextUtils.isEmpty(editText.getText())) {
                    buttonEnabled = false;
                    break;
                }
            }
            mAddButton.setEnabled(buttonEnabled);
        }
    }
}
