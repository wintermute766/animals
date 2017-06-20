package ru.sberbank.mobile.common.animal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.sberbank.backgroundtaskssample.R;

/**
 * @author QuickNick
 */
public class AnimalsAdapter extends BaseAdapter {

    private final List<Animal> mAnimals;

    public AnimalsAdapter() {
        mAnimals = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mAnimals.size();
    }

    @Override
    public Animal getItem(int position) {
        return mAnimals.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = newView(parent);
        }
        bindView(itemView, position);
        return itemView;
    }

    public void setAnimals(List<Animal> animals) {
        mAnimals.clear();
        mAnimals.addAll(animals);
        notifyDataSetChanged();
    }

    private View newView(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.animal_list_item, parent, false);
        view.setTag(new ViewHolder(view));
        return view;
    }

    private void bindView(View itemView, int position) {
        ViewHolder holder = (ViewHolder) itemView.getTag();
        Animal animal = mAnimals.get(position);
        Context context = itemView.getContext();
        holder.speciesTextView.setText(composeString(context, R.string.species, animal.getSpecies()));
        holder.ageTextView.setText(composeString(context, R.string.age, String.valueOf(animal.getAge())));
        holder.nameTextView.setText(composeString(context, R.string.name, animal.getName()));
    }

    private static String composeString(Context context, int propertyResId, String propertyValue) {
        StringBuilder sb = new StringBuilder();
        sb.append(context.getString(propertyResId)).append(": ").append(propertyValue);
        return sb.toString();
    }

    private static class ViewHolder {

        public final TextView speciesTextView;
        public final TextView ageTextView;
        public final TextView nameTextView;

        public ViewHolder(View itemView) {
            speciesTextView = (TextView) itemView.findViewById(R.id.species_text_view);
            ageTextView = (TextView) itemView.findViewById(R.id.age_text_view);
            nameTextView = (TextView) itemView.findViewById(R.id.name_text_view);
        }
    }
}
