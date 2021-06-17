package com.takehome.sauravrp.helpers

import androidx.recyclerview.widget.DiffUtil
import com.takehome.sauravrp.viewmodels.User

class UserItemResultDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: User, newItem: User) = oldItem == newItem
}