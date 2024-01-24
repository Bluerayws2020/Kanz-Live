package com.blueray.Kanz.ui.fragments



import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.Kanz.R
import com.blueray.Kanz.adapters.SearchAdapters
import com.blueray.Kanz.api.OnProfileSearch
import com.blueray.Kanz.databinding.SearchFragmentsBinding
import com.blueray.Kanz.helpers.ViewUtils.hide
import com.blueray.Kanz.helpers.ViewUtils.show
import com.blueray.Kanz.model.NetworkResults
import com.blueray.Kanz.ui.viewModels.AppViewModel


class SearchFragment : Fragment() {
    private lateinit var adapter : SearchAdapters
    private lateinit var binding : SearchFragmentsBinding
    private lateinit var navController: NavController


    private val mainViewModel by viewModels<AppViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SearchFragmentsBinding.inflate(layoutInflater)
//        setUpRecyclerView()
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
binding.searchs.show()
        getMainVidos()

        binding.noData.show()

        binding.searchs.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // Perform your search operation here
                binding.progressBar.show()
                Toast.makeText(requireContext() , "Hello" , Toast.LENGTH_LONG).show()
                Log.d("searchhhhhh" , "hello")
                mainViewModel.retrivesearchTxt(binding.searchs.text.toString())
                true
            } else {
                false
            }
        }



    }
//    private fun setUpRecyclerView() {
//        adapter = SearchAdapters(listOf())
//        val lm  = LinearLayoutManager(requireContext())
//        binding.recyclerView.adapter = adapter
//        binding.recyclerView.layoutManager = lm
//    }


    fun getMainVidos(){
        mainViewModel.getSerchData().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
                 binding.progressBar.hide()


                    if(result.data.results.isNullOrEmpty()){
                        binding.noData.show()
                    }else {
                        binding.noData.hide()

                    }
                    adapter = SearchAdapters(result.data.results ,object : OnProfileSearch {
                        override fun onProfileTargetSearch(pos: Int) {
                            var swipedItem  = result.data.results[pos]
                            val bundle = Bundle().apply {
//                            if (swipedItem.type == "poet") {



                                putString(
                                    "usernName",
                                    swipedItem.user_name
                                ) // Use your item's unique identifier
                                putString(
                                    "userIdes",
                                    swipedItem.id.toString()
                                ) // Use your item's unique identifier
                                putString(
                                    "userImg",
                                    swipedItem.profile_image
                                ) // Use your item's unique identifier
                                putString(
                                    "fullname",
                                    swipedItem.first_name + swipedItem.last_name
                                ) // Use your item's unique identifier

                                putString(
                                    "numOfFollowers",
                                    ""
                                ) // Use your item's unique identifier

                                putString(
                                    "numOfFollowing",
                                    ""
                                ) // Use your item's unique identifier


                                putString(
                                    "numOfLikes",
                                    ""
                                ) // Use your item's unique identifier


                                putString(
                                    "target_user_follow_flag",
                                    ""
                                ) // Use your item's unique identifier





                            }

                            navController.navigate(R.id.partitionChannelFragment, bundle)


                        }
                    })
                    val lm  = LinearLayoutManager(requireContext())
                    binding.recyclerView.adapter = adapter
                    binding.recyclerView.layoutManager = lm

binding.progressBar.hide()
                }


                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    binding.progressBar.hide()

                }

                is NetworkResults.NoInternet -> TODO()
            }
        }
    }
    }




