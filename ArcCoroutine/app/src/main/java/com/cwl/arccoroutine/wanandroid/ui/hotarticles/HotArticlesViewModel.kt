package com.cwl.arccoroutine.wanandroid.ui.hotarticles

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.cwl.arccoroutine.wanandroid.data.dto.hotarticles.HotArticlesDto
import com.cwl.arccoroutine.wanandroid.data.repository.HotArticlesRepository
import com.cwl.arccoroutine.wanandroid.data.vo.hotarticles.HotArticlesVo
import kotlinx.coroutines.flow.onCompletion

/**

 * @Author cwl

 * @Date 2021/1/4 16:43

 */
class HotArticlesViewModel(private val hotArticlesRepository: HotArticlesRepository):ViewModel() {
    val loading=MutableLiveData(true)
    var page=0
    fun queryHotArticles():LiveData<List<HotArticlesVo>> = hotArticlesRepository.queryHotArticles(page++)
        .onCompletion { loading.value=false }
        .asLiveData(viewModelScope.coroutineContext)

    fun queryHotArticlesPaging()=hotArticlesRepository.queryHotArticlesPaging()
        .cachedIn(viewModelScope)
        .asLiveData(viewModelScope.coroutineContext)

}