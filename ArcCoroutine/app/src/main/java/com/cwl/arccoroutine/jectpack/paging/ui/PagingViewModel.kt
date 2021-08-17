package com.cwl.arccoroutine.jectpack.paging.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.paging.PagingData
import com.cwl.arccoroutine.jectpack.paging.bean.Person
import com.cwl.arccoroutine.jectpack.paging.data.source.PersonRepository

/**

 * @Author cwl

 * @Date 2020/12/25 17:00

 */
class PagingViewModel(val repository: PersonRepository):ViewModel() {
    val pageDataLiveData3: LiveData<PagingData<Person>> = repository.postOfData().asLiveData()

    fun remove(person: Person) {
        repository.remove(person)
    }

    fun insert(person: Person) {
        repository.insert(person)
    }
}