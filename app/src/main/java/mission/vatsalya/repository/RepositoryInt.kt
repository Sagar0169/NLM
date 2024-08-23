package mission.vatsalya.repository

import mission.vatsalya.model.AppLoginResponse
import mission.vatsalya.model.ApplicationStatusRequest
import mission.vatsalya.model.ApplicationStatusResponse
import mission.vatsalya.model.LoginRequest
import mission.vatsalya.model.LoginResponse
import mission.vatsalya.model.MyAccountRequest
import mission.vatsalya.model.MyAccountResponse
import mission.vatsalya.model.OtpRequest

import retrofit2.Response

interface RepositoryInt {

    suspend fun getLogin(request: LoginRequest): Response<AppLoginResponse>

    suspend fun getMyAccount(request: MyAccountRequest): Response<MyAccountResponse>

    suspend fun getOtpLogin(request: OtpRequest): Response<LoginResponse>

    suspend fun getAppStatus(request: ApplicationStatusRequest): Response<ApplicationStatusResponse>

}