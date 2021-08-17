package com.cwl.arccoroutine.jectpack.paging.data.repository

import androidx.paging.PagingData
import com.cwl.arccoroutine.jectpack.paging.bean.Person
import kotlinx.coroutines.flow.Flow

interface PersonDataSource {

    fun postOfData(): Flow<PagingData<Person>>

    fun remove(person: Person)

    fun insert(person: Person)
}

