package com.example.cp3407_assignment.ui.cart

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cp3407_assignment.Dog
import com.example.cp3407_assignment.R
import com.example.cp3407_assignment.databinding.FragmentCartBinding
import com.google.gson.Gson
import java.util.ArrayList


class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var cartViewModel: CartViewModel

    private var mPurchaseList = ArrayList<Dog>()
    private lateinit var mAdapter: CartImageAdapter
    private lateinit var mRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]
        val root: View = binding.root

        mRecyclerView = binding.recyclerView
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(context)

        if (arguments != null) {
            val dog = constructDog()
            mPurchaseList = arrayListOf(dog)
            binding.totalCost.text = dog.cost
            cartViewModel.setDoggos(mPurchaseList)
        }

        binding.selectAvailability.setOnClickListener { findNavController().navigate(
            R.id.action_navigation_cart_to_paymentFragment) }

        cartViewModel.getDoggos().observe(viewLifecycleOwner) {
            updateRecyclerView(it)
        }
        return root
    }

    private fun constructDog(): Dog {
        val doggo_name = requireArguments().getString("doggo_name", "")
        val doggo_breed = requireArguments().getString("doggo_breed", "")
        val description = requireArguments().getString("description", "")
        val hire_start_date = requireArguments().getString("start_date", "")
        val hire_end_date = requireArguments().getString("end_date", "")
        val cost = requireArguments().getString("cost", "")
        val doggo_review = requireArguments().getString("reviews", "")
        val owner_id = requireArguments().getString("owner_id", "")
        val owner_contact = requireArguments().getString("owner_contact", "")
        val imageUrl = requireArguments().getString("imageUrl", "")
        val hiree = requireArguments().getString("hiree", "")
        return Dog(
            doggo_name,
            doggo_breed,
            description,
            hire_start_date,
            hire_end_date,
            cost,
            doggo_review,
            owner_id,
            owner_contact,
            imageUrl,
            hiree
        )
    }

    private fun savePreference() {
        val sharedPref: SharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val gson = Gson()
        val json: String = gson.toJson(mPurchaseList)
        editor.putString("purchaseList", json)
        editor.apply()
    }

    private fun updateRecyclerView(doggoList: ArrayList<Dog>) {
        mRecyclerView.scrollToPosition(0)

        mAdapter = context?.let { CartImageAdapter(it, doggoList) }!!
        mRecyclerView.adapter = mAdapter

        mAdapter.setOnClickListener(object : CartImageAdapter.OnClickListener {
            override fun onClick(position: Int, model: Dog) {
                val bundle = bundleOf(
                    "doggo_name" to model.doggo_name,
                    "doggo_breed" to model.doggo_breed,
                    "imageUrl" to model.imageUrl,
                    "description" to model.description,
                    "reviews" to model.doggo_review,
                    "start_date" to model.hire_start_date,
                    "end_date" to model.hire_end_date,
                    "cost" to model.cost
                    // "doggos" to searchList
                )
                savePreference()
                findNavController().navigate(
                    R.id.action_navigation_home_to_doggoInformation, bundle
                )
//                findNavController().addOnDestinationChangedListener { controller, destination, arguments ->
//                    when(destination.id) {
//                        R.id.navigation_cart -> {
//                            val argument = NavArgument.Builder().setDefaultValue(6).build()
//                            destination.addArgument("Argument", argument)
//                        }
//                    }
//                }
//                val navView: BottomNavigationView
//                val item = navView.menu.findItem(R.id.navigation_cart)//val item = findNavController(). .menu.findItem(R.id.reportsFragment)
//                NavigationUI.onNavDestinationSelected(item, findNavController())
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}