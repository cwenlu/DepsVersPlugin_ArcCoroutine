package com.cwl.arccoroutine.wanandroid.ui.hotarticles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import by.kirich1409.viewbindingdelegate.viewBinding
import com.cwl.arccoroutine.R
import com.cwl.arccoroutine.databinding.ActivityHotArticlesBinding
import com.cwl.arccoroutine.databinding.ItemHotArticlesBinding
import com.cwl.arccoroutine.wanandroid.data.dto.hotarticles.HotArticlesDto
import com.cwl.arccoroutine.wanandroid.data.vo.hotarticles.HotArticlesVo
import com.cwl.common.widget.recyclerview.multitype2.QuickAdapter2
import com.cwl.common.widget.recyclerview.setupLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel

class HotArticlesActivity : AppCompatActivity() {
    private val viewBinding by viewBinding(ActivityHotArticlesBinding::bind,R.id.root)
    private val hotArticlesViewModel by viewModel<HotArticlesViewModel>()
    private val hotArticlesList= arrayListOf<HotArticlesVo>()
    private val adapter by lazy { QuickAdapter2<ItemHotArticlesBinding,HotArticlesVo>(ItemHotArticlesBinding::inflate,hotArticlesList)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hot_articles)

        with(viewBinding){
            rvList.setupLayoutManager()
            adapter.register { commonViewHolder2, data, i ->
                (commonViewHolder2.viewBinding as ItemHotArticlesBinding).run {
                    tvTitle.text=data.title
                }
            }
            rvList.adapter=adapter

            hotArticlesViewModel.loading.observe(this@HotArticlesActivity){
                refreshLayout.isRefreshing=it
            }
        }

        hotArticlesViewModel.queryHotArticles().observe(this@HotArticlesActivity){
            hotArticlesList.addAll(it)
            adapter.notifyDataSetChanged()
        }
        hotArticlesViewModel.fetch()

    }

}