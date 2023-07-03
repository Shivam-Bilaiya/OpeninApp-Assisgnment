package com.alphacreators.myapplication

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    private val retrofit = RetrofitService.getRetrofitInstance()
    private lateinit var sharedPreferences: SharedPreferences
    private val communicationAPI = retrofit.create(CommunicationAPI::class.java)
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: CustomViewPager
    private lateinit var TodayClicks: TextView
    private lateinit var TopLocation: TextView
    private lateinit var TopSource: TextView
    private lateinit var graphView: GraphView
    private lateinit var greeting: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // include the tab layout
        val includeTabLayout = findViewById<ScrollView>(R.id.includeTabLayout)


        // storing api key in local storage

        sharedPreferences = getSharedPreferences("API_KEY", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(
            "key",
            "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MjU5MjcsImlhdCI6MTY3NDU1MDQ1MH0.dCkW0ox8tbjJA2GgUx2UEwNlbTZ7Rr38PVFJevYcXFI"
        )
        editor.apply()


        // finding id of all views

        greeting = findViewById(R.id.greeting)
        tabLayout = includeTabLayout.findViewById(R.id.tabLayout)
        viewPager = includeTabLayout.findViewById(R.id.viewPager)
        viewPager.setViewPagingEnabled(true)
        TodayClicks = findViewById(R.id.todayClicks)
        TopLocation = findViewById(R.id.topLocation)
        TopSource = findViewById(R.id.topSource)
        graphView = findViewById(R.id.graph)



        // Setting Adapter for ViewPager

        val adapter = ViewPagerLinkAdapter(supportFragmentManager)
        adapter.addFragment(TopLinksFragment(), "Top Links")
        adapter.addFragment(RecentLinksFragment(), "Recent Links")
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
        viewPager.isPagingEnabled = true


        // Greet User According to Time

        greeting.text = greetToUser()


        // Show Graph Data
        showGraph()

        // Fetch Top Links Data

        getTopLinksData()

        // Fetch Today Clicks , Top Location , Top Source
        getDataFromAPI()

        // Fetch Recent Links Data
        getRecentLinksData()


    }


    private fun greetToUser(): String {
        val calendar = Calendar.getInstance()
        val currentTime = calendar.time

        val sdf = SimpleDateFormat("HH", Locale.getDefault())

        return when (sdf.format(currentTime).toInt()) {
            in 0..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            else -> "Good Evening!"
        }
    }

    private fun showGraph() {
        val data = getGraphData()
        val series = LineGraphSeries(data)

        graphView.addSeries(series)
        graphView.viewport.isXAxisBoundsManual = true
        graphView.viewport.setMinX(data.first().x)
        graphView.viewport.setMaxX(data.last().x)
        graphView.viewport.isScalable = true

        graphView.gridLabelRenderer.labelFormatter = DateAsXAxisLabelFormatter(
            this,
            SimpleDateFormat("MMM", Locale.getDefault())
        )
        graphView.gridLabelRenderer.numHorizontalLabels = 4
    }

    private fun getGraphData(): Array<DataPoint> {
        return arrayOf(
            DataPoint(getDate("2023-05-31"), 1.0),
            DataPoint(getDate("2023-06-01"), 1.0),
            DataPoint(getDate("2023-06-02"), 5.0),
            DataPoint(getDate("2023-06-03"), 1.0),
            DataPoint(getDate("2023-06-04"), 2.0),
            DataPoint(getDate("2023-06-05"), 19.0),
            DataPoint(getDate("2023-06-06"), 0.0),
            DataPoint(getDate("2023-06-07"), 3.0),
            DataPoint(getDate("2023-06-08"), 5.0),
            DataPoint(getDate("2023-06-09"), 11.0),
            DataPoint(getDate("2023-06-10"), 4.0),
            DataPoint(getDate("2023-06-11"), 9.0),
            DataPoint(getDate("2023-06-12"), 4.0),
            DataPoint(getDate("2023-06-13"), 20.0),
            DataPoint(getDate("2023-06-14"), 2.0),
            DataPoint(getDate("2023-06-15"), 7.0),
            DataPoint(getDate("2023-06-16"), 24.0),
            DataPoint(getDate("2023-06-17"), 2.0),
            DataPoint(getDate("2023-06-18"), 4.0),
            DataPoint(getDate("2023-06-19"), 16.0),
            DataPoint(getDate("2023-06-20"), 11.0),
            DataPoint(getDate("2023-06-21"), 29.0),
            DataPoint(getDate("2023-06-22"), 6.0),
            DataPoint(getDate("2023-06-23"), 3.0),
            DataPoint(getDate("2023-06-24"), 8.0),
            DataPoint(getDate("2023-06-25"), 7.0),
            DataPoint(getDate("2023-06-26"), 5.0),
            DataPoint(getDate("2023-06-27"), 2.0),
            DataPoint(getDate("2023-06-28"), 1.0),
            DataPoint(getDate("2023-06-29"), 13.0),
            DataPoint(getDate("2023-06-30"), 26.0),
            DataPoint(getDate("2023-07-01"), 0.0)
        )
    }

    // Convert date into their respective month

    private fun getDate(dateString: String): Date {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.parse(dateString) ?: Date()
    }


    private fun getRecentLinksData() {
        val apiKey = sharedPreferences.getString("key",null)
        if (apiKey != null) {
            val call =
                communicationAPI.getRecentLinksData(apiKey)
            call.enqueue(object : Callback<MainApiClass> {
                override fun onResponse(
                    call: Call<MainApiClass>,
                    response: Response<MainApiClass>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        if (data != null) {
                            sendRecentLinksData(data.apiData.recentLinks)
                        }
                    } else {
                        Log.d("ERROR_MESSAGE", response.message())
                        Toast.makeText(this@MainActivity, "Something Wrong", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<MainApiClass>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Something Wrong", Toast.LENGTH_SHORT).show()
                    Log.d("RecentLinksFail", "FAIL")
                }

            })
        } else {
            Toast.makeText(this@MainActivity, "Something Went Wrong", Toast.LENGTH_SHORT).show()
        }
    }


    //  Send Recent Links Data to Fragment

    private fun sendRecentLinksData(recentLinks: List<RecentLinks>) {
        val fragment = RecentLinksFragment()
        val bundle = Bundle()
        bundle.putParcelableArrayList("recentLinksList", ArrayList(recentLinks))
        fragment.arguments = bundle
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.recentLinksFragment, fragment)
        transaction.commitAllowingStateLoss()

    }

    private fun getTopLinksData() {
        val apiKey = sharedPreferences.getString("key", null)
        if (apiKey != null) {
            val call =
                communicationAPI.getTopLinksData(apiKey)
            call.enqueue(object : Callback<MainApiClass> {
                override fun onResponse(
                    call: Call<MainApiClass>,
                    response: Response<MainApiClass>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        if (data != null) {
                            sendDataToFragment(data.apiData.topLinks)
                        }
                    } else {

                        Log.d("ERROR", response.message())
                        Toast.makeText(this@MainActivity, "Something Wrong", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<MainApiClass>, t: Throwable) {
                    Toast.makeText(this@MainActivity,"Something Wrong",Toast.LENGTH_SHORT).show()
                    Log.d("TopLinksFail", "FAIL")
                }

            })
        } else {
            Toast.makeText(this@MainActivity, "Something Went Wrong", Toast.LENGTH_SHORT).show()
        }


    }

    //  Send Top Links Data to Fragment

    private fun sendDataToFragment(topLinks: List<TopLinks>) {
        val fragment = TopLinksFragment()
        val bundle = Bundle()
        bundle.putParcelableArrayList("topLinksList", ArrayList(topLinks))
        fragment.arguments = bundle
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.topLinksFragment, fragment)
        transaction.commitAllowingStateLoss()

    }


    private fun getDataFromAPI() {
        val apiKey = sharedPreferences.getString("key", null)
        if (apiKey != null) {
            val call =
                communicationAPI.getDashBoardData(apiKey)
            call.enqueue(object : Callback<MainApiClass> {
                override fun onResponse(
                    call: Call<MainApiClass>,
                    response: Response<MainApiClass>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        if (data != null) {
                            setDatToParameters(data.todayClicks, data.topLocation, data.topSource)
                        } else {
                            Log.d("ERROR_BODY", response.message())
                            Toast.makeText(this@MainActivity,"Something Wrong",Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@MainActivity,"Something Wrong",Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<MainApiClass>, t: Throwable) {
                    Toast.makeText(this@MainActivity,"Something Wrong",Toast.LENGTH_SHORT).show()
                    Log.d("Graph Data Fail", "FAILURE")
                }

            })
        } else {
            Toast.makeText(this@MainActivity, "Something Went Wrong", Toast.LENGTH_SHORT).show()
        }


    }

   // Set Data to Parameters

    private fun setDatToParameters(todayClicks: Int?, topLocation: String?, topSource: String?) {
        TodayClicks.text = todayClicks.toString()
        TopLocation.text = topLocation
        TopSource.text = topSource
    }

}



