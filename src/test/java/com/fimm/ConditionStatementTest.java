package com.fimm;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.fimm.ConditionStatement.*;
import static org.junit.Assert.assertThat;

/**
 * @author fimm(seo.seokho_nhn.com)
 */
public class ConditionStatementTest {
    public class JavaBeanForTest {
        private String propertyString;
        private Integer propertyInteger;

        public String getPropertyString() {
            return propertyString;
        }

        public void setPropertyString(String propertyString) {
            this.propertyString = propertyString;
        }

        public Integer getPropertyInteger() {
            return propertyInteger;
        }

        public void setPropertyInteger(Integer propertyInteger) {
            this.propertyInteger = propertyInteger;
        }
    }

    @Test
    // 조건 확인 대상이 Map 인 경우 entry 관련 구문을 사용할 수 있다.
    public void test2() throws Exception {
        //Given
        Map<String, Object> sourceMap = new HashMap<String, Object>() {
            {
                put("Name", "Seo");
                put("number", 100);
            }
        };

        //Expect
        assertThat(does(sourceMap, entry("Name", is("Seo"))), CoreMatchers.is(true));
        assertThat(does(sourceMap, entry("number", is(100))), CoreMatchers.is(true));

        assertThat(does(sourceMap, hasEntry("Name")), CoreMatchers.is(true));
        assertThat(does(sourceMap, hasEntry("number")), CoreMatchers.is(true));
    }

    @Test
    // 조건 확인 대상이 JavaBean 인 경우 property 관련 구문을 사용할 수 있다. 이 경우 property value는 모두 String 이다.
    public void test3() throws Exception {
        //Given
        JavaBeanForTest javabean = new JavaBeanForTest() {
            {
                setPropertyString("value");
                setPropertyInteger(100);
            }
        };

        //Expect
        assertThat(does(javabean, property("propertyString", is("value"))), CoreMatchers.is(true));
        assertThat(does(javabean, property("propertyInteger", is("100"))), CoreMatchers.is(true));
        assertThat(does(javabean, hasProperty("propertyString")), CoreMatchers.is(true));
        assertThat(does(javabean, hasProperty("propertyInteger")), CoreMatchers.is(true));
    }

    public enum Member implements Condition {
        SEO {
            @Override
            public Class[] getCompatibleClasses() {
                return new Class[]{Map.class};
            }

            @Override
            public boolean test(Object source) {
                return does(source,
                    and(
                        entry("Name", eq("Seo")),
                        entry("number", eq(100))
                    )
                );
            }
        }
    }
}