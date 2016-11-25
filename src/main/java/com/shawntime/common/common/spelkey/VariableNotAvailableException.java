package com.shawntime.common.common.spelkey;

import org.springframework.expression.EvaluationException;

/**
 * Created by IDEA
 * User: mashaohua
 * Date: 2016-10-12 10:05
 * Desc:
 */
class VariableNotAvailableException extends EvaluationException {

    private final String name;

    public VariableNotAvailableException(String name) {
        super("Variable '" + name + "' is not available");
        this.name = name;
    }


    public String getName() {
        return name;
    }
}
