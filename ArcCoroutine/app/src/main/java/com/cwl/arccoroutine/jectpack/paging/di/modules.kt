package com.cwl.arccoroutine.jectpack.paging.di

import com.cwl.arccoroutine.jectpack.paging.data.source.local.AppDatabase
import com.cwl.arccoroutine.jectpack.paging.data.source.PersonRepository
import com.cwl.arccoroutine.jectpack.paging.data.source.local.PersonLocalDataSource
import com.cwl.arccoroutine.jectpack.paging.data.source.remote.PersonRemoteDataSource
import com.cwl.arccoroutine.jectpack.paging.ui.PagingViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**

 * @Author cwl

 * @Date 2020/12/25 15:41

 */

val dbModuleTest= module {
    single { AppDatabase.instance(androidApplication()) }
}

val dataSourceModuleTest=module{
    factory<PersonLocalDataSource> { PersonLocalDataSource(get()) }
    factory<PersonRemoteDataSource> { PersonRemoteDataSource() }
}

val repositoryModuleTest= module {
    factory { PersonRepository(get(),get())  }
}

val viewModuleTest= module {
    viewModel { PagingViewModel(get()) }

}

val appModuleTest=listOf(dbModuleTest,dataSourceModuleTest,repositoryModuleTest,viewModuleTest)