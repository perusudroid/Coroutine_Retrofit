package com.perusu.coroutineapp.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.perusu.coroutineapp.*
import com.perusu.coroutineapp.common.ImageLoader
import com.perusu.coroutineapp.data.model.GeneralResult
import com.perusu.coroutineapp.data.model.Dog
import com.perusu.coroutineapp.data.model.ResultOf
import com.perusu.coroutineapp.view.CoroutineViewModel
import com.perusu.coroutineapp.vm.CoroutineVMProvider
import kotlinx.android.synthetic.main.item_dog_of_the_day.view.*
import kotlinx.android.synthetic.main.main_fragment.*

class StaticPicFragment : Fragment() {

    private val viewModel: CoroutineViewModel by lazy {
        CoroutineVMProvider.obtainMainViewModel(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subscribeToChanges()
        //viewModel.getTopDogList()
    }

    private fun subscribeToChanges() {

        viewModel.obRandomTwoDogs.observe(viewLifecycleOwner, Observer {
            when (it) {
                is GeneralResult.Progress -> {
                    showMessage("progress")
                }
                is GeneralResult.SuccessGeneric<*> -> {
                    updateTopTwoDogs(it.data as List<Dog>)
                }
                is GeneralResult.Error -> {
                    showMessage("error")
                }
            }
        })

        viewModel.obTopTwoDogs.observe(viewLifecycleOwner, {
            when (it) {
                is ResultOf.Progress -> showMessage(if(it.loading) "Loading" else "Stopped")
                is ResultOf.Success -> updateTopTwoDogs(it.value)
                is ResultOf.Empty -> showMessage(it.message)
                is ResultOf.Failure -> showMessage(it.message ?: "onFailure")
            }
        })
    }

    private fun showMessage(msg : String){
        Toast.makeText(requireContext(),msg, Toast.LENGTH_SHORT).show()
    }

    private fun updateTopTwoDogs(it: List<Dog>) {
        Log.d("MainFragment", "updateTopTwoDogs: it ${Gson().toJson(it)}")
        it.let { list ->
            list[0].let {
                dog_one.breed_name.text = it.breed
                it.imageUsl?.let { it1 -> ImageLoader.loadImage(requireContext(), it1, dog_one.episode_item_image) }
            }

            list[1].let {
                dog_two.breed_name.text = it.breed
                it.imageUsl?.let { it1 -> ImageLoader.loadImage(requireContext(), it1, dog_two.episode_item_image) }
            }
        }
    }

}