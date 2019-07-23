package com.orbital2019.plannerplusplus.view.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.orbital2019.plannerplusplus.constants.ADD_EVENT_REQUEST
import com.orbital2019.plannerplusplus.constants.ADD_TASK_REQUEST
import com.orbital2019.plannerplusplus.constants.COPY_TASK_REQUEST
import com.orbital2019.plannerplusplus.constants.EDIT_EVENT_REQUEST
import com.orbital2019.plannerplusplus.model.entity.TaskEntity
import com.orbital2019.plannerplusplus.view.ui.selecttask.SelectTaskFragment

/**
 * todo:
 *  1. refactor all program logic to go through viewModel (viewModel methods return intent)
 */

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    PopupMenu.OnMenuItemClickListener, SelectTaskFragment.TaskSelectedListener {

    // lateinit means variable is initialized later
    private val drawerLayout: DrawerLayout by lazy {
        findViewById<DrawerLayout>(R.id.drawer_layout)
    }
    private val fab: FloatingActionButton by lazy {
        findViewById<FloatingActionButton>(R.id.fab)
    }
    private lateinit var fragmentInMain: Fragment
    private var selectedTask: TaskEntity? = null

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
        when (p0?.itemId) {
            R.id.new_event -> {
                val newEventIntent =
                    AddEditEventActivity.newIntent(this)
                startActivityForResult(newEventIntent, ADD_EVENT_REQUEST)
            }
            R.id.copy_existing_event -> {
                toast("copy existing event")
            }
            R.id.new_linked_event -> {
                Toast.makeText(this, "new linked event", Toast.LENGTH_SHORT).show()
            }
            R.id.link_existing_event -> {
                Toast.makeText(this, "link existing event", Toast.LENGTH_SHORT).show()
            }
            R.id.new_task -> {
                val newTaskIntent =
                    AddEditTaskActivity.newIntent(this)
                startActivityForResult(newTaskIntent, ADD_TASK_REQUEST)
            }
            R.id.copy_existing_task -> {
                // todo setTargetFragment
                SelectTaskFragment().show(supportFragmentManager, "select_task")
                toast("copy_existing_task")
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

    override fun onTaskSelected(task: TaskEntity) {
        selectedTask = task
        val copyEventIntent =
            AddEditTaskActivity.newIntent(this)
        copyEventIntent.putExtra(EXTRA_PARCEL_PLANNERTASK, getSelectedTask().copyTask())
        startActivityForResult(copyEventIntent, COPY_TASK_REQUEST)
    }

    override fun getSelectedTask(): TaskEntity {
        if (selectedTask != null) {
            val returnTask = selectedTask
            selectedTask = null
            return returnTask!!
        } else {
            throw Exception("No Task Selected!")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // todo debug wrong requestCode being passed
        when (requestCode) {
            ADD_EVENT_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    Log.d("req code", requestCode.toString())
                    Toast.makeText(this, data?.getStringExtra(EXTRA_SAVE_STATUS), Toast.LENGTH_SHORT).show()
                }
            }
            EDIT_EVENT_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    Log.d("req code", requestCode.toString())
                    Toast.makeText(this, data?.getStringExtra(EXTRA_SAVE_STATUS), Toast.LENGTH_SHORT).show()
                }
            }
            COPY_TASK_REQUEST -> {
                Toast.makeText(this, data?.getStringExtra(EXTRA_SAVE_STATUS), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "not saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun toast(string: String) {
        return Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }


}
