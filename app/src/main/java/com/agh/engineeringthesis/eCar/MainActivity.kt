package com.agh.engineeringthesis.eCar

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager
import com.agh.engineeringthesis.eCar.adapters.MainFragmentPagerAdapter
import com.agh.engineeringthesis.eCar.adapters.VehicleAdapter
import com.agh.engineeringthesis.eCar.db.VehicleContract
import com.agh.engineeringthesis.eCar.db.VehicleDBHelper
import com.agh.engineeringthesis.eCar.fragments.VehicleListFragment
import com.agh.engineeringthesis.eCar.model.Vehicle
import kotlinx.android.synthetic.main.activity_main.*
import com.agh.engineeringthesis.eCar.db.VehicleContract.VehicleEntry as Vehicles

class MainActivity : AppCompatActivity(), ViewPager.OnPageChangeListener { //NavigationView.OnNavigationItemSelectedListener

    var dbHelper: VehicleDBHelper? = null
    private var selectVehicleSpinner: Spinner? = null
    private var currentVehicle: Vehicle? = null
    val drawerToogle by lazy {
        ActionBarDrawerToggle(this, drawer_general_layout, toolbar, R.string.drawer_oper, R.string.drawer_close)
    }
    private lateinit var broadcastUpdateSpinner: BroadcastReceiver

    fun switchFragment(item: MenuItem): Boolean{
        when (item.itemId){
            R.id.main_page -> {
                println("main page pressed")
                mainViewPager.currentItem = 0
            }
            R.id.refuelling_list -> {
                println("refuelling pressed")
                mainViewPager.currentItem = 1
            }
            R.id.expense_list -> {
                mainViewPager.currentItem = 2
            }
            R.id.vehicle_manager -> {
                println("vehicle manager pressed")
                mainViewPager.currentItem = 3
            }
            R.id.notifications_item -> {
                val intent = Intent(this, NotificationsListActivity::class.java)
                intent.putExtra("vehicleID", currentVehicle!!.getId())
                startActivity(intent)
            }
            R.id.stats_item -> {
                val intent = Intent(this, StatisticsActivity::class.java)
                intent.putExtra("vehicleID", currentVehicle!!.getId())
                startActivity(intent)
            }
            R.id.charts_item -> {
                val intent = Intent(this, ChartsActivity::class.java)
                intent.putExtra("vehicleID", currentVehicle!!.getId())
                startActivity(intent)
            }
            R.id.calculator_item -> {
                val intent = Intent(this, CalculatorActivity::class.java)
                startActivity(intent)
            }
            R.id.app_info -> {
                val intent = Intent(this, AppInfoActivity::class.java)
                startActivity(intent)
            }
            else -> mainViewPager.currentItem = 0
        }
        drawer_general_layout.closeDrawer(GravityCompat.START)
       return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = null

        dbHelper = VehicleDBHelper(this, VehicleContract.tables)
        selectVehicleSpinner = findViewById(R.id.vehicle_spinner_list)

        createNewVehicleAdapter()

        selectVehicleSpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedVehicle: Vehicle = parent!!.getItemAtPosition(position) as Vehicle
                setVehicle(selectedVehicle)
                mainViewPager.adapter!!.notifyDataSetChanged()
                //refreshFragment()
            }
        }
        //drawerToogle.isDrawerIndicatorEnabled = true
        drawer_general_layout.addDrawerListener(drawerToogle)

        bottom_nav.setOnNavigationItemSelectedListener {
            switchFragment(it)
        }

        nav_view.setNavigationItemSelectedListener{
            switchFragment(it)
        }

        val pagerAdapter = MainFragmentPagerAdapter(supportFragmentManager)
        mainViewPager.adapter = pagerAdapter
        mainViewPager.addOnPageChangeListener(this)

        if ( (lastCustomNonConfigurationInstance as? Int) != null ) {
            bottom_nav.selectedItemId = (lastCustomNonConfigurationInstance as Int)
        } else {
            bottom_nav.selectedItemId = R.id.main_page
        }
        //switchFragment(bottom_nav.selectedItemId)

        val updateSpinnerFilter = IntentFilter("com.agh.engineeringthesis.vehiclemanager.UPDATE_SPINNER")
        broadcastUpdateSpinner = object : BroadcastReceiver(){
            override fun onReceive(context: Context, intent: Intent) {
                updateSpinner()
                var fragment: Fragment? = supportFragmentManager.fragments.last()
                if(fragment != null && fragment is VehicleListFragment) {
                    fragment.updateVehicleList()
                }
            }
        }
        registerReceiver(broadcastUpdateSpinner, updateSpinnerFilter)





    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToogle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToogle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (drawerToogle.onOptionsItemSelected(item)) true else super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        mainViewPager.adapter!!.notifyDataSetChanged()
//        refreshFragment()
    }

    private fun createNewVehicleAdapter() {
        val vehicleAdapter = VehicleAdapter(this, 0, getVehicles())

        if(vehicleAdapter.isEmpty){
            val intent = Intent(this, AddVehicleActivity::class.java)
            intent.putExtra("emptyVehicleList", true)
            startActivity(intent)
        }
        selectVehicleSpinner!!.adapter = vehicleAdapter
    }

    override fun onDestroy() {
        unregisterReceiver(broadcastUpdateSpinner)
        super.onDestroy()
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }

    private fun getVehicles(): List<Vehicle> {
        return dbHelper!!.getAll(Vehicles)
    }

    private fun setVehicle(vehicle: Vehicle) {
        currentVehicle = vehicle
    }

    private fun refreshFragment() {
        val currentFragment: Fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)!!
        val fragTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragTransaction.detach(currentFragment)
        fragTransaction.attach(currentFragment)
        fragTransaction.commit()
    }

    fun updateSpinner() {
        val selectedVehicle : Vehicle? = currentVehicle
        createNewVehicleAdapter()
        if(null != currentVehicle) {
            selectVehicleSpinner!!.setSelection(getSpinnerVehicleIndex(selectedVehicle!!))
        }
    }

    private fun getSpinnerVehicleIndex(vehicle: Vehicle): Int {
        for (i in 0 until selectVehicleSpinner!!.count) {
            if ((selectVehicleSpinner!!.getItemAtPosition(i) as Vehicle).getId() == vehicle.getId()) {
                return i
            }
        }
        return 0
    }

    fun getCurrentVehicle(): Vehicle? {
        return currentVehicle
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return bottom_nav.selectedItemId
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        val currentMenuItem = bottom_nav.menu.getItem(position).itemId
        if (currentMenuItem != bottom_nav.selectedItemId) {
            bottom_nav.menu.getItem(position).isChecked = true
            bottom_nav.menu.findItem(bottom_nav.selectedItemId).isChecked = false
        }
    }
}
