package ru.sberbank.mobile.common.rate.net;

import ru.sberbank.mobile.core.network.DefaultHttpConnector;
import ru.sberbank.mobile.core.parser.ExtendedParser;
import ru.sberbank.mobile.core.parser.SimpleXMLParser;

/**
 * @author QuickNick
 */
public class ApiFactory {

    public static ICurrencyRateApiMapper createApi() {
        ICurrencyRateApiMapper api = new DefaultCurrencyRateApiMapper(
                new DefaultHttpConnector(),
                new ExtendedParser(new SimpleXMLParser())
        );
        return api;
    }
}
