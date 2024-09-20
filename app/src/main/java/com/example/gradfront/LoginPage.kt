package com.example.gradfront

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.gradfront.data.KakaoLoginResponse
import com.example.gradfront.data.KakaoTokenRequest
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

        call.enqueue(object : Callback<KakaoLoginResponse> {
            override fun onResponse(
                call: Call<KakaoLoginResponse>,
                response: Response<KakaoLoginResponse>
            ) {
                if (response.isSuccessful) {
                    val user = response.body()?.user
                    Log.d("LoginActivity", "로그인 성공: ${user?.email}")
                    // 로그인 성공 후 메인 액티비티로 이동
                    startActivity(Intent(this@LoginPage, MainActivity::class.java))
                    finish()
                } else {
                    Log.e("LoginActivity", "로그인 실패: 서버 응답 오류")
                }
            }

            override fun onFailure(call: Call<KakaoLoginResponse>, t: Throwable) {
                Log.e("LoginActivity", "로그인 실패: 네트워크 오류", t)
            }
        })
    }
}