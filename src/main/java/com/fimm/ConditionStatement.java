package com.fimm;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ConditionStatement {
    public static boolean does(Object source, Condition condition) {
        return condition.check(source);
    }

    public static Condition hasEntry(String entryKey) {
        return new Condition() {
            @Override
            public Class[] getCompatibleClasses() {
                return new Class[]{Map.class};
            }

            @Override
            public boolean test(Object source) {
                return ((Map) source).get(entryKey) != null;
            }
        };
    }

    public static Condition hasProperty(String propertyName) {
        return source -> {
            try {
                return (BeanUtils.getProperty(source, propertyName) != null);
            } catch (Exception e) {
                throw new ConditionStatementException("지원하지 않습니다.", e);
            }
        };
    }

    public static Condition eq(Object object) {
        return source -> source.equals(object);
    }

    public static Condition is(Object object) {
        return eq(object);
    }

    public static Condition isNull() {
        return source -> source == null;
    }

    public static Condition isIn(Object... compareObjects) {
        return object -> Arrays.stream(compareObjects).anyMatch(source -> source.equals(object));
    }

    public static Condition isNotIn(Object... compareObjects) {
        return object -> Arrays.stream(compareObjects).noneMatch(source -> source.equals(object));
    }

    public static Condition and(Condition... conditions) {
        return source -> Arrays.stream(conditions).allMatch(condition -> condition.test(source));
    }

    public static Condition or(Condition... conditions) {
        return source -> Arrays.stream(conditions).anyMatch(condition -> condition.test(source));
    }

    public static Condition all() {
        return source -> true;
    }

    public static Condition isContainedIn(List<String> list) {
        return new Condition() {
            @Override
            public Class[] getCompatibleClasses() {
                return new Class[]{String.class};
            }

            @Override
            public boolean test(Object source) {
                return list.stream().anyMatch(compareString -> (StringUtils.equals((String) source, compareString)));
            }
        };
    }

    public static Condition isNotContainedIn(List<String> list) {
        return new Condition() {
            @Override
            public Class[] getCompatibleClasses() {
                return new Class[]{String.class};
            }

            @Override
            public boolean test(Object source) {
                return list.stream().anyMatch(compareString -> (StringUtils.equals((String) source, compareString))) == false;
            }
        };
    }

    public static Condition entry(String entryKey, Condition condition) {
        return new Condition() {
            @Override
            public Class[] getCompatibleClasses() {
                return new Class[]{Map.class};
            }

            @Override
            public boolean test(Object source) {
                return condition.check(((Map) source).get(entryKey));
            }
        };
    }

    public static Condition property(String propertyName, Condition condition) {
        return source -> {
            try {
                return condition.check(BeanUtils.getProperty(source, propertyName));
            } catch (Exception e) {
                throw new ConditionStatementException("지원하지 않습니다.", e);
            }
        };
    }
}
