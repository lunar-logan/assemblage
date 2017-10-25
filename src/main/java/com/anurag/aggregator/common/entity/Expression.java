package com.anurag.aggregator.common.entity;

public class Expression {
//    @Id
    private String id;
    private String expression;
    /**
     * Type of expression, for instance expression could be a spring expression, mule expression or
     * a json path expression, etc
     *
     * @see com.anurag.aggregator.common.type.ExpressionType
     */
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
        return "Expression{" +
                "id='" + id + '\'' +
                ", expression='" + expression + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
