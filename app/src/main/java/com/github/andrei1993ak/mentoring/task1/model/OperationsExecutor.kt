package com.github.andrei1993ak.mentoring.task1.model

import com.github.andrei1993ak.mentoring.task1.model.operations.MathOperation

interface OperationsExecutor {
    fun calculateResult(firstField: Double, secondField: Double, operation: MathOperation): Double
}

class OperationExecutorImpl : OperationsExecutor {
    override fun calculateResult(firstField: Double, secondField: Double, operation: MathOperation) = operation.execute(firstField, secondField)
}