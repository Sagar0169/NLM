package mission.vatsalya.services

import mission.vatsalya.model.AppLoginResponse
import mission.vatsalya.model.ApplicationStatusRequest
import mission.vatsalya.model.LoginRequest
import mission.vatsalya.model.LoginResponse
import mission.vatsalya.model.MyAccountRequest
import mission.vatsalya.model.MyAccountResponse
import mission.vatsalya.model.ApplicationStatusResponse
import mission.vatsalya.model.OTPResponse
import mission.vatsalya.model.OtpRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

const val LOGIN = "applogin"
const val OTP_LOGIN = "otplogin"
const val MY_ACCOUNT = "myaccount"
const val APP_STATUS = "getAppapplicationstatus "



interface MyService {

    @POST(LOGIN)
    suspend fun getLogin(@Body request: LoginRequest): Response<AppLoginResponse>
    @POST(OTP_LOGIN)
    suspend fun getOtpLogin(@Body request: OtpRequest): Response<LoginResponse>
    @POST(MY_ACCOUNT)
    suspend fun getMyAccount(@Body request: MyAccountRequest): Response<MyAccountResponse>
    @POST(APP_STATUS)
    suspend fun getAppStatus(@Body request: ApplicationStatusRequest): Response<ApplicationStatusResponse>

}

