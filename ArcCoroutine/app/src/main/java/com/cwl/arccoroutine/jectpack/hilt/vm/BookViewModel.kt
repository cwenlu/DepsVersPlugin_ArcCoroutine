package com.cwl.arccoroutine.jectpack.hilt.vm

import androidx.lifecycle.ViewModel
import com.cwl.arccoroutine.jectpack.hilt.bean.IBookGenerate
import com.cwl.arccoroutine.jectpack.hilt.data.BookRepository
import com.cwl.arccoroutine.jectpack.hilt.data.SingletionBookRepository
import com.cwl.arccoroutine.jectpack.hilt.di.QualiferBook2
import com.cwl.arccoroutine.jectpack.hilt.di.QualiferBook3
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

/**
 * @Author cwl
 * @Date 2021/8/21 11:25
 * @Description
 */
@HiltViewModel
class BookViewModel @Inject constructor(private val bookRepository: BookRepository):ViewModel() {
    @Inject
    @QualiferBook2
    lateinit var iBookGenerate: IBookGenerate

    @Inject
    @QualiferBook3
    lateinit var iBookGenerate2: IBookGenerate

    init {
        bookRepository.generateBooks().forEach {
            Timber.i(it.toString())
        }

    }
}

@HiltViewModel
class SingletonBookViewModel @Inject constructor(private val bookRepository: SingletionBookRepository):ViewModel()