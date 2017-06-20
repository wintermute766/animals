package ru.sberbank.mobile.core.parser;

import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

import java.math.BigDecimal;

import ru.sberbank.mobile.core.format.DecimalFormatter;
import ru.sberbank.mobile.core.utils.LocaleUtils;

/**
 * @author QuickNick.
 */
public class SimpleXmlRuBigDecimalConverter implements Converter<BigDecimal> {
    @Override
    public BigDecimal read(InputNode inputNode) throws Exception {
        BigDecimal decimal = DecimalFormatter.parseBigDecimal(inputNode.getValue(), LocaleUtils.getRussianLocale());
        return decimal;
    }

    @Override
    public void write(OutputNode outputNode, BigDecimal bigDecimal) throws Exception {
        outputNode.setValue(DecimalFormatter.format(bigDecimal, LocaleUtils.getRussianLocale()));
    }
}
