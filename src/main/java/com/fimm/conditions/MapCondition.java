package com.fimm.conditions;

import java.util.Map;

/**
 * @author fimm(seo.seokho_nhn.com)
 */
public interface MapCondition extends Condition {
	@SuppressWarnings("rawtypes")
	@Override
	default Class[] getCompatibleClasses() {
		return new Class[] { Map.class };
	}
}
