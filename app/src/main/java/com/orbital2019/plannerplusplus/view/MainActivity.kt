package com.orbital2019.plannerplusplus.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.jakewharton.threetenabp.AndroidThreeTen
import com.orbital2019.plannerplusplus.R

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    PopupMenu.OnMenuItemClickListener {


    // lateinit means variable is initialized later
    private val drawerLayout: DrawerLayout by lazy {
        findViewById<DrawerLayout>(R.id.drawer_layout)
    }
    private val fab: FloatingActionButton by lazy {
        findViewById<FloatingActionButton>(R.id.fab)
    }
    private lateinit var fragmentInMain: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidThreeTen.init(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.nav_menu_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navView: NavigationView = findViewById(R.id.nav_view)
        // ActionBarDrawerToggle class allows rotating animation and onClick for toolbar
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        // settles hamburger icon and animation by itself
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        // when device is rotated new activity is created via onCreate and it will always reset to nav_events
        // to circumvent this, we need to add an additional check:
        if (savedInstanceState == null) {
            // start activity will open one fragment immediately so that it does not start on an empty screen
            // by default, this fragment will be the Events fragment list.
            changeActiveFragment(EventsFragment())
            navView.setCheckedItem(R.id.nav_events)
            setEventsFabMenu()
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            // if drawer is open, pressing back button doesnt leave activity but closes navigation drawer
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            // else, drawer is not open, and close the app by calling super.onBackPressed()
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_events -> {
                changeActiveFragment(EventsFragment())
                setEventsFabMenu()
            }
            R.id.nav_tasks -> {
                changeActiveFragment(TasksFragment())
                setTasksFabMenu()
            }
            R.id.nav_log -> {
                changeActiveFragment(LogFragment())
                hideFabMenu()
            }
            R.id.nav_share -> Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun changeActiveFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
        fragmentInMain = fragment
    }

    private fun setEventsFabMenu() {
        fab.show()
        fab.setOnClickListener { view ->
            val popup = PopupMenu(this, view)
            popup.setOnMenuItemClickListener(this)
            popup.inflate(R.menu.events_fab_menu)
            popup.show()
        }
    }

    private fun setTasksFabMenu() {
        fab.show()
        fab.setOnClickListener { view ->
            val popup = PopupMenu(this, view)
            popup.setOnMenuItemClickListener(this)
            popup.inflate(R.menu.tasks_fab_menu)
            popup.show()
        }
    }

    private fun hideFabMenu() {
        fab.hide()
    }

    override fun onMenuItemClick(p0: MenuItem?): Boolean {
        //todo make proper item options
        // TODO: set onclick to new event menu with intent
        when (p0?.itemId) {
            R.id.new_event -> {
                val newEventIntent = AddNewEventActivity.newIntent(this)
                startActivity(newEventIntent)
            }
            R.id.copy_existing_event -> {
                Toast.makeText(this, "existing event", Toast.LENGTH_SHORT).show()
            }
            R.id.new_linked_event -> {
                Toast.makeText(this, "new linked event", Toast.LENGTH_SHORT).show()
            }
            R.id.link_existing_event -> {
                Toast.makeText(this, "link existing event", Toast.LENGTH_SHORT).show()
            }
            R.id.new_task -> {
                Toast.makeText(this, "new task", Toast.LENGTH_SHORT).show()
            }
            R.id.copy_existing_task -> {
                Toast.makeText(this, "existing task", Toast.LENGTH_SHORT).show()
            }
            R.id.new_linked_task -> {
                Toast.makeText(this, "new linked task", Toast.LENGTH_SHORT).show()
            }
            R.id.link_existing_task -> {
                Toast.makeText(this, "link existing task", Toast.LENGTH_SHORT).show()
            }
            else -> return false
        }
            return true
    }

}
