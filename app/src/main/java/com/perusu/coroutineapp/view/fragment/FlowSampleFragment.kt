package com.perusu.coroutineapp.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.perusu.coroutineapp.R
import com.perusu.coroutineapp.view.CoroutineViewModel
import com.perusu.coroutineapp.vm.CoroutineVMProvider
import kotlinx.android.synthetic.main.fragment_flow_sample.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class FlowSampleFragment : Fragment() {

    private val viewModel: CoroutineViewModel by lazy {
        CoroutineVMProvider.obtainMainViewModel(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_flow_sample, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClicks()
    }

    private fun initClicks() {
        btnObserve.setOnClickListener(onClick)
        btnCollect.setOnClickListener(onClick)
        btnNoLiveData.setOnClickListener(onClick)
        btnFlowMap.setOnClickListener(onClick)
    }

    private val onClick = View.OnClickListener {
        when (it.id) {
            R.id.btnObserve -> observeFlow()
            R.id.btnCollect -> collectFlow()
            R.id.btnNoLiveData -> flowWithoutLiveData()
            R.id.btnFlowMap -> flowWithMap()
        }
    }

    private fun flowWithoutLiveData() {
        viewModel.setMutableStateValue()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.mutableStateFlow.collect {
                tvText.text = it.toString()
            }
        }
    }

    private fun flowWithMap() {
        viewModel.setMutableStateValue()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.flowWithMap().collect {
                tvText.text = it.toString()
            }
        }
    }


    private fun observeFlow() {
        viewModel.flowWithBuilder.observe(viewLifecycleOwner, {
            tvText.text = it.breed
        })
    }

    private fun collectFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            sampleFlowData().collect {
                tvText.text = it.toString()
                Log.d("SampleFragment", "onViewCreated: $it")
            }
        }
    }

    private fun sampleFlowData(): Flow<Int> = flow {
        for (i in 1..5) {
            delay(1000)
            emit(i) // <-- emit is called here
        }
    }


    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


}