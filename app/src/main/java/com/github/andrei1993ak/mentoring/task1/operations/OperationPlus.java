package com.github.andrei1993ak.mentoring.task1.operations;

public class OperationPlus implements MathOperation {

    @Override
    public Double execute(final Double pOperand1, final Double pOerand2) {
        return pOperand1 + pOerand2;
    }
}
