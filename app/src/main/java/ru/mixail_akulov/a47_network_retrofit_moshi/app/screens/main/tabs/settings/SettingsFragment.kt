package ru.mixail_akulov.a47_network_retrofit_moshi.app.screens.main.tabs.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.mixail_akulov.a47_network_retrofit_moshi.app.R
import ru.mixail_akulov.a47_network_retrofit_moshi.app.databinding.FragmentSettingsBinding
import ru.mixail_akulov.a47_network_retrofit_moshi.app.screens.base.BaseFragment
import ru.mixail_akulov.a47_network_retrofit_moshi.app.utils.observeResults

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    override val viewModel by viewModels<SettingsViewModel>()

    private lateinit var binding: FragmentSettingsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)

        binding.resultView.setTryAgainAction { viewModel.tryAgain() }
        val adapter = setupList()
        viewModel.boxSettings.observeResults(this, view, binding.resultView) {
            adapter.renderSettings(it)
        }
    }

    private fun setupList(): SettingsAdapter {
        binding.settingsList.layoutManager = LinearLayoutManager(requireContext())
        val adapter = SettingsAdapter(viewModel)
        binding.settingsList.adapter = adapter
        return adapter
    }

}