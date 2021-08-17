package com.cwl.arccoroutine.jectpack.databinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.cwl.arccoroutine.R
import com.cwl.arccoroutine.databinding.ItemHotArticlesBindingBinding
import com.cwl.arccoroutine.wanandroid.data.vo.hotarticles.HotArticlesVo

/**

 * @Author cwl

 * @Date 2021/4/29 14:28

 */
class BindingHotArticlesAdapter(var data:List<HotArticlesVo> = emptyList()) :RecyclerView.Adapter<BindingHotArticlesAdapter.ViewHolder>(){
    class ViewHolder(var databinding: ItemHotArticlesBindingBinding):RecyclerView.ViewHolder(databinding.root)
    var vhKey=1000
    //避免在onCreateViewHolder创建多个对象,自己定义了key防止与一些其他图片库使用tag冲突
    //onBindViewHolder 中 position不可靠(不是notifyDataSetChanged全更新可能对象没变位置变了),反复设置点击事件也不好
    var onClickListener: View.OnClickListener = View.OnClickListener {
        var holder = it.getTag(vhKey) as BindingHotArticlesAdapter.ViewHolder
        //如果你没有使用MergeAdapter，那么getBindingAdapterPosition()和getAbsoluteAdapterPosition()方法的效果是一模一样的。
        //如果你使用了MergeAdapter，getBindingAdapterPosition()得到的是元素位于当前绑定Adapter的位置，而getAbsoluteAdapterPosition()方法得到的是元素位于合并后Adapter的绝对位置
        onItemClickListener?.onItemClick(holder,holder.bindingAdapterPosition)
    }

     interface OnItemClickListener {
        fun onItemClick(holder: ViewHolder,position: Int)
    }
    var onItemClickListener:OnItemClickListener?=null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding= DataBindingUtil.inflate<ItemHotArticlesBindingBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_hot_articles_binding,
            parent,
            false
        )
        var viewHolder = ViewHolder(binding)

        binding.root.setTag(vhKey,viewHolder)
        binding.root.setOnClickListener(onClickListener)
        return viewHolder
    }

    override fun getItemCount(): Int =data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databinding.item=data[position]
        holder.databinding.executePendingBindings()
    }
}


