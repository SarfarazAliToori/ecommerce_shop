package com.acclivousbyte.shopee.view.activties

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.*
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import com.acclivousbyte.shopee.R
import com.acclivousbyte.shopee.databinding.ActivityHomeBinding
import com.acclivousbyte.shopee.extensions.visible
import com.acclivousbyte.shopee.utils.AppUtils
import com.acclivousbyte.shopee.utils.SharePreferenceHelper
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.navigation.NavigationView
import org.koin.android.ext.android.inject

class HomeActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding
    private val sharePreferenceHelper: SharePreferenceHelper by inject()
    private lateinit var badge: BadgeDrawable


    private var isGuestMode: Boolean = false
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //check guest user and login user.
        checkUser()

    }

    @SuppressLint("UnsafeOptInUsageError", "ResourceAsColor")
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        val cartIconShowHide = menu.findItem(R.id.cartFragment)
        val filterIconShowHide = menu.findItem(R.id.filterFragment)
        val backIconShowHide = menu.findItem(R.id.backActionIcon)

        if (AppUtils.showHideCartBadge) {
            val badgeDrawable = BadgeDrawable.create(this).apply {
                isVisible = true
                backgroundColor = 0
//                badgeTextColor = R.color.white
                verticalOffsetWithText = -18
                if (AppUtils.badgeCounter != 0) {
                    number = AppUtils.badgeCounter
                }
            }
            //badgeDrawable.setTint(R.color.gray)
            BadgeUtils.attachBadgeDrawable(badgeDrawable, binding.appBarMain.toolbar, R.id.cartFragment)
            //badge.number = AppUtils.badgeCounter
        }


        // Show hide toolbar icons on different fragments.
        val cartSet = setOf(R.id.promotionsFragment,
            R.id.promotionDetailsFragment,
            R.id.productsDetailFragment,
            R.id.brandsFragment, R.id.brandDetailFragment,
            R.id.wishListFragment
        )

        val filterSet = setOf(R.id.productsFragment)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = navHostFragment.findNavController()

        navController.addOnDestinationChangedListener{ controller, distination, argument ->

            if (cartSet.contains(distination.id)) {
                cartIconShowHide?.isVisible = true
                filterIconShowHide?.isVisible = false
                backIconShowHide?.isVisible = false
            } else {
                cartIconShowHide?.isVisible = false
                filterIconShowHide?.isVisible = false
                backIconShowHide?.isVisible = false
            }

            if (filterSet.contains(distination.id)) {
                cartIconShowHide?.isVisible = true
                filterIconShowHide?.isVisible = true
                backIconShowHide?.isVisible = false
            }

            if (R.id.filterFragment == distination.id) {
                cartIconShowHide?.isVisible = false
                filterIconShowHide?.isVisible = false
                backIconShowHide?.isVisible = true
            }

            // hide toolbar of webview fragment and profile fragment and other
            val withHideToolbarSet = setOf(
                R.id.webViewFragment,
                R.id.profileFragment,
                R.id.orderSummaryFragment,
                R.id.signInFragment,
                R.id.signUpFragment,
                R.id.cartFragment,
                R.id.cartCheckOutFragment,
                R.id.filterFragment,
                R.id.settingsFragment
            )
            if (withHideToolbarSet.contains(distination.id)) {
                binding.appBarMain.toolbar.visibility = View.GONE
            } else {
                binding.appBarMain.toolbar.visibility = View.VISIBLE
            }
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return if (item.itemId == R.id.cartFragment) {
//            val action = NavGraphDirections.actionGlobalTermsFragment()
//            navController.navigate(action)
//            true
//        } else {
//            item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
//        }

        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun checkUser() {
        val userData = sharePreferenceHelper.getUserData()
        setSupportActionBar(binding.appBarMain.toolbar)
        drawerLayout = binding.drawerLayout
        navView = binding.navView
        badge = BadgeDrawable.create(this)

        if (userData != null) {

            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
            navController = navHostFragment.findNavController()
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.adsHomeFragment, R.id.promotionsFragment,
                    R.id.brandsFragment, R.id.productsFragment,
                    R.id.contactUsFragment, R.id.rateUsFragment,
                    R.id.settingsFragment, R.id.signInFragment
                ), drawerLayout
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)


            binding.appBarMain.toolbar.inflateMenu(R.menu.main_menu)

            binding.appBarMain.toolbar.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.cartFragment -> {
                        Log.i("menu", "Cart is Clicked.")
                        it.onNavDestinationSelected(navController) || super.onOptionsItemSelected(it)
                    }
                    R.id.filterFragment -> {
                        Log.i("menu", "filter is Clicked.")
                        it.onNavDestinationSelected(navController) || super.onOptionsItemSelected(it)
                    }
                    R.id.backActionIcon -> {
                        navController.popBackStack()
                    }
                }
                true
            }

            this.recreate() // for sign in the activtiy recreate because we want to show My orders, wishlist and profile.
            // only for sign out and sign in click
            navView.menu.findItem(R.id.signInFragment).setOnMenuItemClickListener { menuItem:MenuItem? ->
                //write your implementation here
                //to close the navigation drawer
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }
                lifecycleScope.launchWhenStarted {
                    sharePreferenceHelper.removeUserData()
                    //startActivity(Intent(this@HomeActivity, MainActivity::class.java))
                    findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.adsHomeFragment)
                }
