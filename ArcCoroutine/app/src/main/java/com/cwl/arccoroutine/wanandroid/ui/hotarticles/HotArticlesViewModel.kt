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
    private val page=MutableLiveData(0)

    fun queryHotArticles():LiveData<List<HotArticlesVo>> = page.switchMap {
        hotArticlesRepository.queryHotArticles(it)
            .onCompletion { loading.value=false }
            .asLiveData(viewModelScope.coroutineContext)
    }

    fun fetch(refresh:Boolean=true){
        page.value=if(refresh) 0 else page.value!!+1
    }

    fun queryHotArticlesPaging()=hotArticlesRepository.queryHotArticlesPaging()
        .cachedIn(viewModelScope)
        .asLiveData(viewModelScope.coroutineContext)/*不指定CoroutineContext也可以，内部会设置main*/

}