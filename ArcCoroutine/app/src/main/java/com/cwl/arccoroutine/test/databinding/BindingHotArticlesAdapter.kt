package com.cwl.arccoroutine.test.databinding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.cwl.arccoroutine.R
import com.cwl.arccoroutine.databinding.ItemHotArticlesBindingBinding
import com.cwl.arccoroutine.databinding.ItemPageBinding
import com.cwl.arccoroutine.wanandroid.data.vo.hotarticles.HotArticlesVo

/**

 * @Author cwl

 * @Date 2021/4/29 14:28

 */
class BindingHotArticlesAdapter(var data:List<HotArticlesVo> = emptyList()) :RecyclerView.Adapter<BindingHotArticlesAdapter.ViewHolder>(){
    class ViewHolder(var databinding: ItemHotArticlesBindingBinding):RecyclerView.ViewHolder(databinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding= DataBindingUtil.inflate<ItemHotArticlesBindingBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_hot_articles_binding,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int =data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databinding.item=data[position]
    }
}


