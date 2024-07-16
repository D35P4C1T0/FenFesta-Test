package com.fenfesta

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fenfesta.data.notifications.RequestNotificationPermission
import com.fenfesta.data.settings.DataStoreUserPreference
import com.fenfesta.data.settings.SearchHistoryDataStore
import com.fenfesta.data.settings.ThemePreferences
import com.fenfesta.data.viewmodel.EventViewModel
import com.fenfesta.data.viewmodel.EventViewModelFactory
import com.fenfesta.data.viewmodel.LocationViewModel
import com.fenfesta.data.viewmodel.LocationViewModelFactory
import com.fenfesta.data.viewmodel.SearchHistoryViewModel
import com.fenfesta.data.viewmodel.ThemeOption
import com.fenfesta.data.viewmodel.ThemeViewModel
import com.fenfesta.data.viewmodel.ThemeViewModelFactory
import com.fenfesta.data.viewmodel.UserViewModel
import com.fenfesta.data.viewmodel.UserViewModelFactory
import com.fenfesta.ui.theme.AppTheme
import com.fenfesta.ui.theme.navigation.MyApp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.mapbox.maps.MapboxExperimental
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class MainActivity : ComponentActivity() {
    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val themePreferences = ThemePreferences(this)
        val themeViewModelFactory = ThemeViewModelFactory(themePreferences)
        val backgroundScope = CoroutineScope(Dispatchers.IO)

        // Inizializzazione Mobile Ads
        MobileAds.initialize(this) {}

        // Configurazione dispositivo di test
        val testDeviceIds =
            listOf(
                "f37807bd-4d48-482a-a145-d3bfc60ebfc2",
                "2f3c5547-a0ba-4d4c-9ba8-de61c42b61b7"
            )
        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(configuration)

        // Carica annuncio interstitial
        loadInterstitialAd()

        setContent {
            DynamicTheme(
                themeViewModel = viewModel(factory = themeViewModelFactory),
            )
        }
    }

    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            this,
            "ca-app-pub-3940256099942544/1033173712",
            adRequest,
            object : InterstitialAdLoadCallback() {
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

@OptIn(MapboxExperimental::class)
@Composable
fun DynamicTheme(themeViewModel: ThemeViewModel) {
    // Theme
    val themeOption by themeViewModel.themeOption.collectAsState()
    val darkTheme = when (themeOption) {
        ThemeOption.LIGHT -> false
        ThemeOption.DARK -> true
        ThemeOption.SYSTEM -> isSystemInDarkTheme()
    }

    // Notification Permission
    var notificationPermissionGranted by remember { mutableStateOf(false) }

    // Andri
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        RequestNotificationPermission { isGranted ->
            notificationPermissionGranted = isGranted
        }
    } else {
        notificationPermissionGranted = true
    }

    // Login
    val context = LocalContext.current
    val userPreferences = remember { DataStoreUserPreference(context) }
    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(userPreferences = userPreferences, context = context)
    )

    val eventViewModel: EventViewModel = viewModel(
        factory = EventViewModelFactory(userPreferences = userPreferences, context = context)
    )

    val locationViewModel: LocationViewModel = viewModel(
        factory = LocationViewModelFactory(context)
    )

    val searchHistoryDataStore = remember { SearchHistoryDataStore(context) }
    val searchHistoryViewModel: SearchHistoryViewModel =
        viewModel { SearchHistoryViewModel(searchHistoryDataStore) }

    AppTheme(darkTheme = darkTheme) {
        SystemUIController(isDarkTheme = darkTheme)
        MyApp(
            userViewModel = userViewModel,
            themeViewModel = themeViewModel,
            searchHistoryViewModel = searchHistoryViewModel,
            eventsViewModel = eventViewModel,
            locationViewModel = locationViewModel,
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
