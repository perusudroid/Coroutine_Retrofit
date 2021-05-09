package com.perusu.coroutineapp.view.fragment

import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.perusu.coroutineapp.R
import com.perusu.coroutineapp.common.CoroutineApp
import com.perusu.coroutineapp.data.model.ResultOf
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
        subsObs()
    }

    private fun subsObs() {
        viewModel.obLogin.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ResultOf.Progress -> showMsg(it.loading.toString())
                is ResultOf.Success -> showMsg("Welcome".plus(it.value.access_token))
                is ResultOf.Failure -> showMsg(it.message.toString())
            }
        })
    }

    private fun showMsg(msg: String) {
        Log.e("FlowSample", "showMsg: $msg",)
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun initClicks() {
        btnObserve.setOnClickListener(onClick)
        btnCollect.setOnClickListener(onClick)
        btnNoLiveData.setOnClickListener(onClick)
        btnFlowMap.setOnClickListener(onClick)
        btnFlowTimer.setOnClickListener(onClick)
        btnObservableTimer.setOnClickListener(onClick)
        btnLoginSample.setOnClickListener(onClick)
    }

    private val onClick = View.OnClickListener { view ->
        when (view.id) {
            R.id.btnObserve -> observeFlow()
            R.id.btnCollect -> collectFlow()
            R.id.btnNoLiveData -> flowWithoutLiveData()
            R.id.btnFlowMap -> flowWithMap()
            R.id.btnFlowTimer -> flowTimer()
            R.id.btnObservableTimer -> observableTimer()
            R.id.btnLoginSample -> doSample()
        }
    }

    private fun doSample() {
        CoroutineApp.createEmailCursor("388").run {
            moveToFirst()
            while (!isAfterLast) {

                getString(getColumnIndex(ContactsContract.Data.DISPLAY_NAME_PRIMARY))

                parseEmailAndPhone(
                    this,
                    getColumnIndex(ContactsContract.Data.MIMETYPE),
                    getColumnIndex(ContactsContract.Data.DATA1)
                )
                moveToNext()
            }
        }
    }

    private fun parseEmailAndPhone(
        cursor: Cursor,
        mimeTypeColumnIndex: Int,
        dataColumnIndex: Int
    ) {

        var email : String?=null
        when (cursor.getString(mimeTypeColumnIndex)) {
            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE -> {
                if (!cursor.getString(dataColumnIndex).isNullOrEmpty()) {
                    email = cursor.getString(dataColumnIndex)
                }
                showMessage(email ?: "null")
            }
            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE -> {
                var phoneNumber = cursor.getString(dataColumnIndex)
                if (!phoneNumber.isNullOrEmpty()) {
                    phoneNumber = phoneNumber.replace("\\s+".toRegex(), "")
                }
            }
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
        viewModel.flowWithBuilder.observe(viewLifecycleOwner, Observer {
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

    private fun observableTimer() {
        viewModel.timer.observe(this, Observer {
            btnObservableTimer.text = it.toString()
        })
        viewModel.startObsTimer()
    }

    private fun flowTimer() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.startCoroutineTimer(1000, 2000).collect {
                btnFlowTimer.text = it.toString()
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