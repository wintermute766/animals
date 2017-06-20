package ru.sberbank.mobile.core.parser;

import org.simpleframework.xml.transform.Matcher;
import org.simpleframework.xml.transform.Transform;

public class EnumMatcher implements Matcher {

    @Override
    @SuppressWarnings("unchecked")
    public Transform match(Class aClass) throws Exception {
        if (aClass.isEnum()) {
            return new EnumTransform(aClass);
        }
        return null;
    }

    private static class EnumTransform implements Transform<Enum> {

        final Class<?> mDesiredClass;

        EnumTransform(Class<?> desiredClass) {
            this.mDesiredClass = desiredClass;
        }

        @Override
        @SuppressWarnings("unchecked") // save cast because we checked that in EnumMatcher
        public Enum read(String s) throws Exception {
            return (s == null || s.length() == 0) ? null : Enum.valueOf((Class<? extends Enum>) mDesiredClass, s.trim());
        }

        @Override
        public String write(Enum anEnum) throws Exception {
            return anEnum == null ? null : anEnum.name();
        }
    }
}
