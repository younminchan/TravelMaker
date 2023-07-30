package com.newdeps.travelmaker.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.newdeps.travelmaker.R
import com.newdeps.travelmaker.adapter.ViewPager2Adapter
import com.newdeps.travelmaker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var viewPager2Adapter: ViewPager2Adapter

    private lateinit var ivTabArr: Array<AppCompatImageView>
    private lateinit var tvTabArr: Array<AppCompatTextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.lifecycleOwner = this //데이터바인딩 Lifecycle에 종속, LifeCycle_Observe역할
        binding.mainActivity = this

        ivTabArr = arrayOf(binding.ivTab1, binding.ivTab2, binding.ivTab3, binding.ivTab4)
        tvTabArr = arrayOf(binding.tvTab1, binding.tvTab2, binding.tvTab3, binding.tvTab4)

        initViewPager2()
    }


    private fun initViewPager2() {
        viewPager2Adapter = ViewPager2Adapter(this)

        viewPager2Adapter.addFragment(RecommendFragment())
        viewPager2Adapter.addFragment(NearbyFragment())
        viewPager2Adapter.addFragment(CollectionFragment())
        viewPager2Adapter.addFragment(MypageFragment())

        binding.vpMaPager.apply {
            offscreenPageLimit = viewPager2Adapter.itemCount //ViewPager 개수 고정 (계속된 OnCreate 없도록)
            adapter = viewPager2Adapter

            //ViewPager pageChange
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    clearTab()
                    ivTabArr[position].setColorFilter(getColor(R.color.blue))
                    tvTabArr[position].setTextColor(getColor(R.color.blue))

                }
            })

            //오버스크롤 막기
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

            //좌우 스크롤 막기
            isUserInputEnabled = false
        }
    }

    /** 탭 선택 */
    fun setTabPosition(position: Int) {
        binding.vpMaPager.setCurrentItem(position, false)
    }

    /** 탭 선택 clear */
    private fun clearTab() {
        ivTabArr.forEach {
            it.clearColorFilter()
        }
        tvTabArr.forEach {
            it.setTextColor(getColor(R.color.black))
        }
    }






    override fun onBackPressed() {
        super.onBackPressed()
        this.finishAffinity()
    }
}