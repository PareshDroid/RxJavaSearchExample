package com.example.rxjavasearchexample

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val subscriptions = CompositeDisposable()

    lateinit var newsRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        newsRecyclerView = findViewById(R.id.news_recyclerView)

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView

        //on typing the keywords in the search view of toolbar the keywords are passed to the api call
        // and the result is brought back to the main thread

        subscriptions.addAll(Observable.create(ObservableOnSubscribe<String> { subscriber ->
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    subscriber.onNext(newText!!)
                    return false
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    subscriber.onNext(query!!)
                    return false
                }
            })
        })
            .map { text -> text.toLowerCase().trim() }
            .debounce(500, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .filter { text -> text.isNotBlank() }
            .switchMapSingle {
                    result -> NetworkCall(result)  //making network call to the server

            }
            .subscribe { newsData ->

                //actual results of the search are fetched here
                displayNewsData(newsData)
            })
        return true
    }


    override fun onDestroy() {
        super.onDestroy()
        subscriptions.dispose()
    }


    fun NetworkCall(query:String): Single<NewsModel.Result> {

        return ApiService.create().getTopNews(
            query, Constants.API_KEY
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun displayNewsData(newsData:NewsModel.Result){

        val newsModel = newsData.response
        val newsList = newsModel.docs
        val mListAdapter = NewsListAdapter(newsList)
        val mLayoutManager = LinearLayoutManager(this)
        newsRecyclerView.setLayoutManager(mLayoutManager)
        newsRecyclerView.setItemAnimator(DefaultItemAnimator())
        newsRecyclerView.setAdapter(mListAdapter)
    }
}
