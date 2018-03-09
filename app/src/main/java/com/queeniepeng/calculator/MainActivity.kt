package com.queeniepeng.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

private const val STATE_OPERAND1 = "Operand1"
private const val STATE_OPERAND1_STORED = "Operand1_Stored"
private const val STATE_PENDING_OPERATION = "PendingOperation"

class MainActivity : AppCompatActivity() {

    private var operand1: Double? = null
    private var pendingOperation = "="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listener = View.OnClickListener { view ->
            val button = view as Button
            newNumber.append(button.text)
        }

        val buttons: Array<Button> = arrayOf(button0, button1, button2,
                                             button3, button4, button5,
                                             button6, button7, button8,
                                             button9, buttonDot)
        for (button in buttons) {
            button.setOnClickListener(listener)
        }

        val opListener = View.OnClickListener { view ->
            val op = (view as Button).text.toString()
            try {
                val value = newNumber.text.toString().toDouble()
                performOperation(value, op)
            } catch (e: NumberFormatException) {
                newNumber.setText("")
            }

            pendingOperation = op
            operation.text = pendingOperation
        }

        buttonPlus.setOnClickListener(opListener)
        buttonMinus.setOnClickListener(opListener)
        buttonDivide.setOnClickListener(opListener)
        buttonEquals.setOnClickListener(opListener)
        buttonMultiply.setOnClickListener(opListener)

        buttonNeg.setOnClickListener({ view ->
            val value = newNumber.text.toString()
            if (value.isEmpty()) {
                newNumber.setText("-")
            } else {
                try {
                    var doubleValue = value.toDouble()
                    doubleValue *= -1
                    newNumber.setText(doubleValue.toString())
                } catch (e: NumberFormatException) {
                    // new number was "-" or ".", so clear it
                    newNumber.setText("")
                }
            }
        })

        buttonClear.setOnClickListener({ view ->
            newNumber.setText("")
            operation.setText("=")
            result.setText("")
            operand1 = null
            pendingOperation = "="
        })
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        if (operand1 != null) {
            outState?.putDouble(STATE_OPERAND1, operand1!!)
            outState?.putBoolean(STATE_OPERAND1_STORED, true)
        }
        outState?.putString(STATE_PENDING_OPERATION, pendingOperation)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        operand1 = if (savedInstanceState.getBoolean(STATE_OPERAND1_STORED, false)) {
            savedInstanceState.getDouble(STATE_OPERAND1)
        } else {
            null
        }
        pendingOperation = savedInstanceState.getString(STATE_PENDING_OPERATION)
        operation.text = pendingOperation
    }

    private fun performOperation(value: Double, operation: String) {
        if (operand1 == null) {
            operand1 = value
        } else {
            if (pendingOperation == "=") {
                pendingOperation = operation
            }

            when (pendingOperation) {
                "=" -> operand1 = value
                "/" -> operand1 = if (value == 0.0) {
                    Double.NaN // handle attempt to divide by zero
                } else {
                    operand1!! / value
                }
                "*" -> operand1 = operand1!! * value
                "-" -> operand1 = operand1!! - value
                "+" -> operand1 = operand1!! + value
            }
        }
        result.setText(operand1.toString())
        newNumber.setText("")
    }
}




















