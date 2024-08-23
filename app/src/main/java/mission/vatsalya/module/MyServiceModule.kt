package mission.vatsalya.module


import android.content.Context

import dagger.Provides
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mission.vatsalya.repository.Repository
import mission.vatsalya.services.MyService
import mission.vatsalya.services.ServiceGenerator
import mission.vatsalya.utilities.Vatsalya
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MyServiceModule {
    @Provides
    @Singleton
    fun provideMyService(): MyService {
        return ServiceGenerator.createService(MyService::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(api: MyService,
                          apiLogin: MyService
    ): Repository {
        return Repository(api, apiLogin)
    }

    @Provides
    @Singleton
    fun provideAppContext(context:Context): Vatsalya {
        return context as Vatsalya
    }


}