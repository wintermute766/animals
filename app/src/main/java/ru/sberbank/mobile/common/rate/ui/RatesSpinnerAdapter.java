package ru.sberbank.mobile.common.rate.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.sberbank.backgroundtaskssample.R;
import ru.sberbank.mobile.common.rate.entity.Rate;
import ru.sberbank.mobile.core.utils.CollectionUtils;

/**
 * @author QuickNick
 */
public class RatesSpinnerAdapter extends BaseAdapter {

    private final List<Rate> mRates;

    public RatesSpinnerAdapter() {
        mRates = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mRates.size();
    }

    @Override
    public Rate getItem(int position) {
        return mRates.get(position);
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

    public void setRates(List<Rate> rates) {
        CollectionUtils.clearAndFill(rates, mRates);
        notifyDataSetChanged();
    }

    private View newView(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return inflater.inflate(R.layout.simple_text_item, parent, false);
    }

    private void bindView(View itemView, int position) {
        TextView textView = (TextView) itemView;
        Rate rate = mRates.get(position);
        textView.setText(rate.getCurrency().getStringResource());
    }
}
