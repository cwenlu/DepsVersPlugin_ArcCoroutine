package com.cwl.arccoroutine.test.paging.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import by.kirich1409.viewbindingdelegate.viewBinding
import com.cwl.arccoroutine.R
import com.cwl.arccoroutine.databinding.ActivityPagingBinding
import com.cwl.common.widget.recyclerview.setupLayoutManager
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class PagingActivity : AppCompatActivity(R.layout.activity_paging) {

    private val vb by viewBinding(ActivityPagingBinding::bind,R.id.root)
    private val pagingViewModel by viewModel<PagingViewModel>()
    //or  private val pagingViewModel:PagingViewModel by viewModel()

    private val mAdapter by lazy { PersonAdapter()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(vb){
            rvList.setupLayoutManager()
            rvList.adapter=mAdapter.withLoadStateFooter(PersonLoadStateAdapter())

            refreshLayout.setOnRefreshListener {
                mAdapter.refresh()
            }
            lifecycleScope.launchWhenCreated {
                mAdapter.loadStateFlow.collectLatest {
                    refreshLayout.isRefreshing=it.refresh is LoadState.Loading
                }
            }
        }
        pagingViewModel.pageDataLiveData3.observe(this){
            mAdapter.submitData(lifecycle,it)
//            lifecycleScope.launchWhenCreated {
//                mAdapter.submitData(it)
//            }
        }
    }
}