package mission.vatsalya.repository

import mission.vatsalya.services.ServiceGenerator
import mission.vatsalya.services.ServiceGeneratorLogin
import retrofit2.Response
import mission.vatsalya.model.AppLoginResponse
import mission.vatsalya.model.ApplicationStatusRequest
import mission.vatsalya.model.ApplicationStatusResponse
import mission.vatsalya.model.LoginRequest
import mission.vatsalya.model.LoginResponse
import mission.vatsalya.model.MyAccountRequest
import mission.vatsalya.model.MyAccountResponse
import mission.vatsalya.model.OtpRequest
import mission.vatsalya.services.MyService
import javax.inject.Inject

class Repository @Inject constructor(api: MyService,
                                     apiLogin: MyService): BaseRepository(),RepositoryInt{


    init {
        mApi=api
        mApiLogin=apiLogin
    }

    override suspend fun getLogin(request: LoginRequest): Response<AppLoginResponse> {
        mApiLogin = ServiceGeneratorLogin.createServiceLogin(MyService::class.java)
        return mApiLogin.getLogin(request)
    }

    override suspend fun getMyAccount(request: MyAccountRequest): Response<MyAccountResponse> {
        mApi = ServiceGenerator.createService(MyService::class.java)
        return mApi.getMyAccount(request)
    }

    override suspend fun getOtpLogin(request: OtpRequest): Response<LoginResponse> {
        mApiLogin = ServiceGeneratorLogin.createServiceLogin(MyService::class.java)
        return mApiLogin.getOtpLogin(request)
    }

    override suspend fun getAppStatus(request: ApplicationStatusRequest): Response<ApplicationStatusResponse> {
        mApi = ServiceGenerator.createService(MyService::class.java)
        return mApi.getAppStatus(request)
    }
}


