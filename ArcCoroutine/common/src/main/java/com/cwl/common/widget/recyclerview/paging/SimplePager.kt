package com.cwl.common.widget.recyclerview.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import java.security.Key

/**

 * @Author cwl

 * @Date 2021/1/11 11:21

 */
class SimplePager<Key : Any, Value : Any>(
    private val pageSize:Int=20,
    //预加载距离,根据实际数据情况调整
    private val prefetchDistance: Int = pageSize/2,
    //是否开起占位
    private val enablePlaceholders: Boolean =false,
    //初始加载大小
    private val initialLoadSize: Int = pageSize,
    //控制存储数据大小，如果设置，必须处理prevKey,保证向前请求
    private val maxSize: Int = PagingConfig.MAX_SIZE_UNBOUNDED,
    private val jumpThreshold: Int = PagingSource.LoadResult.Page.COUNT_UNDEFINED,
    private val initialKey: Key? = null,
    private val loadData:suspend (PagingSource.LoadParams<Key>) -> PagingSource.LoadResult<Key, Value>
){
    fun getPagerDataFlow()=Pager(
        PagingConfig(
            pageSize,
            initialLoadSize = initialLoadSize,
            prefetchDistance = prefetchDistance,
            maxSize = maxSize,
            enablePlaceholders = enablePlaceholders
        ),
        initialKey=initialKey
    ){
        object : PagingSource<Key, Value>() {
            override suspend fun load(params: LoadParams<Key>): LoadResult<Key, Value> =loadData(params)
        }
    }.flow
}