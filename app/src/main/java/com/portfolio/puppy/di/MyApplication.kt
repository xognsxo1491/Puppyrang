package com.portfolio.puppy.di

import android.app.Application
import com.portfolio.puppy.data.board.BoardDataSource
import com.portfolio.puppy.data.board.BoardRepository
import com.portfolio.puppy.data.home.HomeDataSource
import com.portfolio.puppy.data.home.HomeRepository
import com.portfolio.puppy.data.main.MainDataSource
import com.portfolio.puppy.data.main.MainRepository
import com.portfolio.puppy.data.user.UserDataSource
import com.portfolio.puppy.data.user.UserRepository
import com.portfolio.puppy.ui.board.BoardViewModel
import com.portfolio.puppy.ui.board.BoardViewModelFactory
import com.portfolio.puppy.ui.home.HomeViewModel
import com.portfolio.puppy.ui.home.HomeViewModelFactory
import com.portfolio.puppy.ui.main.MainViewModel
import com.portfolio.puppy.ui.main.MainViewModelFactory
import com.portfolio.puppy.ui.user.UserViewModel
import com.portfolio.puppy.ui.user.UserViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

// 의존성 주입
class MyApplication: Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@MyApplication))

        bind() from singleton { MainDataSource() }
        bind() from singleton { MainRepository(instance(), instance()) }
        bind() from provider { MainViewModel(instance()) }
        bind() from provider { MainViewModelFactory(instance()) }

        bind() from singleton { HomeDataSource() }
        bind() from singleton { HomeRepository(instance(), instance()) }
        bind() from provider { HomeViewModel(instance()) }
        bind() from provider { HomeViewModelFactory(instance()) }

        bind() from singleton { UserDataSource() }
        bind() from singleton { UserRepository(instance(), instance()) }
        bind() from provider { UserViewModel(instance()) }
        bind() from provider { UserViewModelFactory(instance()) }

        bind() from singleton { BoardDataSource() }
        bind() from singleton { BoardRepository(instance(), instance()) }
        bind() from provider { BoardViewModel(instance()) }
        bind() from provider { BoardViewModelFactory(instance()) }
    }
}