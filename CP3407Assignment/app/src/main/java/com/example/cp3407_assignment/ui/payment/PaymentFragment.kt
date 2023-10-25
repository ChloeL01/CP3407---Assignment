package com.example.cp3407_assignment.ui.payment

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.braintreepayments.cardform.view.CardForm
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.databinding.FragmentPaymentBinding

class PaymentFragment : Fragment() {

    private lateinit var cardForm: CardForm
    private lateinit var binding: FragmentPaymentBinding
    private lateinit var cardInfoArray: Array<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_payment, container, false)
        binding.hirePayment.setOnClickListener {
            onClickPaymentConfirmation(cardInfoArray)
        }
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
            .setup(requireActivity())

        cardForm.cvvEditText.inputType =
            InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
    }


    private fun initialisePayment(binding: FragmentPaymentBinding) {
//        val num = cardForm.cardNumber
//        val expireDate = cardForm.expirationDateEditText.toString()
//        val cvv = cardForm.cvv
//        val cardHolderName = cardForm.cardholderName
//        val postCode = cardForm.postalCode
//        val mobileNum = cardForm.mobileNumber
        binding.hirePayment.setOnClickListener {
            if (cardForm.isValid) {
                Toast.makeText(
                    requireActivity(), "Payment Successful!",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    requireActivity(),
                    "Please complete the payment form.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
//        binding.cardName.setText(cardHolderName)
//        binding.cardNumber.setText(num)
//        binding.cardCcvNumber.setText(cvv)
//        binding.cardExpiryDate.setText(expireDate)
//        binding.cardPostCode.setText(postCode)
//        binding.cardMobileNumber.setText(mobileNum)
    }

    private fun onClickPaymentConfirmation(cardInfoArray: Array<String>): MutableList<String> {
        val cardInfoList = cardInfoArray.toMutableList()
//        cardInfoList.add(binding.cardName.toString())
//        cardInfoList.add(binding.cardNumber.toString())
//        cardInfoList.add(binding.cardCcvNumber.toString())
//        cardInfoList.add(binding.cardExpiryDate.toString())
//        cardInfoList.add(binding.cardPostCode.toString())
//        cardInfoList.add(binding.cardMobileNumber.toString())
        return cardInfoList
    }

}