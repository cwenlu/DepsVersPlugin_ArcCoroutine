package com.cwl.arccoroutine.jectpack.paging.data.source.remote

import androidx.paging.*
import com.cwl.arccoroutine.jectpack.paging.bean.Person
import com.cwl.arccoroutine.jectpack.paging.data.repository.PersonDataSource
import com.cwl.arccoroutine.jectpack.paging.data.source.local.defaultPagingConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PersonRemoteDataSource(
    val pageConfig: PagingConfig=defaultPagingConfig,
    ):PersonDataSource {


     override fun postOfData(): Flow<PagingData<Person>> {
         return Pager(pageConfig) {
             PersonPagingSource()
         }.flow.map { pagingData ->
             // 数据映射，数据库实体 PersonEntity ——>  上层用到的实体 Person
             pagingData.map {
                 Person(it.id,it.name,it.updateTime)
             }
         }
    }

    override fun remove(person: Person) {

    }

    override fun insert(person: Person) {

    }

}

class PersonPagingSource : PagingSource<Int,Person>(){
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Person> {
        return try {
            //模拟了下实际不能用
            val page = params.key?:0
            //获取网络数据
            val result =null
            LoadResult.Page(
                //需要加载的数据
                data = /*result.data.datas*/arrayListOf(),
                //如果可以往上加载更多就设置该参数，否则不设置
                prevKey = null,
                //加载下一页的key 如果传null就说明到底了
                nextKey = /*if(result.data.curPage==result.data.pageCount) null else page+1*/0
            )
        }catch (e:Exception){
            LoadResult.Error(e)
        }

    }

}