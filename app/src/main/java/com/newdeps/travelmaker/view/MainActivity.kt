package com.newdeps.travelmaker.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.newdeps.travelmaker.R
import com.newdeps.travelmaker.adapter.ViewPager2Adapter
import com.newdeps.travelmaker.databinding.ActivityMainBinding
import com.newdeps.travelmaker.viewmodel.GeocoderViewModel
import com.newdeps.travelmaker.viewmodel.MarkerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var viewPager2Adapter: ViewPager2Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

       initViewPager2()
    }


    private fun initViewPager2() {
        viewPager2Adapter = ViewPager2Adapter(this)

        viewPager2Adapter.addFragment(Tab1Fragment())

        binding.vpMaPager.apply {
            offscreenPageLimit = viewPager2Adapter.itemCount //ViewPager 개수고정 (계속된 OnCreate 없도록)
            adapter = viewPager2Adapter

            //ViewPager pageChange
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    //HelzzangLog.e("YMC", "ViewPager onPageSelected position: $position")
                }
            })

            //오버스크롤 막기
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

            //스크롤 막기
            isUserInputEnabled = false
        }
    }










    override fun onBackPressed() {
        super.onBackPressed()
        this.finishAffinity()
    }
}