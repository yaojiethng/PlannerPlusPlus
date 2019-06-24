package com.orbital2019.plannerplusplus

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast

class AddNewEvent : AppCompatActivity() {

    private val editTextTitle: EditText by lazy {
        findViewById<EditText>(R.id.edit_text_title)
    }
    private val editTextDetails: EditText by lazy {
        findViewById<EditText>(R.id.edit_text_details)
    }

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_event)

        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_close_black_24dp)
        title = "Add Note"
    }

    // todo: when go to next event save all entered data, when back put details back
    fun saveEvent() {

        val title: String = editTextTitle.text.toString()
        val details: String = editTextDetails.text.toString()

        if (title.trim().isEmpty() || details.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show()
            return
        }

        /* Intent data = new Intent()
        data.putExtra(EXTRA_TITLE, title)
        data.putExtra(EXTRA_DESCRIPTION, description)
        data.putExtra(EXTRA_PRIORITY, priority)*/

        // setResult(RESULT_OK, data)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        // todo: make relevant menu (no menu?)
        // menuInflater.inflate(R.menu.add_note_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        /*return when (item!!.itemId) {
            R.id.save_event -> {
                saveEvent()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }*/
        return super.onOptionsItemSelected(item)
    }
}
