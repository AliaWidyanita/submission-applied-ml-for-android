package com.dicoding.asclepius.view.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.remote.ArticlesItem
import com.dicoding.asclepius.data.remote.News
import com.dicoding.asclepius.data.remote.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel : ViewModel() {
    private val _listNews = MutableLiveData<List<ArticlesItem>?>()
    val listNews : MutableLiveData<List<ArticlesItem>?> = _listNews

    init {
        getListNews("cancer")
    }

    private fun getListNews(query: String){
        val client = ApiConfig.getApiService().getNews(query)
        client.enqueue(object : Callback<News> {
            override fun onResponse(
                call: Call<News>,
                response: Response<News>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listNews.value = responseBody.articles as List<ArticlesItem>?
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<News>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "NewsActivity"
    }
}