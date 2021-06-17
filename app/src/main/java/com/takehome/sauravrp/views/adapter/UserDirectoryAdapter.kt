package com.takehome.sauravrp.views.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.takehome.sauravrp.R
import com.takehome.sauravrp.databinding.UserSummaryItemViewBinding
import com.takehome.sauravrp.helpers.UserItemResultDiffCallback
import com.takehome.sauravrp.viewmodels.User
import com.takehome.sauravrp.views.adapter.UserDirectoryAdapter.UserViewHolder

class UserDirectoryAdapter(private val employeeSelectionListener: UserSelectionListener) :
    ListAdapter<User, UserViewHolder>(UserItemResultDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemBinding = UserSummaryItemViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UserViewHolder(itemBinding).apply {
            itemBinding.root.setOnClickListener(this)
        }
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class UserViewHolder(private val binding: UserSummaryItemViewBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        fun bind(item: User) {
            with(binding) {
                root.setOnClickListener(this@UserViewHolder)

                name.text = item.userName
                name.isVisible = item.userName.isNotBlank()

                typeValue.text = item.type
                item.type.isNotBlank().let {
                    typeValue.isVisible = it
                    typeValue.isVisible = it
                }
            }

            if(item.photoUrl.isNotBlank()) {
                binding.avatar.isVisible = true
                Picasso
                    .get()
                    .load(item.photoUrl)
                    .error(R.drawable.no_image_found)
                    .placeholder(R.drawable.image_loading)
                    .into(binding.avatar)
            } else {
                binding.avatar.isVisible = false
            }
        }

        override fun onClick(v: View?) {
            employeeSelectionListener.cardItemSelected(currentList[adapterPosition])
        }
    }

    interface UserSelectionListener {
        fun cardItemSelected(user: User)
    }
}