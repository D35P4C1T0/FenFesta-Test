package com.example.logintest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.logintest.data.settings.DataStoreUserPreference
import com.example.logintest.data.settings.SearchHistoryDataStore
import com.example.logintest.data.settings.ThemePreferences
import com.example.logintest.data.viewmodel.LoginState
import com.example.logintest.data.viewmodel.SearchHistoryViewModel
import com.example.logintest.data.viewmodel.ThemeOption
import com.example.logintest.data.viewmodel.ThemeViewModel
import com.example.logintest.data.viewmodel.ThemeViewModelFactory
import com.example.logintest.data.viewmodel.UserViewModel
import com.example.logintest.data.viewmodel.UserViewModelFactory
import com.example.logintest.ui.theme.AppTheme
import com.example.logintest.ui.theme.navigation.MyApp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private var mInterstitialAd: InterstitialAd? = null
    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val themePreferences = ThemePreferences(this)
        val themeViewModelFactory = ThemeViewModelFactory(themePreferences)
        val backgroundScope = CoroutineScope(Dispatchers.IO)

        // Inizializzazione Mobile Ads
        MobileAds.initialize(this) {}

        // Configurazione dispositivo di test
        val testDeviceIds = listOf("f37807bd-4d48-482a-a145-d3bfc60ebfc2")
        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(configuration)

        // Carica annuncio interstitial
        loadInterstitialAd()

        setContent {
            DynamicTheme(
                themeViewModel = viewModel(factory = themeViewModelFactory)
            )
        }
    }

    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
            }
        })
    }

    fun showInterstitialAd(onAdDismissed: () -> Unit) {
        if (mInterstitialAd != null) {
            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    onAdDismissed()
                    mInterstitialAd = null
                    loadInterstitialAd()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    onAdDismissed()
                }
            }
            mInterstitialAd?.show(this)
        } else {
            onAdDismissed()
            loadInterstitialAd()
        }
    }
}

@Composable
fun DynamicTheme(themeViewModel: ThemeViewModel) {
    // Theme
    val themeOption by themeViewModel.themeOption.collectAsState()
    val darkTheme = when (themeOption) {
        ThemeOption.LIGHT -> false
        ThemeOption.DARK -> true
        ThemeOption.SYSTEM -> isSystemInDarkTheme()
    }

    // Login
    val context = LocalContext.current
    val userPreferences = remember { DataStoreUserPreference(context) }
    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(userPreferences, context)
    )
    val loginState by userViewModel.loginState.collectAsState()
    val searchHistoryDataStore = remember { SearchHistoryDataStore(context) }
    val searchHistoryViewModel: SearchHistoryViewModel =
        viewModel { SearchHistoryViewModel(searchHistoryDataStore) }


    when (loginState) {
        is LoginState.Success -> {
            // Show main app content
            //MainContent(userViewModel)
        }

        else -> {
            //LoginScreen(userViewModel = userViewModel)
        }
    }

    AppTheme(darkTheme = darkTheme) {
        SystemUIController(isDarkTheme = darkTheme)
        MyApp(
            userModel = userViewModel,
            themeViewModel = themeViewModel,
            searchHistoryViewModel = searchHistoryViewModel
        )
    }
}

@Composable
fun SystemUIController(isDarkTheme: Boolean) {
    val systemUiController = rememberSystemUiController()
    val statusBarColor = MaterialTheme.colorScheme.surface

    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = !isDarkTheme
        )
    }
}