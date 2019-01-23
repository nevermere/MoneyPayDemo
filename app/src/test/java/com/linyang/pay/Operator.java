package com.linyang.pay;

/**
 * 描述:
 * Created by fzJiang on 2018-10-16
 */
public enum Operator {

    ADD("+") {
        @Override
        public int calculate(int a, int b) {
            return a + b;
        }
    },

    DELETE("-") {
        @Override
        public int calculate(int a, int b) {
            return a - b;
        }
    };

    Operator(String operator) {
        this.mOperator = operator;
    }

    private String mOperator;

    public abstract int calculate(int a, int b);

    public String getOperator() {
        return mOperator;
    }

    public void setOperator(String operator) {
        mOperator = operator;
    }
}
