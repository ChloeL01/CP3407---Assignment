package com.example.cp3407_assignment.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.databinding.FragmentDoggoInformationBinding
import com.squareup.picasso.Picasso


class DoggoInformation : Fragment() {
    private var _binding: FragmentDoggoInformationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDoggoInformationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        Picasso.with(context)
            .load(arguments?.getString("imageUrl"))
            .fit()
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.ic_baseline_error_24)
            .centerCrop()
            .into(binding.imageView)

        (activity as AppCompatActivity).supportActionBar?.title = arguments?.getString("doggo_name")
        binding.textViewName.text = arguments?.getString("doggo_name")
        binding.textViewBreed.text = arguments?.getString("doggo_breed")
        binding.textViewDescription.text = arguments?.getString("description")
        binding.textViewReviews.text = arguments?.getString("reviews")


        binding.submitReview.setOnClickListener {
            val dogId = arguments?.getString("dogId")
            val bundle = bundleOf("dogId" to dogId)
            findNavController().navigate(R.id.action_doggoInformation_to_dogReview, bundle)
        }
        return root
    }


        binding.selectAvailability.setOnClickListener {
            val bundle =
                bundleOf(
                    "doggo_name" to arguments?.getString("doggo_name"),
                    "doggo_breed" to arguments?.getString("doggo_breed"),
                    "imageUrl" to arguments?.getString("imageUrl"),
                    "description" to arguments?.getString("description"),
                    "reviews" to arguments?.getString("reviews"),
                    "start_date" to arguments?.getString("start_date"),
                    "end_date" to arguments?.getString("end_date"),
                    "cost" to arguments?.getString("cost"),
                    "owner_id" to arguments?.getString("owner_id"),
                    "owner_contact" to arguments?.getString("owner_contact"),
                    "hiree" to arguments?.getString("hiree")
                )
            findNavController().navigate(
                        R.id.action_doggoInformation_to_hireItem,
                        bundle)
        }

        binding.fab.setOnClickListener{
            shareSuccess()
        }

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    val bundle =
                        bundleOf(
                            //"search_doggos" to arguments?.getStringArrayList("doggos")
                            "navigation" to true
                        )
                    findNavController().popBackStack()
//                    findNavController().navigate(
//                        R.id.action_doggoInformation_to_navigation_home,
//                        bundle)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        return root
    }

    private fun shareSuccess() {
        startActivity(getShareIntent())
    }

    private fun getShareIntent(): Intent {
        val shareIntent = Intent(Intent.ACTION_SEND)
        val doggo_name = arguments?.getString("doggo_name")
        val start_date = arguments?.getString("start_date")
        val end_date = arguments?.getString("end_date")
        val price = arguments?.getString("cost")
        shareIntent.setType("text/plain")
            .putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.share_doggos_text, doggo_name, start_date, end_date, price)
            )
        return shareIntent
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}