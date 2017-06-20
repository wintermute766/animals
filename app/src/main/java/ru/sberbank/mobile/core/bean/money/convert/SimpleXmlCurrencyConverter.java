package ru.sberbank.mobile.core.bean.money.convert;

import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

import ru.sberbank.mobile.core.bean.money.Currency;

/**
 * @author QuickNick.
 */
public class SimpleXmlCurrencyConverter implements Converter<Currency> {
    @Override
    public Currency read(InputNode inputNode) throws Exception {
        return Currency.valueOf(inputNode.getValue());
    }

    @Override
    public void write(OutputNode outputNode, Currency currency) throws Exception {
        outputNode.setValue(currency.name());
    }
}
