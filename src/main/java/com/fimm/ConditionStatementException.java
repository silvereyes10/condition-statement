package com.fimm;

/**
 * @author fimm(seo.seokho_nhn.com)
 */
public class ConditionStatementException extends RuntimeException {
    public ConditionStatementException() {
    }

    public ConditionStatementException(String message) {
        super(message);
    }

    public ConditionStatementException(String message, Throwable cause) {
        super(message, cause);
    }
}
