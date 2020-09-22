package com.perusu.coroutineapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.perusu.coroutineapp.R
import com.perusu.coroutineapp.data.model.ResultOf
import com.perusu.coroutineapp.view.CoroutineViewModel
import com.perusu.coroutineapp.view.adapter.RecyclerAdapter
import com.perusu.coroutineapp.vm.CoroutineVMProvider
import kotlinx.android.synthetic.main.fragment_recycler.*


class RecyclerFragment : Fragment() {

    private val viewModel: CoroutineViewModel by lazy {
        CoroutineVMProvider.obtainMainViewModel(this)
    }

    private val recyclerAdapter by lazy(LazyThreadSafetyMode.NONE) { RecyclerAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recycler, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAssets()
        subscribeToChanges()
        viewModel.getDogList()
    }

    private fun setAssets() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }
    }

    private fun subscribeToChanges() {
        viewModel.obDogList.observe(viewLifecycleOwner, {
            when (it) {
                is ResultOf.Progress -> if (it.loading) vsRoot.setParentVisible()
                is ResultOf.Success -> recyclerAdapter.submitList(it.value).also { vsRoot.setChildVisible() }
                is ResultOf.Empty -> showMessage(it.message)
                is ResultOf.Failure -> showMessage(it.message ?: "onFailure")
            }
        })
    }


    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}