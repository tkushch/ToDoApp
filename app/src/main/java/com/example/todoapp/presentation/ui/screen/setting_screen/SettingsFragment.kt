package com.example.todoapp.presentation.ui.screen.setting_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentSettingsBinding
import com.example.todoapp.presentation.ui.MainActivity
import com.example.todoapp.presentation.ui.theme.control.ThemeController
import com.example.todoapp.presentation.ui.theme.control.ThemeHelper
import javax.inject.Inject

class SettingsFragment : Fragment() {

    @Inject
    lateinit var themeController: ThemeController

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireContext() as MainActivity).activityComponent.fragmentComponentFactory().create()
            .inject(this)

        binding.closeSettingsButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        themeLogic()

    }

    private fun themeLogic() {
        when (themeController.getTheme()) {
            ThemeHelper.LIGHT_MODE -> binding.settingsRadioLightTheme.isChecked = true
            ThemeHelper.DARK_MODE -> binding.settingsRadioDarkTheme.isChecked = true
            ThemeHelper.SYSTEM_MODE -> binding.settingsRadioSystemTheme.isChecked = true
        }

        binding.settingsRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.settings_radio_light_theme -> themeController.setLightTheme()
                R.id.settings_radio_dark_theme -> themeController.setDarkTheme()
                R.id.settings_radio_system_theme -> themeController.setSystemTheme()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}



