package com.cwl.arccoroutine.jectpack.paging.data.source

import androidx.paging.PagingData
import com.cwl.arccoroutine.jectpack.paging.bean.Person
import com.cwl.arccoroutine.jectpack.paging.data.source.local.PersonLocalDataSource
import com.cwl.arccoroutine.jectpack.paging.data.source.remote.PersonRemoteDataSource
import kotlinx.coroutines.flow.Flow

class PersonRepository(
    val personLocalDataSource: PersonLocalDataSource,
    val personRemoteDataSource: PersonRemoteDataSource
) {


    fun postOfData(): Flow<PagingData<Person>> {
       return personLocalDataSource.postOfData()
    }

    fun remove(person: Person) {
        personLocalDataSource.remove(person)
    }

    fun insert(person: Person) {
        personLocalDataSource.insert(person)
    }
}

