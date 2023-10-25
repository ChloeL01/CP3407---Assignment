package com.example.cp3407_assignment.ui.dogs

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.databinding.FragmentDogReviewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class DogReview : Fragment() {

    private lateinit var binding: FragmentDogReviewBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val db = Firebase.firestore


    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Not required
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            binding.submitReviewBtn.isEnabled = checkAllEditTextsNotEmpty()
        }

        override fun afterTextChanged(s: Editable?) {
            if (binding.title.text.isEmpty() || binding.reviewDescription.text.isEmpty()) {
                binding.submitReviewBtn.isEnabled = false
            }
        }
    }

    /**
     * Checks if all EditTexts to see if they are empty
     */
    private fun checkAllEditTextsNotEmpty(): Boolean {
        return binding.title.text.toString().isNotEmpty() && binding.reviewDescription.toString()
            .isNotEmpty()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dog_review, container, false)

        firebaseAuth = Firebase.auth

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.title.setOnKeyListener { view, keyCode, _ ->
            handleKeyEvent(view, keyCode)
        }
        binding.reviewDescription.setOnKeyListener { view, keyCode, _ ->
            handleKeyEvent(view, keyCode)
        }

        binding.title.addTextChangedListener(textWatcher)
        binding.reviewDescription.addTextChangedListener(textWatcher)


        binding.submitReviewBtn.setOnClickListener { view: View? ->
            val rating = binding.dogRatingBar.rating.toString()
            val reviewTitle = binding.reviewTitle.text.toString()
            val reviewDescription = binding.reviewDescription.toString()

            // Need to get current dog selected
//            val currentId = arguments?.getString("dogId")
//            val currentDogRef = currentId?.let { db.collection("Dogs").document(it) }

            // Save details of that dog
//            val reviewData = hashMapOf("reviews" to listOf(reviewTitle, reviewDescription, rating))
//
//            currentDogRef?.set(reviewData, SetOptions.merge())?.addOnSuccessListener {
//                Toast.makeText(requireContext(),"Review saved!", Toast.LENGTH_SHORT).show()
//            }?.addOnFailureListener{
//                Toast.makeText(requireContext(),"Review failed to save,", Toast.LENGTH_SHORT).show()
//            }

//            view?.findNavController()?.navigate(R.id.action_dogReview_to_doggoInformation)
        }
    }

    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide keyboard
            val inputMethodManager =
                view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }
}