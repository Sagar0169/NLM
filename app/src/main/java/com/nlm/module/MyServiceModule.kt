package com.nlm.module//package com.nlm.module
//
//
//import android.content.Context
//
//import dagger.Provides
//import dagger.Module
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import com.nlm.repository.Repository
//import com.nlm.services.MyService
//import com.nlm.services.ServiceGenerator
//import com.nlm.utilities.Nlm
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//class MyServiceModule {
//    @Provides
//    @Singleton
//    fun provideMyService(): MyService {
//        return ServiceGenerator.createService(MyService::class.java)
//    }
//
//    @Provides
//    @Singleton
//    fun provideRepository(api: MyService,
//                          apiLogin: MyService
//    ): Repository {
//        return com.nlm.repository.Repository(api, apiLogin)
//    }
//
//    @Provides
//    @Singleton
//    fun provideAppContext(context:Context): Nlm {
//        return context as Nlm
//    }
//
//
//}