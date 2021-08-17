package com.cwl.arccoroutine.jectpack.paging.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cwl.arccoroutine.databinding.ItemLoadStateBinding
import com.cwl.arccoroutine.jectpack.paging.bean.Person

class PersonAdapter : PagingDataAdapter<Person, PersonAdapter.ViewHolder>(Person.diffCallback) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName.setText(getItem(position)?.name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflateView(parent, android.R.layout.simple_list_item_1)
        return PersonAdapter.ViewHolder(view)
    }

    private fun inflateView(viewGroup: ViewGroup, @LayoutRes viewType: Int): View {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        return layoutInflater.inflate(viewType, viewGroup, false)
    }

    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val tvName=view.findViewById<TextView>(android.R.id.text1)
    }
}

// ViewBinding 写法
class PersonLoadStateAdapter:LoadStateAdapter<PersonLoadStateAdapter.ViewHolder>(){
//    class ViewHolder(view:View):RecyclerView.ViewHolder(view){
//
//    }

    class ViewHolder(val itemLoadStateBinding: ItemLoadStateBinding):RecyclerView.ViewHolder(itemLoadStateBinding.root){

    }


    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        with(holder.itemLoadStateBinding){
            tvState.text="sasas"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        return ViewHolder(ItemLoadStateBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }
}



