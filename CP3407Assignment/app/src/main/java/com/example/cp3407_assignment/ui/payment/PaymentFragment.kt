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
    lateinit var cardInfoArray: MutableList<String>

    override fun onCreateView(
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View {
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_payment)
        initialiseCardForm(binding)
        initialisePayment(binding)

        //TODO add list contents to database

        return binding.root
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


    private fun initialisePayment(binding: FragmentPaymentBinding) {
        val num = cardForm.cardNumber
        val expireDate = cardForm.expirationDateEditText.toString()
        val cvv = cardForm.cvv
        val cardHolderName = cardForm.cardholderName
        val postCode = cardForm.postalCode
        val mobileNum = cardForm.mobileNumber
        binding.hirePayment.setOnClickListener {
            if (cardForm.isValid) {
                Toast.makeText(
                    this@PaymentFragment, "Card holder name: $cardHolderName \n" +
                            "Card Number: $num \n" +
                            "Card CVV: $cvv \n" +
                            "Card expiry date: $expireDate \n" +
                            "Card postal code: $postCode \n" +
                            "Card mobile number: $mobileNum \n",
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
        binding.cardName.setText(cardHolderName)
        binding.cardNumber.setText(num)
        binding.cardCcvNumber.setText(cvv)
        binding.cardExpiryDate.setText(expireDate)
        binding.cardPostCode.setText(postCode)
        binding.cardMobileNumber.setText(mobileNum)
    }

    fun onClickPaymentConfirmation(cardInfoArray: Array<String>): MutableList<String> {
        val cardInfoList = cardInfoArray.toMutableList()
        cardInfoList.add(binding.cardName.toString())
        cardInfoList.add(binding.cardNumber.toString())
        cardInfoList.add(binding.cardCcvNumber.toString())
        cardInfoList.add(binding.cardExpiryDate.toString())
        cardInfoList.add(binding.cardPostCode.toString())
        cardInfoList.add(binding.cardMobileNumber.toString())
        return cardInfoList
    }

}