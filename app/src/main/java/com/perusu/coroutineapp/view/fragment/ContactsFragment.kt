package com.perusu.coroutineapp.view.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.perusu.coroutineapp.R
import com.perusu.coroutineapp.view.CoroutineViewModel
import com.perusu.coroutineapp.view.adapter.ContactsAdapter
import com.perusu.coroutineapp.vm.CoroutineVMProvider
import kotlinx.android.synthetic.main.fragment_contacts.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch


class ContactsFragment : Fragment() {

    private val CONTACTS_READ_REQ_CODE = 100

    private val viewModel: CoroutineViewModel by lazy {
        CoroutineVMProvider.obtainMainViewModel(this)
    }

    private val contactsAdapter by lazy {
        ContactsAdapter(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contacts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        intView()
        checkPermission()
    }

    private fun checkPermission() {
        if (requireContext().hasPermission(Manifest.permission.READ_CONTACTS))
            obs()
        else
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), CONTACTS_READ_REQ_CODE)
    }

    private fun intView() {
        recyclerContacts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = contactsAdapter
        }
    }

    private fun obs() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.fetchContacts()
                .onCompletion {
                    showMsg()
                }
                .collect {
                contactsAdapter.contactModel = it!!
                Log.d("Contacts", "obs: ${Gson().toJson(it)}")
            }
        }
    }

    private fun showMsg() {
        Toast.makeText(requireContext(), "Total contacts ${contactsAdapter.contactList.size}", Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CONTACTS_READ_REQ_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            obs()
        }
    }

}

fun Context.hasPermission(permission: String): Boolean {

    return ActivityCompat.checkSelfPermission(this, permission) ==
            PackageManager.PERMISSION_GRANTED
}
