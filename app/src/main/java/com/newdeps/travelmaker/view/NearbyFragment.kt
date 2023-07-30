package com.newdeps.travelmaker.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.newdeps.travelmaker.R
import com.newdeps.travelmaker.databinding.FragmentNearbyBinding
import com.newdeps.travelmaker.viewmodel.GeocoderViewModel
import com.newdeps.travelmaker.viewmodel.MarkerViewModel

class NearbyFragment : Fragment() {
    lateinit var binding: FragmentNearbyBinding
    private lateinit var geocoderViewModel: GeocoderViewModel
    private lateinit var markerViewModel: MarkerViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_nearby, container, false)

        //ViewModel
        geocoderViewModel = ViewModelProvider(requireActivity())[GeocoderViewModel::class.java]
        markerViewModel = ViewModelProvider(requireActivity())[MarkerViewModel::class.java]

        //DataBinding
        binding.lifecycleOwner = this //데이터바인딩 Lifecycle에 종속, LifeCycle_Observe역할
        binding.nearbyFragment = this
        binding.geocoderViewModel = geocoderViewModel
        binding.markerViewModel = markerViewModel

        nearbyFragmentInit()

        return binding.root
    }

    /** init */
    private fun nearbyFragmentInit() {

    }



}