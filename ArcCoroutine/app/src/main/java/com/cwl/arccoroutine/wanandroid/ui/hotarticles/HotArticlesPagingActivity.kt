package com.cwl.arccoroutine.wanandroid.ui.hotarticles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.observe
import androidx.paging.LoadState
import by.kirich1409.viewbindingdelegate.viewBinding
import com.cwl.arccoroutine.R
import com.cwl.arccoroutine.databinding.ActivityHotArticlesBinding
import com.cwl.arccoroutine.jectpack.paging.ui.PersonLoadStateAdapter
import com.cwl.common.widget.recyclerview.setupLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel

class HotArticlesPagingActivity : AppCompatActivity() {
    private val viewBinding by viewBinding(ActivityHotArticlesBinding::bind,R.id.root)
    private val hotArticlesViewModel by viewModel<HotArticlesViewModel>()
    private val adapter by lazy { HotArticlesAdapter()}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hot_articles)

        with(viewBinding){
            rvList.setupLayoutManager()
            rvList.adapter=adapter
            //对应header的设置是表示向前的loading
            rvList.adapter=adapter.withLoadStateFooter(PersonLoadStateAdapter())

            adapter.addLoadStateListener {
                refreshLayout.isRefreshing=it.refresh is LoadState.Loading
            }
        }

        hotArticlesViewModel.queryHotArticlesPaging().observe(this@HotArticlesPagingActivity){
            adapter.submitData(lifecycle,it)
        }

    }

}