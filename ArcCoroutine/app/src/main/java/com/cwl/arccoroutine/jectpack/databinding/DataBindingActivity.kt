package com.cwl.arccoroutine.jectpack.databinding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.lifecycle.whenStarted
import com.cwl.arccoroutine.R
import com.cwl.arccoroutine.databinding.ActivityDataBindingBinding
import com.cwl.arccoroutine.wanandroid.data.vo.hotarticles.HotArticlesVo
import com.cwl.common.widget.recyclerview.setupLayoutManager


class DataBindingActivity : AppCompatActivity(), ActionListener {
    lateinit var binding: ActivityDataBindingBinding
    val viewModel by viewModels<DataBindingHotArticlesViewModel> { getViewModelFactory() }

    val viewModel2 by viewModels<DataBindingHotArticlesViewModel> ()//也可以不指定factory
    private val hotArticlesList = arrayListOf<HotArticlesVo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityDataBindingBinding>(
            this,
            R.layout.activity_data_binding
        ).apply {
            action = this@DataBindingActivity
            lifecycleOwner = this@DataBindingActivity
            vm = viewModel
            viewModel.hotArticlesList.observe(this@DataBindingActivity){
                hotArticlesList.addAll(it)
                rvList.adapter?.notifyDataSetChanged()
            }
            rvList.setupLayoutManager()
            rvList.adapter = BindingHotArticlesAdapter(hotArticlesList)
            viewModel.fetch()
        }

        lifecycleScope.launchWhenStarted {
            //在与之对应的生命周期会暂停 eg:onStop
        }
//        binding.vm=ViewModelProvider(this).get(ViewModelWithLiveData::class.java)
    }

    override fun onClick(view: View?) {


    }
}