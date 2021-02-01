package com.cwl.arccoroutine.wanandroid.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.recyclerview.widget.SortedList
import com.cwl.arccoroutine.wanandroid.data.dto.hotarticles.HotArticlesDto
import com.cwl.arccoroutine.wanandroid.data.mapper.HotArticlesMapper
import com.cwl.arccoroutine.wanandroid.data.mapper.HotArticlesMapperImpl
import com.cwl.arccoroutine.wanandroid.data.vo.hotarticles.HotArticlesVo
import com.cwl.common.okhttp.rb
import com.cwl.common.widget.recyclerview.paging.SimplePager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**

 * @Author cwl

 * @Date 2021/1/4 15:53

 */
class HotArticlesRepository {
    fun queryHotArticles(page:Int?=0):Flow<List<HotArticlesVo>> = flow {
        emit(HotArticlesMapperImpl().toHotArticlesVoList("article/list/${page}/json".rb().getAwait<HotArticlesDto>().data.datas))
    }.flowOn(Dispatchers.IO)

    fun queryHotArticlesPaging()=Pager(PagingConfig(
        pageSize = 30,
        prefetchDistance = 10,
        initialLoadSize = 60,
        //（一般射大点或不设置）注意这个值如果设置了，会控制存储的数据，那么prevKey必须处理，不然不能加载之前的数据(再请求)
        maxSize = 60
    )){
        HotArticlesPagingSource()

    }.flow/*如果前面没有进行数据转换可在这转换 map*/
    fun queryHotArticlesPaging2()=SimplePager<Int,HotArticlesVo>(
        pageSize = 30,
        enablePlaceholders = true,
        prefetchDistance = 10,
        initialLoadSize = 60,
        //（一般射大点或不设置）注意这个值如果设置了，会控制存储的数据，那么prevKey必须处理，不然不能加载之前的数据(再请求)
        maxSize = 60
    ){
        try{
            val page=it.key?:0
            val dto="article/list/${page}/json".rb().getAwait<HotArticlesDto>().data
            PagingSource.LoadResult.Page(
                data = HotArticlesMapperImpl().toHotArticlesVoList(dto.datas),
                prevKey = if(page==0) null else page-1,
                nextKey =if(dto.pageCount==dto.curPage) null else page+1,
                itemsAfter=dto.total-dto.curPage*dto.pageCount
            )
        }catch (e:Exception){
            PagingSource.LoadResult.Error(e)
        }
    }.getPagerDataFlow()

    private inner class HotArticlesPagingSource:PagingSource<Int,HotArticlesVo>(){
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HotArticlesVo> {
            return try{
                val page=params.key?:0
                val dto="article/list/${page}/json".rb().getAwait<HotArticlesDto>().data
                LoadResult.Page(
                    data = HotArticlesMapperImpl().toHotArticlesVoList(dto.datas),
                    prevKey = if(page==0) null else page-1,
                    nextKey =if(dto.pageCount==dto.curPage) null else page+1
                )
            }catch (e:Exception){
                LoadResult.Error(e)
            }
        }

    }
}