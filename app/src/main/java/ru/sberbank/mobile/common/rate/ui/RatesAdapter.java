package ru.sberbank.mobile.common.rate.ui;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.sberbank.mobile.common.rate.entity.Rate;
import ru.sberbank.mobile.core.utils.CollectionUtils;

/**
 * @author QuickNick.
 */
public class RatesAdapter extends RecyclerView.Adapter<RateViewHolder> {

    private final List<Rate> mRates;

    public RatesAdapter() {
        mRates = new ArrayList<>();
    }

    @Override
    public RateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return RateViewHolder.newHolder(parent);
    }

    @Override
    public void onBindViewHolder(RateViewHolder holder, int position) {
        holder.bindView(mRates.get(position));
    }

    @Override
    public int getItemCount() {
        return mRates.size();
    }

    public void setRates(List<Rate> rates) {
        CollectionUtils.clearAndFill(rates, mRates);
        notifyDataSetChanged();
    }
}
