package ru.sberbank.mobile.common.rate.entity;

import com.google.common.base.Objects;

import org.simpleframework.xml.ElementList;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.sberbank.mobile.core.bean.money.Currency;
import ru.sberbank.mobile.core.bean.operation.ServerEntity;
import ru.sberbank.mobile.core.network.ConnectorStatus;
import ru.sberbank.mobile.core.utils.CollectionUtils;

/**
 * @author QuickNick.
 */
public class RatesBundle extends ServerEntity {

    private static final Rate RUB_RATE = new Rate()
            .setCurrency(Currency.RUB)
            .setRelation(BigDecimal.ONE)
            .setScale(BigDecimal.ONE);

    private List<Rate> mRates;
    private Map<Currency, Rate> mRateMap;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RatesBundle)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        RatesBundle bundle = (RatesBundle) o;
        return Objects.equal(mRates, bundle.mRates);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), mRates);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("super", super.toString())
                .add("mRates", mRates)
                .toString();
    }

    @Override
    public boolean isSuccess() {
        return getConnectorStatus() == ConnectorStatus.SUCCESS;
    }

    @ElementList(entry = "Valute", inline = true)
    public List<Rate> getRates() {
        return CollectionUtils.wrapList(mRates);
    }

    @ElementList(entry = "Valute", inline = true)
    public void setRates(List<Rate> rates) {
        mRates = CollectionUtils.wrapList(rates);
        mRateMap = new HashMap<>();
        if (mRates != null) {
            mRates.add(0, RUB_RATE);
            for (Rate rate : mRates) {
                mRateMap.put(rate.getCurrency(), rate);
            }
        }
    }

    public Rate getRateByCurrency(Currency currency) {
        return mRateMap.get(currency);
    }
}
