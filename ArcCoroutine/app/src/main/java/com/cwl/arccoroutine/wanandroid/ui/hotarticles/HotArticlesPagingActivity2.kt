package com.cwl.arccoroutine.wanandroid.ui.hotarticles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.paging.LoadState
import androidx.recyclerview.widget.DiffUtil
import by.kirich1409.viewbindingdelegate.viewBinding
import com.cwl.arccoroutine.R
import com.cwl.arccoroutine.databinding.ActivityHotArticlesBinding
import com.cwl.arccoroutine.databinding.ItemHotArticlesBinding
import com.cwl.arccoroutine.test.paging.ui.PersonLoadStateAdapter
import com.cwl.arccoroutine.wanandroid.data.dto.hotarticles.HotArticlesDto
import com.cwl.arccoroutine.wanandroid.data.vo.hotarticles.HotArticlesVo
import com.cwl.common.widget.recyclerview.multitype2.QuickAdapter2
import com.cwl.common.widget.recyclerview.paging.QuickPagingAdapter
import com.cwl.common.widget.recyclerview.setupLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel

class HotArticlesPagingActivity2 : AppCompatActivity() {
    private val viewBinding by viewBinding(ActivityHotArticlesBinding::bind,R.id.root)
    private val hotArticlesViewModel by viewModel<HotArticlesViewModel>()
    private val adapter by lazy {
        QuickPagingAdapter<ItemHotArticlesBinding,HotArticlesVo>(ItemHotArticlesBinding::inflate, object : DiffUtil.ItemCallback<HotArticlesVo>() {
            override fun areItemsTheSame(oldItem: HotArticlesVo, newItem: HotArticlesVo): Boolean {
               return oldItem==newItem
            }

            override fun areContentsTheSame(
                oldItem: HotArticlesVo,
                newItem: HotArticlesVo
            ): Boolean {
                return oldItem.title==newItem.title
            }

        })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hot_articles)

        with(viewBinding){
            rvList.setupLayoutManager()
            rvList.adapter=adapter
            adapter.register{commonViewHolder2, data, i ->
                (commonViewHolder2.viewBinding).run {
                    tvTitle.text=if(data==null) "placeholder" else  data.title
                }
            }
            //对应header的设置是表示向前的loading
            rvList.adapter=adapter.withLoadStateFooter(PersonLoadStateAdapter())

            adapter.addLoadStateListener {
                refreshLayout.isRefreshing=it.refresh is LoadState.Loading
            }
        }

        hotArticlesViewModel.queryHotArticlesPaging().observe(this@HotArticlesPagingActivity2){
            adapter.submitData(lifecycle,it)
        }

    }

}