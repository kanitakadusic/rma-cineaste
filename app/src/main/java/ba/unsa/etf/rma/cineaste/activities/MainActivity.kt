package ba.unsa.etf.rma.cineaste.activities

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ba.unsa.etf.rma.cineaste.R
import ba.unsa.etf.rma.cineaste.receivers.ConnectivityBroadcastReceiver
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private val receiver: BroadcastReceiver = ConnectivityBroadcastReceiver()
    private val filter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.hostFragmentMain) as NavHostFragment
        val navController = navHostFragment.navController
        val navView: BottomNavigationView = findViewById(R.id.bottomNavigationMain)
        navView.setupWithNavController(navController)

        if(intent?.action == Intent.ACTION_SEND && intent?.type == "text/plain") {
            intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
                val bundle = bundleOf("search" to it)

                navView.selectedItemId = R.id.searchFragment
                navController.navigate(R.id.searchFragment, bundle)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(receiver, filter)
    }

    override fun onPause() {
        unregisterReceiver(receiver)
        super.onPause()
    }
}