package com.newdeps.travelmaker.adapter

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.newdeps.travelmaker.view.Tab1Fragment

//ViewPager2Adapter.kt
class ViewPager2Adapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    var fragments: ArrayList<Tab1Fragment> = ArrayList()

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Tab1Fragment {
        return fragments[position]
    }

    fun addFragment(fragment: Tab1Fragment) {
        fragments.add(fragment)
        notifyItemInserted(fragments.size - 1)
        //TODO: notifyItemInserted!!
    }

    fun removeFragement() {
        fragments.removeLast()
        notifyItemRemoved(fragments.size)
        //TODO: notifyItemRemoved!!
    }

    fun getFragment(position: Int): Tab1Fragment {
        return fragments[position]
    }
}