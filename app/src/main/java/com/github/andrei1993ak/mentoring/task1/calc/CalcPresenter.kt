package com.github.andrei1993ak.mentoring.task1.calc

import android.text.Editable
import android.text.InputType
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.github.andrei1993ak.mentoring.task1.OperationsTags
import com.github.andrei1993ak.mentoring.task1.model.OperationExecutorImpl
import com.github.andrei1993ak.mentoring.task1.model.OperationsExecutor
import com.github.andrei1993ak.mentoring.task1.model.operations.*

@InjectViewState
class CalcPresenter : MvpPresenter<CalcView>() {

    private var mUseFloats = true
    private var mUseSigned = true
    private var selectedMathOperation: Int? = null

    private val operationExecutor: OperationsExecutor = OperationExecutorImpl()

    fun getDefaultState() {
        viewState.clearInputs()
        viewState.clearSelectedOptions()
    }

    fun getResult(operand1: Editable, operand2: Editable) {
        try {
            val firstField = operand1.toString().toDouble()
            val secondField = operand2.toString().toDouble()

            selectedMathOperation?.let { tag ->
                getOperation(tag)?.let { operation ->
                    val result = operationExecutor.calculateResult(firstField, secondField, operation)
                    viewState.showResult(result.toString())
                } ?: viewState.showError("Select Operation")
            } ?: viewState.showError("Select Operation")
        } catch (e: Exception) {
            viewState.showError(e.message.toString())
        }
    }


    fun onUseSignedCheckedChanged(isSingleChecked: Boolean) {
        mUseSigned = isSingleChecked

        calcInputType(mUseFloats, mUseSigned)
    }

    fun onUseFloatCheckedChanged(isFloatChecked: Boolean) {
        mUseFloats = isFloatChecked

        calcInputType(mUseFloats, mUseSigned)
    }

    private fun calcInputType(pUseFloats: Boolean, pUseSigned: Boolean) {
        var contentType: Int
        contentType = InputType.TYPE_CLASS_NUMBER

        if (pUseSigned) {
            contentType = contentType or InputType.TYPE_NUMBER_FLAG_SIGNED
        }

        if (pUseFloats) {
            contentType = contentType or InputType.TYPE_NUMBER_FLAG_DECIMAL
        }

        viewState.clearInputs()
        viewState.setInputFieldsContentType(contentType)
    }

    fun onOperationChecked(operationTag: Int) {
        selectedMathOperation = operationTag
    }

    private fun getOperation(tag: Int): MathOperation? {
        return when (tag) {
            OperationsTags.PLUS -> OperationPlus()
            OperationsTags.MINUS -> OperationMinus()
            OperationsTags.MULTIPLY -> OperationMultiply()
            OperationsTags.DIVIDE -> OperationDivide()
            else -> null
        }
    }
}