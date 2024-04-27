package com.example.musicforprogrammers.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.musicforprogrammers.R
import com.example.musicforprogrammers.models.VolumeModel
import kotlinx.coroutines.launch


class VolumeControlsFragment: Fragment(R.layout.volume_controls_fragment) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.volume_controls_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(activity!!)[VolumeModel::class.java]

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.volumeState.collect { volumeData ->
                setPercentageText(volumeData.percentageVolume)
            }
        }
    }

    private fun setPercentageText(percentage: Int) {
        val percentageView = view?.findViewById<MFPTextView>(R.id.sound_volume)

        if (percentageView != null) {
            percentageView.text = getString(R.string.percentage, percentage)
        }
    }
}