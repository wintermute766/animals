package ru.sberbank.mobile.core.bean.money;

import android.support.annotation.StringRes;

import java.util.Locale;

import ru.sberbank.backgroundtaskssample.R;

/**
 * Валюты: коды и ресурсы для отображения в UI
 *
 * @author QuickNick.
 */
public enum Currency {

    RUB(R.string.currency_rub),
    USD(R.string.currency_usd),
    EUR(R.string.currency_eur),
    GBP(R.string.currency_gbp),
    AUD(R.string.currency_aud),
    AZN(R.string.currency_azn),
    AMD(R.string.currency_amd),
    BYN(R.string.currency_byn),
    BGN(R.string.currency_bgn),
    BRL(R.string.currency_brl),
    HUF(R.string.currency_huf),
    HKD(R.string.currency_hkd),
    DKK(R.string.currency_dkk),
    INR(R.string.currency_inr),
    KZT(R.string.currency_kzt),
    CAD(R.string.currency_cad),
    KGS(R.string.currency_kgs),
    CNY(R.string.currency_cny),
    MDL(R.string.currency_mdl),
    NOK(R.string.currency_nok),
    PLN(R.string.currency_pln),
    RON(R.string.currency_ron),
    XDR(R.string.currency_xdr),
    SGD(R.string.currency_sgd),
    TJS(R.string.currency_tjs),
    TRY(R.string.currency_try),
    TMT(R.string.currency_tmt),
    UZS(R.string.currency_uzs),
    UAH(R.string.currency_uah),
    CZK(R.string.currency_czk),
    SEK(R.string.currency_sek),
    CHF(R.string.currency_chf),
    ZAR(R.string.currency_zar),
    KRW(R.string.currency_krw),
    JPY(R.string.currency_jpy)
    ;

    @StringRes
    private final int mStringResource;

    Currency(@StringRes int stringResource) {
        mStringResource = stringResource;
    }

    public static Currency findByCode(String code) {
        Currency currency = null;
        if (code != null) {
            String name = code.toUpperCase(Locale.US);
            try {
                currency = Currency.valueOf(name);
            } catch (IllegalArgumentException ex) {
                // код может отсутствовать в нашем списке
            }
        }
        return currency;
    }

    @StringRes
    public int getStringResource() {
        return mStringResource;
    }
}
