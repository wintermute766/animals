package ru.sberbank.mobile.common.rate.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.sberbank.backgroundtaskssample.R;
import ru.sberbank.mobile.common.rate.entity.Rate;
import ru.sberbank.mobile.core.format.DecimalFormatter;
import ru.sberbank.mobile.core.utils.LocaleUtils;

/**
 * @author QuickNick.
 */
public class RateViewHolder extends RecyclerView.ViewHolder {

    private final TextView mNameTextView;
    private final TextView mRateTextView;

    public RateViewHolder(View itemView) {
        super(itemView);
        mNameTextView = (TextView) itemView.findViewById(R.id.currency_name_text_view);
        mRateTextView = (TextView) itemView.findViewById(R.id.rate_text_view);
    }

    public static RateViewHolder newHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.rate_list_item, parent, false);
        return new RateViewHolder(itemView);
    }

    public void bindView(Rate rate) {
        int stringResId = rate.getCurrency().getStringResource();
        mNameTextView.setText(stringResId);
        mRateTextView.setText(DecimalFormatter.format(rate.getScaledRelation(), LocaleUtils.getRussianLocale()));
    }
}
