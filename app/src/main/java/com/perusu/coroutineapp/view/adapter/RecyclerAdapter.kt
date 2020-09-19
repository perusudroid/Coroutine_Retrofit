package com.perusu.coroutineapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.perusu.coroutineapp.R
import com.perusu.coroutineapp.common.ImageLoader
import com.perusu.coroutineapp.data.model.Dog
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_doggo.view.*

class RecyclerAdapter : ListAdapter<Dog, RecyclerAdapter.UserDateViewHolder>(UserDataAdapterListDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserDateViewHolder =
        UserDateViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_doggo, parent, false))

    override fun onBindViewHolder(holder: UserDateViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class UserDataAdapterListDiff : DiffUtil.ItemCallback<Dog>() {

        override fun areItemsTheSame(oldItem: Dog, newItem: Dog): Boolean {
            return oldItem.breed == newItem.breed
        }

        override fun areContentsTheSame(oldItem: Dog, newItem: Dog): Boolean {
            return oldItem == newItem
        }

    }

    inner class UserDateViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(dog: Dog) {
            containerView.breed_name.text = dog.breed?.capitalize()
            dog.imageUsl?.let { it1 -> ImageLoader.loadImageWithCircularCrop(containerView.context, it1, containerView.episode_item_image) }
        }
    }
}