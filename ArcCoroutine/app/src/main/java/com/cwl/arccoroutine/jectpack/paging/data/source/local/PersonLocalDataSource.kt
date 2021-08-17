package com.cwl.arccoroutine.jectpack.paging.data.source.local

import androidx.paging.*
import com.cwl.arccoroutine.jectpack.paging.bean.Person
import com.cwl.arccoroutine.jectpack.paging.data.repository.PersonDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PersonLocalDataSource(
    val db: AppDatabase,
    val pageConfig: PagingConfig=defaultPagingConfig,
    ):PersonDataSource {

    private val mPersonDao by lazy { db.personDao() }

     override fun postOfData(): Flow<PagingData<Person>> {
        return Pager(pageConfig) {
            // 加载数据库的数据
            mPersonDao.queryAllData()
        }.flow.map { pagingData ->
            // 数据映射，数据库实体 PersonEntity ——>  上层用到的实体 Person
            pagingData.map {
                Person(it.id,it.name,it.updateTime)
            }
        }
    }

    override fun remove(person: Person) {
        // 数据映射， 上层用到的实体 Person ——> 数据库实体 PersonEntity
        mPersonDao.delete(PersonEntity(person.id,person.name,person.updateTime))
    }

    override fun insert(person: Person) {
        // 数据映射， 上层用到的实体 Person ——> 数据库实体 PersonEntity
        mPersonDao.delete(PersonEntity(person.id,person.name,person.updateTime))
    }

}

val defaultPagingConfig = PagingConfig(
    // 每页显示的数据的大小
    pageSize = 60,

    // 开启占位符
    enablePlaceholders = false,

    // 预刷新的距离，距离最后一个 item 多远时加载数据
    prefetchDistance = 3,

    /**
     * 初始化加载数量，默认为 pageSize * 3
     *
     * internal const val DEFAULT_INITIAL_PAGE_MULTIPLIER = 3
     * val initialLoadSize: Int = pageSize * DEFAULT_INITIAL_PAGE_MULTIPLIER
     */
    initialLoadSize = 60,

    /**
     * 一次应在内存中保存的最大数据
     * 这个数字将会触发，滑动加载更多的数据
     */
    maxSize = 200
)