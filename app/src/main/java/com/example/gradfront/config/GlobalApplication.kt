package com.example.gradfront.config

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility

class GlobalApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        // Kakao Sdk 초기화
        KakaoSdk.init(this, "6aca60cf485fcdb138a0802846166ae7")

        val keyHash = Utility.getKeyHash(this);
        Log.d(TAG, keyHash)
    }
}