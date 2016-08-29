package com.fimm

import com.fimm.test.JavaBeanForTest
import org.apache.commons.collections.MapUtils
import org.apache.commons.lang3.StringUtils
import spock.lang.Specification

import static com.fimm.ConditionStatement.*

/**
 * @author fimm ( seo.seokho_nhn.com )
 */
class ConditionStatementSpec extends Specification {
    def "condition statement 를 사용하면 가독성 향상의 효과가 있다."() {
        given:
        Map<String, Object> person = new HashMap<String, Object>() {
            {
                put("Name", "Seo")
                put("Age", 30)
            }
        }

        JavaBeanForTest javabean = new JavaBeanForTest() {
            {
                setPropertyString("value")
                setPropertyInteger(100)
            }
        }

        expect:
        StringUtils.equals(MapUtils.getString(person, "Name"), "Seo") && MapUtils.getInteger(person, "Age") == 30
        does(person, entry("Name", eq("Seo"))) && does(person, entry("Age", eq(30)))
        does(person,
                and(
                        entry("Name", eq("Seo")),
                        entry("Age", eq(30))
                )
        )
        does(person,
                or(
                        entry("Name", eq("Kim")),
                        entry("Age", eq(30))
                )
        )
        does(person,
                or(
                        entry("Name", eq("Seo")),
                        entry("Age", eq(29))
                )
        )
        does(person,
                or(
                        entry("Name", eq("Kim")),
                        entry("Age", eq(29))
                )
        ) == false

        StringUtils.equals(javabean.getPropertyString(), "value") && javabean.getPropertyInteger() == 100
        does(javabean, property("propertyString", eq("value"))) && does(javabean, property("propertyInteger", eq("100")))
        does(javabean,
                and(
                        property("propertyString", eq("value")),
                        property("propertyInteger", eq("100"))
                )
        )
    }

    def "조건 확인 대상이 Map 인 경우 entry 관련 구문을 사용할 수 있다."() {
        given:
        Map<String, Object> sourceMap = new HashMap<String, Object>() {
            {
                put("Name", "Seo")
                put("number", 100)
            }
        }

        expect:
        does(sourceMap, entry("Name", eq("Seo")))
        does(sourceMap, entry("number", eq(100)))
        does(sourceMap, hasEntry("Name"))
        does(sourceMap, hasEntry("number"))
    }

    def "조건 확인 대상이 JavaBean 인 경우 property 관련 구문을 사용할 수 있다. 이 경우 property value는 모두 String 이다."() {
        given:
        JavaBeanForTest javabean = new JavaBeanForTest() {
            {
                setPropertyString("value")
                setPropertyInteger(100)
            }
        }

        expect:
        does(javabean, property("propertyString", eq("value")))
        does(javabean, property("propertyInteger", eq("100")))
        does(javabean, hasProperty("propertyString"))
        does(javabean, hasProperty("propertyInteger"))
    }

    def "property 구문을 사용할 수 없는 객체인 경우 exception이 발생한다."() {
        given:
        JavaBeanForTest javabean = new JavaBeanForTest() {
            {
                setPropertyString("value")
                setPropertyInteger(100)
            }
        }

        when:
        does(javabean, property("unknownPropertyName", eq("abc")))

        then:
        thrown(ConditionStatementException)
    }

    def "property 구문을 사용할 수 없는 객체인 경우 hasProperty 구문은 exception이 발생한다."() {
        given:
        JavaBeanForTest javabean = new JavaBeanForTest() {
            {
                setPropertyString("value")
                setPropertyInteger(100)
            }
        }

        when:
        does(javabean, hasProperty("unknownPropertyName"))

        then:
        thrown(ConditionStatementException)
    }

    def "eq 구문은 동일성을 판단한다."() {
        expect:
        does("A", eq("A"))
        does(100, eq(100))
        does(new ArrayList(), eq(new ArrayList()))
        does(new HashMap(), eq(new HashMap()))
    }

    def "isNull 구문은 null 상태를 판단한다."() {
        given:
        List nullList
        Map nullMap

        expect:
        does(nullList, isNull())
        does(nullMap, isNull())
    }

    def "isIn 구문은 배열(Object 배열 - primitive 타입은 오동작 가능성 존재) 내 해당 객체가 존재하는지 판단한다."() {
        given:
        String[] testStrings = ["A", "B", "C"]
        Integer[] testInts = [1, 2, 3]

        expect:
        does("B", isIn(testStrings))
        does(2, isIn(testInts))
    }

    def "isNotIn 구문은 배열(Object 배열 - primitive 타입은 오동작 가능성 존재) 내 해당 객체가 존재하지 않는지 판단한다."() {
        given:
        String[] testStrings = ["A", "B", "C"]
        Integer[] testInts = [1, 2, 3]

        expect:
        does("D", isNotIn(testStrings))
        does(0, isNotIn(testInts))
    }

    def "and 구문은 주어진 조건을 모두 만족하는지 판단한다."() {
        given:
        Integer[] multiple2 = [2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30]
        Integer[] multiple3 = [3, 6, 9, 12, 15, 18, 21, 24, 27, 30]
        Integer[] multiple5 = [5, 10, 15, 20, 25, 30]

        expect:
        does(2, and(isIn(multiple2)))
        does(4, and(isIn(multiple2), isNotIn(multiple3)))
        does(6, and(isIn(multiple2), isIn(multiple3)))
        does(10, and(isIn(multiple2), isIn(multiple5)))
        does(15, and(isIn(multiple3), isIn(multiple5), isNotIn(multiple2)))
        does(30, and(isIn(multiple2), isIn(multiple3), isIn(multiple5)))
    }

    def "isNotContainedIn 구문은 포함되지 주어진 List<String> 에 포함되지 않았는지 판단한다."() {
        given:
        List<String> testList = new ArrayList() {
            {
                add("a1")
                add("b2")
                add("c3")
            }
        }

        expect:
        does("b2", isContainedIn(testList))
        does("c1", isNotContainedIn(testList))
    }
    
    def "all 구문은 항상 true로 판단한다."() {
        expect:
        does("a", all())
        does(0, all())
        does(new HashMap(), all())
        does(new ArrayList(), all())
    }
}