package project.n01221824.dontforget

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import android.content.DialogInterface
import android.text.InputType
import android.text.TextUtils

import android.widget.EditText




private const val TAG = "BP-DB"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //Access Objects
        var add_button = findViewById<ImageButton>(R.id.ADDbutton)
        var edit_button = findViewById<ImageButton>(R.id.EDITbutton)
        var clear_button = findViewById<ImageButton>(R.id.CLEARbutton)
        var refresh_button = findViewById<ImageButton>(R.id.BACKbutton)

        //Switch to add reminder View
        add_button.setOnClickListener {
            val myIntent = Intent(this, AddReminder::class.java)
            myIntent.putExtra("key", "To Add View!") //Optional parameters
            this.startActivity(myIntent)
        }

        //This path will only be testing/design purposes until reminders are fully implements
        //Using this to get to ReminderView and using the button within ReminderView -> "EditText"
        //Phase 5 Notes
        //I've decided to rework this portion of the add to lookup the reminder when pressing edit
        //then lead into reminder view
        edit_button.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Enter Reminder Name")
            val input = EditText(this)
            input.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(input)

            builder.setPositiveButton(
                "OK"
            ) { dialog, which ->
                //check for null input
                if (!TextUtils.isEmpty(input.text)) {
                    val myIntent = Intent(this, ReminderView::class.java)
                    myIntent.putExtra("name", input.text.toString()) //Passing our name to lookup
                    this.startActivity(myIntent)
                }else{
                    //null error
                    Toast.makeText(applicationContext, "Empty name!", Toast.LENGTH_SHORT).show()
                }
            }
            builder.setNegativeButton(
                "Cancel"
            ) { dialog, which -> dialog.cancel() }

            builder.show()
        }

        //Clear Button
        //This button will clear all reminders
        //Confirmation prompt before doing this would be
        clear_button.setOnClickListener {
            //confirmation prompt
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Confirmation")
            alertDialogBuilder.setMessage("Are you sure you want to delete all reminders?")
            alertDialogBuilder.setPositiveButton("yes") { dialog, which ->
                //if yes
                /////SQL////
                //Instance of dbhandler class
                val dbHandler = MyDBOpenHelper(this, null)
                //Delete all
                dbHandler.DeleteAll()
                ////////////
                Toast.makeText(applicationContext, "Reminders deleted", Toast.LENGTH_SHORT).show()
                //Clear View
                //Accessing & Clearing Textview
                val reminderlist = findViewById<TextView>(R.id.ReminderList)
                reminderlist.setText("")

            }

            alertDialogBuilder.setNegativeButton("no") { dialog, which ->
                //if no
                Toast.makeText(applicationContext, "Delete cancelled", Toast.LENGTH_SHORT).show()
            }

            alertDialogBuilder.show()
        }

        //Refresh Button
        //This button will update the reminder list
        //For debugging purposes I have replaced the listview with a textview (to show this is actually working)
        //I Decided to keep the textview and go a different route with editing and deleting
        //Clicking the edit button will now handle more of this
        refresh_button.setOnClickListener {

            //access objects - Listview temporarily removed
            //val view = findViewById<ScrollView>(R.id.ReminderList)

            //access debug textview
            val reminderlist = findViewById<TextView>(R.id.ReminderList)

            //clear list
            reminderlist.setText("")

            ///SQL///
            //Instance of dbhandler class
            val dbHandler = MyDBOpenHelper(this, null)

            //query
            val cursor = dbHandler.getallReminder()

            //Check for an empty reminders list
            if (cursor!!.getCount() > 0) {

                cursor!!.moveToFirst()

                reminderlist.append(
                    (cursor.getString(
                        cursor.getColumnIndex(MyDBOpenHelper.COLUM_NAME1)
                    ))
                )
                reminderlist.append(" - ")
                reminderlist.append(
                    (cursor.getString(
                        cursor.getColumnIndex(MyDBOpenHelper.COLUMN_NAME2)
                    ))
                )

                reminderlist.append("\n")

                while (cursor.moveToNext()) {

                    reminderlist.append(
                        (cursor.getString(
                            cursor.getColumnIndex(MyDBOpenHelper.COLUM_NAME1)
                        ))
                    )
                    reminderlist.append(" - ")
                    reminderlist.append(
                        (cursor.getString(
                            cursor.getColumnIndex(MyDBOpenHelper.COLUMN_NAME2)
                        ))
                    )
                    reminderlist.append("\n")


                }

                cursor.close()
            }else{
                reminderlist.setText("Empty list!")
            }
        }

    }

    // help Menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    // actions on click menu items
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {

        //Move help screen
        R.id.action_help -> {
            val myIntent = Intent(this, Help::class.java)
            myIntent.putExtra("key", "To Help me!") //Optional parameters
            this.startActivity(myIntent)
            true
        }
        //Move to about me screen
        R.id.action_about -> {
            val myIntent = Intent(this, AboutMe::class.java)
            myIntent.putExtra("key", "To About me!") //Optional parameters
            this.startActivity(myIntent)
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }


}