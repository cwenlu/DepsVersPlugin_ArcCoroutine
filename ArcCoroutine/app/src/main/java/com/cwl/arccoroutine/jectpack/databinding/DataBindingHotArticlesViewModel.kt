package com.cwl.arccoroutine.jectpack.databinding

import androidx.lifecycle.*
import com.cwl.arccoroutine.wanandroid.data.repository.HotArticlesRepository
import com.cwl.arccoroutine.wanandroid.data.vo.hotarticles.HotArticlesVo
import kotlinx.coroutines.flow.collect

/**

 * @Author cwl

 * @Date 2021/4/28 14:14

 */
class DataBindingHotArticlesViewModel:ViewModel() {
    private val hotArticlesRepository= HotArticlesRepository()

    private val page=MutableLiveData(0)

    val hotArticlesList=page.switchMap {
        hotArticlesRepository.queryHotArticles(it).asLiveData()
    }

    //asLiveData 就是对liveData的封装
    val step1= liveData<List<HotArticlesVo>> {
        hotArticlesRepository.queryHotArticles(0).collect {
            emit(it)
        }

    }

    fun fetch(refresh:Boolean=true){
        page.value=if(refresh) 0 else page.value!!+1
    }

}
