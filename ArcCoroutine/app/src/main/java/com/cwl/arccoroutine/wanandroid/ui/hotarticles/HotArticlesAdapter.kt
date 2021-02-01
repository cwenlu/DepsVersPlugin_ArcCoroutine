package com.cwl.arccoroutine.wanandroid.ui.hotarticles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cwl.arccoroutine.test.paging.ui.PersonAdapter
import com.cwl.arccoroutine.wanandroid.data.vo.hotarticles.HotArticlesVo

/**

 * @Author cwl

 * @Date 2021/1/8 11:25

 */
class HotArticlesAdapter:PagingDataAdapter<HotArticlesVo,HotArticlesAdapter.ViewHolder>(diffItemCallback) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName.setText(getItem(position)?.title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflateView(parent, android.R.layout.simple_list_item_1)
        return HotArticlesAdapter.ViewHolder(view)
    }

    private fun inflateView(viewGroup: ViewGroup, @LayoutRes viewType: Int): View {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        return layoutInflater.inflate(viewType, viewGroup, false)
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val tvName=view.findViewById<TextView>(android.R.id.text1)
    }

    companion object {
        val diffItemCallback= object : DiffUtil.ItemCallback<HotArticlesVo>() {
            override fun areItemsTheSame(oldItem: HotArticlesVo, newItem: HotArticlesVo): Boolean {
                return oldItem==newItem
            }

            override fun areContentsTheSame(oldItem: HotArticlesVo, newItem: HotArticlesVo): Boolean {
                return oldItem.title==newItem.title
            }

        }
    }
}