//                this.finish()
                this.recreate()
                Log.i("check", "sign out")
                true
            }

            // set username
            val header = navView.getHeaderView(0)
            val name = header.findViewById<TextView>(R.id.header_user_name)
            val email = header.findViewById<TextView>(R.id.header_user_email)
            val charCircle = header.findViewById<TextView>(R.id.header_user_firs_char_circle)

            name.text = (userData.first_name +" ".plus(userData.last_name)).uppercase()
            email.text = userData.email
            charCircle.text = userData.first_name.first().uppercase()

        } else {

            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
            navController = navHostFragment.findNavController()
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.adsHomeFragment, R.id.promotionsFragment,
                    R.id.brandsFragment, R.id.productsFragment,
                    R.id.contactUsFragment,
                    R.id.rateUsFragment,
                    R.id.settingsFragment, R.id.signInFragment
                ), drawerLayout
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)


            binding.appBarMain.toolbar
            binding.appBarMain.toolbar.inflateMenu(R.menu.main_menu)

            binding.appBarMain.toolbar.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.cartFragment -> {
                        Log.i("menu", "Cart is Clicked.")
                        it.onNavDestinationSelected(navController) || super.onOptionsItemSelected(it)
                    }
                    R.id.filterFragment -> {
                        Log.i("menu", "filter is Clicked.")
                        it.onNavDestinationSelected(navController) || super.onOptionsItemSelected(it)
                    }
                    R.id.backActionIcon -> {
                        navController.popBackStack()
                    }
                }
                true
            }

  //           only for sign in and sign out click
            navView.menu.findItem(R.id.signInFragment).setOnMenuItemClickListener { menuItem:MenuItem? ->
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.signInFragment)
                Log.i("check", "sign in click")
//                this.finish()
                true
            }
            navView.menu.findItem(R.id.signInFragment).title = "Sign In"
            navView.menu.findItem(R.id.signInFragment).setIcon(R.drawable.sign_in)
            navView.menu.run {
                findItem(R.id.wishListFragment).isVisible = false
                findItem(R.id.profileFragment).isVisible = false
                findItem(R.id.orderListingFragment).isVisible = false
            }

            navView.findViewById<LinearLayout>(R.id.drawer_social_layout).run {
                this.visible()
                this.findViewById<ImageView>(R.id.drawer_facbook_icon).run {
                    setOnClickListener {
                        Toast.makeText(applicationContext, "facebook login not implemented yet", Toast.LENGTH_SHORT).show()
                    }
                }

                this.findViewById<ImageView>(R.id.drawer_instagram_icon).run {
                    setOnClickListener {
                        Toast.makeText(applicationContext, "Instagram login not implemented yet", Toast.LENGTH_SHORT).show()
                    }
                }

                this.findViewById<ImageView>(R.id.drawer_twitter_icon).run {
                    setOnClickListener {
                        Toast.makeText(applicationContext, "Twitter login not implemented yet", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            // Guest user
            // set username
            val header = navView.getHeaderView(0)
            val name = header.findViewById<TextView>(R.id.header_user_name)
            val email = header.findViewById<TextView>(R.id.header_user_email)
            val charCircle = header.findViewById<TextView>(R.id.header_user_firs_char_circle)
            name.text = "Guest User"
            email.text = "Guest User"
            charCircle.text = "G"
        }
    }

}