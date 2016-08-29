package com.fimm;

/**
 * @author fimm(seo.seokho_nhn.com)
 */
public interface StringCondition extends Condition {
    @Override
    default Class[] getCompatibleClasses() {
        return new Class[]{String.class};
    }
}
