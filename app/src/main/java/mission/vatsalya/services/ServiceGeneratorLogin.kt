package mission.vatsalya.services

import android.util.Log
import mission.vatsalya.utilities.AppConstants.BASE_URL
import mission.vatsalya.utilities.Vatsalya
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object ServiceGeneratorLogin {
    // This is the base Url of the application.
    private val httpClient = OkHttpClient.Builder()
    private val builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(buildClient())
        .addConverterFactory(GsonConverterFactory.create())

    private var retrofit: Retrofit? = null

    fun <S> createServiceLogin(serviceClass: Class<S>): S {
        val authToken = Vatsalya.getToken()
        Log.e("token", "c $authToken")

        if (!checkEmptyString(authToken)) {
            val interceptor = AuthenticationInterceptor(authToken)
            val logInterceptor = HttpLoggingInterceptor()
            logInterceptor.level = HttpLoggingInterceptor.Level.BODY
            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(MyOkHttpInterceptor(authToken))
                httpClient.addInterceptor(logInterceptor)
                builder.client(httpClient.build())
                retrofit = builder.build() } }
        return retrofit!!.create(serviceClass) }


    private fun buildClient(): OkHttpClient {
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(logInterceptor)
            .readTimeout(180, TimeUnit.SECONDS)
            .connectTimeout(180, TimeUnit.SECONDS)
            .writeTimeout(180, TimeUnit.SECONDS)
            .build()
    }

    class MyOkHttpInterceptor internal constructor(private val tokenServer: String) : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            Log.e("tokenServer" , "aaa $tokenServer")
            val token = tokenServer// get token logic
                val newRequest = originalRequest.newBuilder()
//                    .header("Authorization", token)
//                    .header("Accept", "application/json")
                    .build()
               return chain.proceed(newRequest)
        }
    }

    private fun checkEmptyString(string: String?): Boolean {
        return string == null || string.trim { it <= ' ' }.isEmpty()
    }
}