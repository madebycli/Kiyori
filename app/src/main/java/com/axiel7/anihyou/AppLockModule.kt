package com.axiel7.anihyou

import com.axiel7.anihyou.ui.screens.main.AppLockRuntime
import org.koin.dsl.module

val appLockModule = module {
    single { AppLockRuntime() }
}
