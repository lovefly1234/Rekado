package com.pavelrekun.rekado.containers

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.pavelrekun.magta.isGesturalNavigationEnabled
import com.pavelrekun.penza.pickers.theme.ThemePickerViewModel
import com.pavelrekun.penza.services.helpers.DesignHelper
import com.pavelrekun.rekado.R
import com.pavelrekun.rekado.base.BaseActivity
import com.pavelrekun.rekado.databinding.ActivityContainerSecondaryBinding
import com.pavelrekun.rekado.services.dialogs.DialogsShower
import com.pavelrekun.rekado.services.extensions.*
import de.halfbit.edgetoedge.Edge
import de.halfbit.edgetoedge.edgeToEdge

class SecondaryContainerActivity : BaseActivity() {

    private val themesPickerViewModel: ThemePickerViewModel by viewModels()

    private lateinit var binding: ActivityContainerSecondaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityContainerSecondaryBinding.inflate(layoutInflater).apply { setContentView(this.root) }

        prepareToolbar()
        prepareNavigation(R.id.secondaryLayoutContainer)
        prepareDestination()
        prepareEdgeToEdge()
        prepareObservers()
    }

    private fun prepareEdgeToEdge() {
        if (isGesturalNavigationEnabled() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.setDecorFitsSystemWindows(false)
            }

            DesignHelper.tintNavigationBar(this, Color.TRANSPARENT)
        }

        edgeToEdge {
            binding.secondaryLayoutToolbar.fit { Edge.Top }
        }
    }

    private fun prepareDestination() {
        when (intent.getIntExtra(NAVIGATION_TYPE, NAVIGATION_DESTINATION_UNKNOWN)) {
            NAVIGATION_DESTINATION_SETTINGS -> controller.navigate(R.id.navigationSettings)
            NAVIGATION_DESTINATION_ABOUT -> controller.navigate(R.id.navigationAbout)
            NAVIGATION_DESTINATION_TRANSLATORS -> controller.navigate(R.id.navigationTranslators)
            NAVIGATION_DESTINATION_TOOLS_SERIAL_CHECKER -> controller.navigate(R.id.navigationSerialChecker)
        }
    }

    private fun prepareToolbar() {
        val title = intent.getStringExtra(NAVIGATION_TITLE) as String
        binding.secondaryLayoutToolbar.title = title
        setSupportActionBar(binding.secondaryLayoutToolbar)
        binding.secondaryLayoutToolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun prepareObservers() {
        themesPickerViewModel.selectionResult.observe(this, Observer {
            DialogsShower.showSettingsRestartDialog(this)
        })

        themesPickerViewModel.controlResult.observe(this, Observer {
            DialogsShower.showSettingsRestartDialog(this)
        })
    }

    override fun onBackPressed() {
        if (!controller.popBackStack()) {
            finish()
        }
    }

}