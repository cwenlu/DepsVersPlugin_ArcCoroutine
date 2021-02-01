package com.cwl.arccoroutine.test.paging.bean

import androidx.recyclerview.widget.DiffUtil

data class Person(val id: Int, val name: String, val updateTime: Long) {
    companion object {

        val diffCallback = object : DiffUtil.ItemCallback<Person>() {
            override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean =
                oldItem == newItem
        }
    }
}