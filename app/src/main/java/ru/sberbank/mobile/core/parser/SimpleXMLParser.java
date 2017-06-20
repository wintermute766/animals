package ru.sberbank.mobile.core.parser;

import android.support.annotation.NonNull;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.convert.Registry;
import org.simpleframework.xml.convert.RegistryStrategy;
import org.simpleframework.xml.core.ElementException;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.core.ValueRequiredException;
import org.simpleframework.xml.strategy.Strategy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Trikhin P O on 08.06.2016.
 */
public class SimpleXMLParser implements IParser {

    private static final String LOG_TAG = "SimpleXmlParser";

    private final Registry mRegistry;
    private final Strategy mStrategy;
    private final Serializer mSerializer;
    private final boolean mFailOnUnknownProperties;

    public SimpleXMLParser() {
        this(false);
    }

    public SimpleXMLParser(boolean failOnUnknownProperties) {
        mRegistry = new Registry();
        mStrategy = new RegistryStrategy(mRegistry, new AnnotationStrategy());
        mSerializer = new Persister(mStrategy, new EnumMatcher());
        mFailOnUnknownProperties = failOnUnknownProperties;
    }

    @Override
    public <T> T parse(@NonNull InputStream inputStream, @NonNull Class<T> clazz) throws IOException, ParserException {
        T read = null;
        try {
            read = mSerializer.read(clazz, inputStream, mFailOnUnknownProperties);
        } catch (IOException e) {
            throw e;
        } catch (ElementException e) {
            throw new ParserException("Error parsing xml (unexpected element in XML)", e);
        } catch (ValueRequiredException e) {
            throw new ParserException("Error parsing xml", e);
        } catch (Exception e) {
            throw new ParserException(e);
        }
        return read;
    }

    @Override
    public void serialize(@NonNull OutputStream outputStream, @NonNull Object object) throws IOException, SerializeException {
        try {
            mSerializer.write(object, outputStream);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new SerializeException(e);
        }
    }
}
