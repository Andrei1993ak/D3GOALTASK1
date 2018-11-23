package com.github.andrei1993ak.mentoring.task1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.andrei1993ak.mentoring.task1.operations.MathOperation;
import com.github.andrei1993ak.mentoring.task1.operations.OperationDivide;
import com.github.andrei1993ak.mentoring.task1.operations.OperationMinus;
import com.github.andrei1993ak.mentoring.task1.operations.OperationMultiPly;
import com.github.andrei1993ak.mentoring.task1.operations.OperationPlus;

import java.util.Arrays;
import java.util.List;

public class CalculatorActivity extends AppCompatActivity {

    private EditText mFirstFiled;
    private EditText mSecondFiled;
    private TextView mTextViewResult;
    private List<RadioButton> mOperations;
    private boolean mUseFloats = true;
    private boolean mUseSigned = true;
    private CheckBox mCheckBoxFloatValues;
    private CheckBox mCheckBoxSignedValues;

    @Override
    protected void onCreate(final Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);

        setContentView(R.layout.activity_calculator);
        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu pMenu) {
        getMenuInflater().inflate(R.menu.menu_main, pMenu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem pMenuItem) {
        final int id = pMenuItem.getItemId();

        if (id == R.id.action_clear) {
            clearInputs();

            return true;
        }

        return super.onOptionsItemSelected(pMenuItem);
    }

    private void clearInputs() {
        clearTextFields();

        for (final RadioButton operation : mOperations) {
            operation.setChecked(false);
        }

        mCheckBoxFloatValues.setSelected(true);
        mCheckBoxSignedValues.setSelected(true);
    }

    private void setInputFieldsInputType(final boolean pUseFloats, final boolean pUseSigned) {
        int contentType;
        contentType = InputType.TYPE_CLASS_NUMBER;

        if (pUseSigned) {
            contentType |= InputType.TYPE_NUMBER_FLAG_SIGNED;
        }

        if (pUseFloats) {
            contentType |= InputType.TYPE_NUMBER_FLAG_DECIMAL;
        }

        clearTextFields();

        mFirstFiled.setInputType(contentType);
        mSecondFiled.setInputType(contentType);
    }

    private void initViews() {
        mTextViewResult = findViewById(R.id.text_view_result);

        mFirstFiled = findViewById(R.id.edit_text_field1);
        mSecondFiled = findViewById(R.id.edit_text_field2);
        setInputFieldsInputType(mUseFloats, mUseSigned);

        mCheckBoxFloatValues = findViewById(R.id.checkbox_float_values);
        mCheckBoxSignedValues = findViewById(R.id.checkbox_signed_values);
        mCheckBoxFloatValues.setOnCheckedChangeListener(new FloatSelectedListener());
        mCheckBoxSignedValues.setOnCheckedChangeListener(new SignedSelectedListener());

        final RadioButton radioButtonPlus = findViewById(R.id.radio_button_plus);
        final RadioButton radioButtonMinus = findViewById(R.id.radio_button_minus);
        final RadioButton radioButtonMultiply = findViewById(R.id.radio_button_multiply);
        final RadioButton radioButtonDivide = findViewById(R.id.radio_button_divide);
        radioButtonPlus.setTag(new OperationPlus());
        radioButtonMinus.setTag(new OperationMinus());
        radioButtonMultiply.setTag(new OperationMultiPly());
        radioButtonDivide.setTag(new OperationDivide());
        mOperations = Arrays.asList(radioButtonPlus, radioButtonMinus, radioButtonMultiply, radioButtonDivide);

        final CheckBoxOperationChangeListener checkBoxOperationChangeListener = new CheckBoxOperationChangeListener();

        for (final RadioButton operation : mOperations) {
            operation.setOnCheckedChangeListener(checkBoxOperationChangeListener);
        }

        final Button mGetResultButton = findViewById(R.id.button_get_result);
        mGetResultButton.setOnClickListener(new GetResultClickListener());
    }

    private void clearTextFields() {
        mFirstFiled.getText().clear();
        mSecondFiled.getText().clear();
        mTextViewResult.setText(StringUtil.EMPTY);
    }

    private class CheckBoxOperationChangeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(final CompoundButton pButtonView, final boolean pIsChecked) {
            if (pIsChecked) {
                for (final RadioButton radioButton : mOperations) {
                    if (!radioButton.equals(pButtonView)) {
                        radioButton.setChecked(false);
                    }
                }
            }
        }
    }

    private class GetResultClickListener implements View.OnClickListener {
        @Override
        public void onClick(final View pView) {
            MathOperation selectedMathOperation = null;

            for (final RadioButton operation : mOperations) {
                if (operation.isChecked()) {
                    selectedMathOperation = (MathOperation) operation.getTag();
                    break;
                }
            }

            if (selectedMathOperation == null) {
                Toast.makeText(CalculatorActivity.this, R.string.OPERATION_NOT_FOUND_MESSAGE, Toast.LENGTH_LONG).show();
            } else {
                try {
                    final double firstField = Double.parseDouble(mFirstFiled.getText().toString());
                    final double secondField = Double.parseDouble(mSecondFiled.getText().toString());
                    final Double result = selectedMathOperation.execute(firstField, secondField);

                    mTextViewResult.setText(String.valueOf(result));
                } catch (final Exception pE) {
                    Toast.makeText(CalculatorActivity.this, pE.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private class SignedSelectedListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(final CompoundButton pButtonView, final boolean pIsChecked) {
            mUseSigned = pIsChecked;
            setInputFieldsInputType(mUseFloats, mUseSigned);
        }
    }

    private class FloatSelectedListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(final CompoundButton pButtonView, final boolean pIsChecked) {
            mUseFloats = pIsChecked;
            setInputFieldsInputType(mUseFloats, mUseSigned);
        }
    }
}
