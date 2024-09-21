package com.example.gradfront

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKeys
import com.example.gradfront.data.KakaoTokenRequest
import com.example.gradfront.data.UserResponse
import com.example.gradfront.databinding.ActivityLoginPageBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginPage : AppCompatActivity() {

    val mCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(TAG, "로그인 실패1 $error")
        } else if (token != null) {
            Log.e(TAG, "로그인 성공1 ${token.accessToken}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this, MainActivity::class.java)

        binding.loginBtn.setOnClickListener {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                // 카카오톡 로그인
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    // 로그인 실패 부분
                    if (error != null) {
                        Log.e(TAG, "로그인 실패 $error")
                        // 사용자가 취소
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }
                        // 다른 오류
                        else {
                            UserApiClient.instance.loginWithKakaoAccount(
                                this,
                                callback = mCallback
                            ) // 카카오 이메일 로그인
                        }
                    }
                    // 로그인 성공 부분
                    else if (token != null) {
                        Log.e(TAG, "로그인 성공 ${token.accessToken}")
                        sendTokenToBackend(token.accessToken)  // 백엔드로 액세스 토큰 전송
                        startActivity(intent)
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = { token, error ->
                    if (error != null) {
                        Log.e(TAG, "이메일 로그인 실패: $error")
                    } else if (token != null) {
                        Log.e(TAG, "이메일 로그인 성공: ${token.accessToken}")
                        sendTokenToBackend(token.accessToken)  // 백엔드로 액세스 토큰 전송
                        startActivity(intent)  // 토큰 전송 후에 액티비티 전환
                    }
                })
            }
        }

    }

    // 여기에서 sendTokenToBackend 함수 호출
    private fun sendTokenToBackend(accessToken: String) {
        val apiService = ApiClient.getApiService()  // Retrofit API 클라이언트
        val call = apiService.sendKakaoAccessToken(KakaoTokenRequest(accessToken))

        call.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                if (response.isSuccessful) {
                    val user = response.body()
                    Log.d("LoginActivity", "로그인 성공: ${user?.id}")

                    // EncryptedSharedPreferences를 사용하여 사용자 ID 저장
                    saveUserId(user?.id ?: 0)

                    // 로그인 성공 후 메인 액티비티로 이동
                    startActivity(Intent(this@LoginPage, MainActivity::class.java))
                    finish()
                } else {
                    Log.e("LoginActivity", "로그인 실패: 서버 응답 오류")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e("LoginActivity", "로그인 실패: 네트워크 오류", t)
            }
        })
    }

    // 사용자 ID를 EncryptedSharedPreferences에 저장하는 함수
    private fun saveUserId(userId: Long) {
        // 암호화된 SharedPreferences 생성
        val masterKey = MasterKey.Builder(applicationContext)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)  // 최신 AES256_GCM 방식 사용
            .build()

        // 암호화된 SharedPreferences 생성
        val sharedPreferences = EncryptedSharedPreferences.create(
            applicationContext,
            "encrypted_prefs", // 저장될 파일 이름
            masterKey, // 마스터 키
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        // 사용자 ID 저장
        val editor = sharedPreferences.edit()
        editor.putLong("userId", userId)
        editor.apply()

        Log.d("LoginActivity", "사용자 ID 저장됨: $userId")
    }
}