package com.robertojr.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.robertojr.moov.R
import com.robertojr.moov.databinding.ActivityMapsHomeBinding
import com.robertojr.moov.databinding.NavHeaderMapsHomeBinding
import com.robertojr.moov.model.RetrofitClient
import com.robertojr.util.credentialData
import com.robertojr.util.userSection
import kotlinx.coroutines.launch

class MapsHomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMapsHomeBinding


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsHomeBinding.inflate(layoutInflater)
        val headerView = binding.navView.getHeaderView(0)
        val bindingHeader = NavHeaderMapsHomeBinding.bind(headerView)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMapsHome.toolbar)



        lifecycleScope.launch {

            val credential = RetrofitClient.loginRetrofit.findById(userSection.credentialId)

            if (credential.isSuccessful){

                bindingHeader.txtEmailUsuario.text = credential.body()?.email
                credentialData = credential.body()
            }


        }

            bindingHeader.txtNameUsuario.text = userSection.name+" "+userSection.lastName







        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_maps_home)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.home_maps,R.id.config_activity
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.maps_home, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_maps_home)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


}