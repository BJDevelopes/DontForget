package project.n01221824.dontforget

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

class ReminderView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder_view)

        //Access Objects
        var back_button = findViewById<ImageButton>(R.id.backButton2)
        var edit_button = findViewById<ImageButton>(R.id.editButton)
        var delete_button = findViewById<ImageButton>(R.id.deleteButton)

        //access text objects
        var name = findViewById<TextView>(R.id.nameText)
        var date = findViewById<TextView>(R.id.dateText)
        var desc = findViewById<TextView>(R.id.descText)

        //Get the remind we need to look up from the intents
        val extras = intent.extras
        if (extras != null) {
            val value = extras.getString("name")
            //name.setText(value) debug test
            //clear textfields
            name.setText("")
            date.setText("")
            desc.setText("")

            ///SQL///
            //Instance of dbhandler class
            val dbHandler = MyDBOpenHelper(this, null)

            //query
            val cursor = dbHandler.getReminder(value.toString())

            //Check for an empty reminders list
            if (cursor!!.getCount() > 0) {

                cursor!!.moveToFirst()

                name.append(
                    (cursor.getString(
                        cursor.getColumnIndex(MyDBOpenHelper.COLUM_NAME1)
                    ))
                )

                date.append(
                    (cursor.getString(
                        cursor.getColumnIndex(MyDBOpenHelper.COLUMN_NAME2)
                    ))
                )

                desc.append(
                    (cursor.getString(
                        cursor.getColumnIndex(MyDBOpenHelper.COLUMN_NAME3)
                    ))
                )


                cursor.close()
            }else{
                name.setText("Error!")
                date.setText("Error!")
                desc.setText("Error! Nothing found")
            }

        }else{
            //nothing in intents (this should never happen)
            name.setText("Error")
            date.setText("Invalid")
            desc.setText("Input")
        }



        //Switch to default View
        back_button.setOnClickListener {
            val myIntent = Intent(this, MainActivity::class.java)
            myIntent.putExtra("key", "To Add View!") //Optional parameters
            this.startActivity(myIntent)
        }

        //Switch to Edit View
        edit_button.setOnClickListener {
            val myIntent = Intent(this, EditReminder::class.java)
            myIntent.putExtra("name", name.text.toString()) //Optional parameters
            myIntent.putExtra("date", date.text.toString()) //Optional parameters
            this.startActivity(myIntent)
        }

        //Delete Button
        //This button will delete and switch back to default
        delete_button.setOnClickListener {

            //Access object
            val name = findViewById<TextView>(R.id.nameText)
            val date = findViewById<TextView>(R.id.dateText)
            val desc = findViewById<TextView>(R.id.descText)

            /////MYSQL//////

            //mydb instance
            var dbHandler = MyDBOpenHelper(this, null)

            //Delete Reminder
            val extras = intent.extras
            if (extras != null) {
                val value = extras.getString("name")
                dbHandler.DeleteSelect(value.toString())
            }
            ////////////////

            //clear Values
            name.setText("")
            date.setText("")
            desc.setText("")
            //
            Toast.makeText(this, "Success! Reminder Cleared", Toast.LENGTH_LONG).show()
            //Return to mainview
            val myIntent = Intent(this, MainActivity::class.java)
            myIntent.putExtra("key", "To Mainview!") //Optional parameters
            this.startActivity(myIntent)
        }

    }
}