package com.cwl.arccoroutine.test.databinding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.cwl.arccoroutine.R
import com.cwl.arccoroutine.databinding.ActivityDataBindingBinding
import com.cwl.arccoroutine.wanandroid.data.vo.hotarticles.HotArticlesVo
import com.cwl.common.widget.recyclerview.setupLayoutManager


class DataBindingActivity : AppCompatActivity(), ActionListener {
    lateinit var binding: ActivityDataBindingBinding
    val viewModel by viewModels<DataBindingHotArticlesViewModel> { getViewModelFactory() }
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


//        binding.vm=ViewModelProvider(this).get(ViewModelWithLiveData::class.java)
    }

    override fun onClick(view: View?) {


    }
}