package com.cwl.arccoroutine.test.databinding

import androidx.lifecycle.*
import com.cwl.arccoroutine.wanandroid.data.repository.HotArticlesRepository
import com.cwl.arccoroutine.wanandroid.data.vo.hotarticles.HotArticlesVo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

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

//    public val hotArticlesList:LiveData<List<HotArticlesVo>> = MutableLiveData<List<HotArticlesVo>>()


    fun fetch(refresh:Boolean=true){
        page.value=if(refresh) 0 else page.value!!+1
    }

}
