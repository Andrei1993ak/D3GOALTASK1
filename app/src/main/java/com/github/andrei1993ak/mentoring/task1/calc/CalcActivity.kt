package com.github.andrei1993ak.mentoring.task1.calc

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.CompoundButton
import android.widget.RadioButton
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.arellomobile.mvp.presenter.ProvidePresenterTag
import com.github.andrei1993ak.mentoring.task1.OperationsTags
import com.github.andrei1993ak.mentoring.task1.R
import com.github.andrei1993ak.mentoring.task1.Strings
import kotlinx.android.synthetic.main.activity_calculator.*

class CalcActivity : MvpAppCompatActivity(), CalcView {

    @InjectPresenter(type = PresenterType.GLOBAL)
    lateinit var presenter: CalcPresenter

    @ProvidePresenterTag(presenterClass = CalcPresenter::class, type = PresenterType.GLOBAL)
    fun provideDialogPresenterTag(): String = "CalcPresenterTag"

    @ProvidePresenter(type = PresenterType.GLOBAL)
    fun provideDialogPresenter() = CalcPresenter()

    lateinit var mOperationRadioButtons: List<RadioButton>

    override fun onCreate(pSavedInstanceState: Bundle?) {
        super.onCreate(pSavedInstanceState)

        setContentView(R.layout.activity_calculator)

        initViews()
    }

    override fun onCreateOptionsMenu(pMenu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, pMenu)

        return true
    }

    override fun onOptionsItemSelected(pMenuItem: MenuItem): Boolean {
        return if (pMenuItem.itemId == R.id.action_clear) {
            presenter.getDefaultState()

            true
        } else super.onOptionsItemSelected(pMenuItem)
    }

    override fun clearSelectedOptions() {
        mOperationRadioButtons.forEach {
            it.isChecked = false
        }

        checkboxFloatValues.isChecked = true
        checkboxSignedValues.isChecked = true
    }

    override fun showError(error: String) {
        textViewResult.text = error
    }

    override fun showResult(result: String) {
        textViewResult.text = result
    }

    override fun setInputFieldsContentType(contentType: Int) {
        editTextField1.inputType = contentType
        editTextField2.inputType = contentType
    }

    override fun clearInputs() {
        editTextField1.text.clear()
        editTextField2.text.clear()
        textViewResult.text = Strings.EMPTY
    }

    private fun initViews() {
        checkboxFloatValues.setOnCheckedChangeListener { _, isChecked ->
            presenter.onUseFloatCheckedChanged(isChecked)
        }

        checkboxSignedValues.setOnCheckedChangeListener { _, isChecked ->
            presenter.onUseSignedCheckedChanged(isChecked)
        }

        buttonGetResult.setOnClickListener {
            presenter.getResult(editTextField1.text, editTextField2.text)
        }

        radioButtonPlus.tag = OperationsTags.PLUS
        radioButtonMinus.tag = OperationsTags.MINUS
        radioButtonMultiply.tag = OperationsTags.MULTIPLY
        radioButtonDivide.tag = OperationsTags.DIVIDE

        val checkBoxOperationChangeListener = CheckBoxOperationChangeListener()

        mOperationRadioButtons = arrayListOf(radioButtonPlus, radioButtonMinus, radioButtonMultiply, radioButtonDivide)
        mOperationRadioButtons.forEach {
            it.setOnCheckedChangeListener(checkBoxOperationChangeListener)
        }
    }

    private inner class CheckBoxOperationChangeListener : CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(pButtonView: CompoundButton, pIsChecked: Boolean) {
            if (pIsChecked) {
                presenter.onOperationChecked(pButtonView.tag as Int)

                mOperationRadioButtons.forEach {
                    if (it != pButtonView) {
                        it.isChecked = false
                    }
                }
            }
        }
    }
}