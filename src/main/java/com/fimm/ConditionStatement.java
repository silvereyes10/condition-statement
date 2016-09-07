package com.fimm;

import com.fimm.conditions.Condition;
import com.fimm.conditions.ListCondition;
import com.fimm.conditions.MapCondition;
import com.fimm.conditions.StringCondition;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

interface ConditionStatement {
    String NO_COMPATIBLE_SOURCE = "지원하지 않습니다.";

    static boolean does(Object source, Condition condition) {
        return condition.check(source);
    }

    static boolean is(Object source, Condition condition) {
        return condition.check(source);
    }

    static Condition eq(Object object) {
        return source -> source.equals(object);
    }

    static Condition is(Object object) {
        return eq(object);
    }

    static Condition isNull() {
        return source -> source == null;
    }

    static Condition isIn(Object... compareObjects) {
        return object -> Arrays.stream(compareObjects).anyMatch(source -> source.equals(object));
    }

    static Condition isNotIn(Object... compareObjects) {
        return object -> Arrays.stream(compareObjects).noneMatch(source -> source.equals(object));
    }

    static Condition and(Condition... conditions) {
        return source -> Arrays.stream(conditions).allMatch(condition -> condition.test(source));
    }

    static Condition or(Condition... conditions) {
        return source -> Arrays.stream(conditions).anyMatch(condition -> condition.test(source));
    }

    static Condition all() {
        return source -> true;
    }

    static StringCondition isContainedIn(List<String> list) {
        return source -> list.stream().anyMatch(compareString -> (StringUtils.equals((String) source, compareString)));
    }

    static StringCondition isNotContainedIn(List<String> list) {
        return source -> list.stream().anyMatch(compareString -> (StringUtils.equals((String) source, compareString))) == false;
    }

    static StringCondition empty() {
        return source -> StringUtils.isEmpty((String) source);
    }

    static Condition hasProperty(String propertyName) {
        return source -> {
            try {
                return (BeanUtils.getProperty(source, propertyName) != null);
            } catch (Exception e) {
                throw new ConditionStatementException(NO_COMPATIBLE_SOURCE, e);
            }
        };
    }

    static Condition property(String propertyName, Condition condition) {
        return source -> {
            try {
                return condition.check(BeanUtils.getProperty(source, propertyName));
            } catch (Exception e) {
                throw new ConditionStatementException(NO_COMPATIBLE_SOURCE, e);
            }
        };
    }

    static MapCondition hasEntry(String entryKey) {
        return source -> ((Map<?, ?>) source).get(entryKey) != null;
    }

    static MapCondition entry(String entryKey, Condition condition) {
        return source -> condition.check(((Map<?, ?>) source).get(entryKey));
    }

    static ListCondition firstData(Condition condition) {
        return source -> condition.check(((List<?>)source).get(0));
    }
}
