package ru.sberbank.mobile.common.rate.entity;

import com.google.common.base.Objects;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.convert.Convert;

import java.math.BigDecimal;

import ru.sberbank.mobile.core.bean.money.Currency;
import ru.sberbank.mobile.core.bean.money.convert.SimpleXmlCurrencyConverter;
import ru.sberbank.mobile.core.parser.SimpleXmlRuBigDecimalConverter;

/**
 * @author QuickNick.
 */
public class Rate {

    @Element(name = "CharCode")
    @Convert(SimpleXmlCurrencyConverter.class)
    private Currency mCurrency;
    @Element(name = "Value")
    @Convert(SimpleXmlRuBigDecimalConverter.class)
    private BigDecimal mRelation;
    @Element(name = "Nominal")
    @Convert(SimpleXmlRuBigDecimalConverter.class)
    private BigDecimal mScale;

    public Rate() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rate)) {
            return false;
        }
        Rate rate = (Rate) o;
        return mCurrency == rate.mCurrency &&
                Objects.equal(mRelation, rate.mRelation) &&
                Objects.equal(mScale, rate.mScale);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mCurrency, mRelation, mScale);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("mCurrency", mCurrency)
                .add("mRelation", mRelation)
                .add("mScale", mScale)
                .toString();
    }

    public Currency getCurrency() {
        return mCurrency;
    }

    public Rate setCurrency(Currency currency) {
        mCurrency = currency;
        return this;
    }

    public BigDecimal getRelation() {
        return mRelation;
    }

    public Rate setRelation(BigDecimal relation) {
        mRelation = relation;
        return this;
    }

    public BigDecimal getScale() {
        return mScale;
    }

    public Rate setScale(BigDecimal scale) {
        mScale = scale;
        return this;
    }

    public BigDecimal getScaledRelation() {
        BigDecimal result = BigDecimal.ZERO;
        if (mRelation != null) {
            result = mRelation;
            if (mScale != null) {
                result = result.divide(mScale);
            }
        }
        return result;
    }
}
