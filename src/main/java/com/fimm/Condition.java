package com.fimm;

import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author fimm(seo.seokho_nhn.com)
 */
@FunctionalInterface
public interface Condition {
    Class[] ALL = null;

    boolean test(Object source);

    /**
     * Condition 이 사용가능한 대상 object 에 대해 조건을 만족하는지 체크.
     */
    default boolean check(Object object) {
        if (getCompatibleClasses() == ALL) {
            return test(object);
        }

        if (Stream.of(getCompatibleClasses()).anyMatch(compatibleClass -> compatibleClass.isAssignableFrom(object.getClass()))) {
            return test(object);
        } else {
            throw new ConditionStatementException(object.getClass().getTypeName() + "은 지원되지 않습니다.");
        }
    }

    /**
     * 호환되는 클래스를 지정하기 위한 메소드. 재정의되지 않으면 항상 호환가능 한 상태로 본다.
     */
    default Class[] getCompatibleClasses() {
        return ALL;
    }
}
