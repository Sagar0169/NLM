package com.nlm.repository

import com.nlm.services.MyService
import javax.inject.Inject

open class BaseRepository {

    @Inject
    lateinit var mApi: MyService

    @Inject
    lateinit var mApiLogin: MyService

}