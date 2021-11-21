package com.brian_sjtu.hw211107

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.brian_sjtu.hw211107.databinding.ActivityMainBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

abstract class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var youDaoAPI: YouDaoAPI? = null

    interface YouDaoAPI {
        @GET("translate?doctype=json")
        fun query(@Query("i") i: String): Call<ResponseBody>
    }

    inner class YouDaoCallback : Callback<ResponseBody> {
        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            binding.output.setText(
                if (response.isSuccessful) response.body().toString()
                else response.errorBody().toString()
            )
        }
        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            binding.output.setText(t.message)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        youDaoAPI = Retrofit.Builder()
            .baseUrl("fanyi.youdao.com")
            .build()
            .create(YouDaoAPI::class.java)
    }

    fun onClick(view: View) {
        try {youDaoAPI!!.query(binding.input.text.toString()).enqueue(YouDaoCallback())}
        catch (t: Throwable) {binding.output.setText(t.message)}
    }
}