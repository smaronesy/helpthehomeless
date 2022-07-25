package app.htheh.helpthehomeless.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import app.htheh.helpthehomeless.R
import app.htheh.helpthehomeless.databinding.ActivityHomelessBinding
import com.firebase.ui.auth.AuthUI
import com.google.android.material.navigation.NavigationView

class HomelessActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomelessBinding
    private lateinit var homelessViewModel: HomelessViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomelessBinding.inflate(layoutInflater)
        // Setting the activity layout
        setContentView(binding.root)

        homelessViewModel =
            ViewModelProvider(this).get(HomelessViewModel::class.java)

        // Setting the toolbar
        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids beøcause each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_about
            ), drawerLayout
        )

        // Tying Nav Controller to appBar
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Tying Nav Controller to the Nav View
        navView.setupWithNavController(navController)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                // add the logout implementation
                AuthUI.getInstance().signOut(this).addOnCompleteListener {
                    if(it.isSuccessful){
                        var authInt = Intent(this, AuthenticationActivity::class.java)
                        startActivity(authInt)
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}