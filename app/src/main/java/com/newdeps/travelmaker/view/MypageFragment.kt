package com.newdeps.travelmaker.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.newdeps.travelmaker.R
import com.newdeps.travelmaker.databinding.FragmentMypageBinding
import com.newdeps.travelmaker.viewmodel.GeocoderViewModel
import com.newdeps.travelmaker.viewmodel.MarkerViewModel

class MypageFragment : Fragment() {
    lateinit var binding: FragmentMypageBinding
    private lateinit var geocoderViewModel: GeocoderViewModel
    private lateinit var markerViewModel: MarkerViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mypage, container, false)

        //ViewModel
        geocoderViewModel = ViewModelProvider(requireActivity())[GeocoderViewModel::class.java]
        markerViewModel = ViewModelProvider(requireActivity())[MarkerViewModel::class.java]

        //DataBinding
        binding.lifecycleOwner = this //데이터바인딩 Lifecycle에 종속, LifeCycle_Observe역할
        binding.mypageFragment= this
        binding.geocoderViewModel = geocoderViewModel
        binding.markerViewModel = markerViewModel

        mypageFragmentInit()

        return binding.root
    }

    /** init */
    private fun mypageFragmentInit() {

    }



}