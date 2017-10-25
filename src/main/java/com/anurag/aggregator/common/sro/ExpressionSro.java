package com.anurag.aggregator.common.sro;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExpressionSro implements Serializable {

    private static final long serialVersionUID = 6030236406347387192L;

    private String expression;

    private String type;

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ExpressionSro{" +
                ", expression='" + expression + '\'' +
                '}';
    }
}
