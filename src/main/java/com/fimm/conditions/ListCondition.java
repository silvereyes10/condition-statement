package com.fimm.conditions;

import java.util.List;

/**
 * @author fimm(seo.seokho_nhn.com)
 */
public interface ListCondition extends Condition {
	@SuppressWarnings("rawtypes")
	@Override
	default Class[] getCompatibleClasses() {
		return new Class[] { List.class };
	}
}
