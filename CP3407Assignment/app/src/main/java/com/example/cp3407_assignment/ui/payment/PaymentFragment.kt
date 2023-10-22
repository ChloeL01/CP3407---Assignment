package com.example.cp3407_assignment.ui.payment

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.braintreepayments.cardform.view.CardForm
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.databinding.FragmentPaymentBinding

class PaymentFragment : AppCompatActivity() {

    private lateinit var cardForm: CardForm
    private lateinit var binding: FragmentPaymentBinding

    override fun onCreateView(
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View {
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_payment)
        initialiseCardForm(binding)
        initialisePayment(binding)
        return binding.root
    }

    private fun initialisePayment(binding: FragmentPaymentBinding) {
        binding.hirePayment.setOnClickListener {
            if (cardForm.isValid) {
                Toast.makeText(
                    this@PaymentFragment, "Card Number: ${cardForm.cardNumber} \n" +
                            "Card expiry date: ${cardForm.expirationDateEditText} \n" +
                            "Card CVV: ${cardForm.cvv} \n" +
                            "Card holder name: ${cardForm.cardholderName} \n" +
                            "Card postal code: ${cardForm.postalCode} \n" +
                            "Card mobile number: ${cardForm.mobileNumber} \n",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    this@PaymentFragment,
                    "Please complete the payment form.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun initialiseCardForm(binding: FragmentPaymentBinding) {
        cardForm = binding.cardForm
        cardForm.cardRequired(true)
            .expirationRequired(true)
            .cvvRequired(true)
            .cardholderName(CardForm.FIELD_REQUIRED)
            .postalCodeRequired(true)
            .mobileNumberRequired(true)
            .mobileNumberExplanation("Mobile Number required for SMS verification")
            .actionLabel("Pay for Hire")
            .setup(this@PaymentFragment)

        cardForm.cvvEditText.inputType =
            InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
    }


}