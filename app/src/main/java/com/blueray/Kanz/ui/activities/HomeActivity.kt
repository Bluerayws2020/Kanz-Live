package com.blueray.Kanz.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.blueray.Kanz.R
import com.blueray.Kanz.databinding.ActivityHomeBinding
import com.blueray.Kanz.helpers.HelperUtils
import com.blueray.Kanz.helpers.HelperUtils.setDefaultLanguage
import com.blueray.Kanz.helpers.HelperUtils.setLang
import com.blueray.Kanz.helpers.ViewUtils.hide
import com.blueray.Kanz.helpers.ViewUtils.show

class HomeActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    private lateinit var binding : ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)

        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


        setDefaultLanguage(this@HomeActivity,"ar")
        setLang(this@HomeActivity,"ar")











        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        NavigationUI.setupWithNavController(binding.bottomNav, navController)
//        NavigationUI.setupWithNavController(binding.navDrawer, navController)
        binding.addNew.setOnClickListener{

            if (HelperUtils.getUid(this@HomeActivity) == "0"){
                Toast.makeText(this,"يجب تسجيل الدخول",Toast.LENGTH_LONG).show()

                startActivity(Intent(this,com.blueray.Kanz.ui.activities.SplashScreen::class.java))
                finish()

            }else {
                MyBottomSheetFragment().show(supportFragmentManager, MyBottomSheetFragment::class.java.simpleName)




            }
        }


        binding.bottomNav.setOnItemSelectedListener {
                item ->
            when(item.itemId){
                R.id.home ->{
//                    val v = Bundle()
//                    v.putString("123","1")
                    navController.navigate(R.id.homePagerFragment)
                    true
                }
                R.id.search->{
//                    replace(R.id.fragmentContainerView, liveEventListFragment)

navController.navigate(R.id.searchFragment)
                   true
                }
                R.id.PlaceHolder->{

                    if (HelperUtils.getUid(this@HomeActivity) == "0"){
                        Toast.makeText(this,"يجب تسجيل الدخول",Toast.LENGTH_LONG).show()

                        startActivity(Intent(this,com.blueray.Kanz.ui.activities.SplashScreen::class.java))
                        finish()

                        true
                    }else {

                        startActivity(Intent(this,UploadeVedio::class.java))
                        true

                    }

                    true
                }
                R.id.profiles->{
                    if (HelperUtils.getUid(this@HomeActivity) == "0"){
                        Toast.makeText(this,"يجب تسجيل الدخول",Toast.LENGTH_LONG).show()

                        startActivity(Intent(this,com.blueray.Kanz.ui.activities.SplashScreen::class.java))
                        finish()

                        false
                    }else {
                        navController.navigate(R.id.myAccountFragment)
                        true
                    }

                }
                else ->{
                    if (HelperUtils.getUid(this@HomeActivity) == "0"){
                        Toast.makeText(this,"يجب تسجيل الدخول",Toast.LENGTH_LONG).show()

                        startActivity(Intent(this,com.blueray.Kanz.ui.activities.SplashScreen::class.java))
                        finish()

                        false
                    }else {
                        navController.navigate(R.id.notfi)
true
                    }
                    true
                }
            }
        }

    }


    private fun autoAuthenticate(callback: (Boolean, String?) -> Unit) {
        val appId = "6A2870E9-4E98-4044-85DE-24DF3DDECB4B"
        val userId = HelperUtils.getUid(this)
        val accessToken = "27ef004db2ee6dcb0b628ef56229a072122a408c"

        if (appId == null || userId == null) {
            callback.invoke(false, null)
            return
        }


    }


//    private fun setUpDrawerNavigation() {
//        binding.navDrawer.setNavigationItemSelectedListener {
//                menuItem->
//            when(menuItem.itemId){
//                R.id.home->{
//                    navController.navigate(R.id.home)
//                    closeDrawer()
//                    true
//                }
////                R.id.
//
//            }
//
//
//            }
//    }


    fun showBottomNav(){
        binding.bottomNav.show()
    }
    fun hideBottomNav(){
        binding.bottomNav.hide()
    }

//        fun openDrawer(){
//            binding.drawerLayout.openDrawer(GravityCompat.START)
//        }
//        fun closeDrawer(){
//            binding.drawerLayout.closeDrawer(GravityCompat.START)
//        }

//    override fun onBackPressed() {
//        super.onBackPressed()
//
//        AlertDialog.Builder(this)
//                .setTitle("خروج")
//                .setMessage("هل انت متاكد من الخروج؟")
//                .setPositiveButton("نعم") { _, _ ->
//                    finishAffinity()
//                }
//                .setNegativeButton("لا", null)
//                .show()
//
//    }


    }