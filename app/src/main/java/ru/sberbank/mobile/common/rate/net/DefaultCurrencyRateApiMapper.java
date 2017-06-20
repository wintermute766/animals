package ru.sberbank.mobile.common.rate.net;

import ru.sberbank.mobile.common.rate.entity.RatesBundle;
import ru.sberbank.mobile.core.bean.io.Encoding;
import ru.sberbank.mobile.core.network.ConnectorException;
import ru.sberbank.mobile.core.network.DefaultHttpResponseReceiver;
import ru.sberbank.mobile.core.network.IHttpConnector;
import ru.sberbank.mobile.core.network.IHttpResponseReceiver;
import ru.sberbank.mobile.core.network.Method;
import ru.sberbank.mobile.core.network.Request;
import ru.sberbank.mobile.core.parser.ExtendedParser;

/**
 * @author QuickNick.
 */
public class DefaultCurrencyRateApiMapper implements ICurrencyRateApiMapper {

    private static final String RATES_HOST = "http://www.cbr.ru";
    private static final String RATES_PATH = "scripts/XML_daily.asp";

    private final IHttpConnector mHttpConnector;
    private final ExtendedParser mParser;

    public DefaultCurrencyRateApiMapper(IHttpConnector httpConnector, ExtendedParser parser) {
        mHttpConnector = httpConnector;
        mParser = parser;
    }

    @Override
    public RatesBundle getRatesBundle() {
        RatesBundle bundle = null;
        try {
            Request request = new Request(Method.GET, RATES_HOST, Encoding.WINDOWS_1251)
                    .setPath(RATES_PATH);
            IHttpResponseReceiver<RatesBundle> responseReceiver = new DefaultHttpResponseReceiver(
                    RatesBundle.class, Encoding.WINDOWS_1251, mParser
            );
            bundle = mHttpConnector.sendAndReceive(request, responseReceiver);
        } catch (ConnectorException e) {
            bundle = new RatesBundle();
            bundle.setConnectorStatus(e.getConnectorStatus());
        }
        return bundle;
    }
}
