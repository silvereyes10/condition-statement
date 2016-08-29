package com.fimm;

import java.util.Map;

/**
 * @author fimm(seo.seokho_nhn.com)
 */
public interface MapCondition extends Condition {
    @Override
    default  Class[] getCompatibleClasses() {
        return new Class[]{Map.class};
    }
}
