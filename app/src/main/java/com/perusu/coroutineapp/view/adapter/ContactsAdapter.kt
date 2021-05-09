package com.perusu.coroutineapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.perusu.coroutineapp.R
import com.perusu.coroutineapp.data.model.ContactModel
import kotlinx.android.synthetic.main.row_contact.view.*
import kotlinx.android.synthetic.main.row_contact_data.view.*


class ContactsAdapter(context: Context) : RecyclerView.Adapter<ContactsAdapter.VH>() {

    val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    var contactList = ArrayList<ContactModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var contactModel = ContactModel()
        set(value) {
            field = value
            contactList.add(value)
            notifyItemInserted(contactList.size - 1)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.row_contact, parent, false))
    }

    override fun getItemCount() = contactList.size

    override fun onBindViewHolder(holder: VH, position: Int) =
        holder.bind(contactList[position])


    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(contactModel: ContactModel?) {
            contactModel?.run {
                itemView.tvContactName.text = name
                itemView.llContactDetails.removeAllViews()
                phone?.forEach {
                    val detail =
                        layoutInflater.inflate(
                            R.layout.row_contact_data,
                            itemView.llContactDetails,
                            false
                        )
                    detail.imgIcon.setImageResource(com.perusu.coroutineapp.R.drawable.ic_local_phone_black_24dp)
                    detail.tvContactData.text = it
                    itemView.llContactDetails.addView(detail)
                }
                emails?.forEach {
                    val detail =
                        layoutInflater.inflate(
                            R.layout.row_contact_data,
                            itemView.llContactDetails,
                            false
                        )
                    detail.imgIcon.setImageResource(com.perusu.coroutineapp.R.drawable.ic_mail_black_24dp)
                    detail.tvContactData.text = it
                    itemView.llContactDetails.addView(detail)
                }
            }
        }
    }
}