package com.cwl.arccoroutine.jectpack.hilt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.cwl.arccoroutine.R
import com.cwl.arccoroutine.jectpack.hilt.bean.Book
import com.cwl.arccoroutine.jectpack.hilt.bean.Book2
import com.cwl.arccoroutine.jectpack.hilt.bean.Book3
import com.cwl.arccoroutine.jectpack.hilt.bean.IBookGenerateT
import com.cwl.arccoroutine.jectpack.hilt.vm.BookViewModel
import com.cwl.arccoroutine.jectpack.hilt.vm.SingletonBookViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class HiltActivity : AppCompatActivity() {
    private val vm by viewModels<BookViewModel>()
    private val vm2 by viewModels<SingletonBookViewModel>()

    @Inject
    lateinit var book2: Book2

    @Inject
    lateinit var book3: Book3

    @Inject
    lateinit var iBookGenerateT: IBookGenerateT<Book>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hilt)
        vm.run {

            Timber.i(iBookGenerate.generateBook().toString())
            Timber.i(iBookGenerate2.generateBook().toString())
        }
        vm2
        Timber.i(book2.toString())
        Timber.i(book3.toString())
    }
}