package com.github.andrei1993ak.mentoring.task1.calc

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface CalcView : MvpView {

    fun showResult(result: String)
    fun showError(error: String)

    fun setInputFieldsContentType(contentType: Int)

    fun clearInputs()
    fun clearSelectedOptions()
}
