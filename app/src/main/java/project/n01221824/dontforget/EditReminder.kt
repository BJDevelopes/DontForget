package project.n01221824.dontforget

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

class EditReminder : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_reminder)

        //Access Objects
        var cancel_button = findViewById<ImageButton>(R.id.backButton)
        var finish_button = findViewById<ImageButton>(R.id.finishbutton)

        //Textboxes
        var name = findViewById<TextView>(R.id.nameValue)
        var date = findViewById<TextView>(R.id.dateValue)
        var desc = findViewById<TextView>(R.id.descValue)

        //Get the remind we need to look up from the intents
        val extras = intent.extras
        if (extras != null) {
            val value = extras.getString("name")
            //clear textfields
            name.setText("")
            date.setText("DD-MMM-YYYY")
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



                desc.append(
                    (cursor.getString(
                        cursor.getColumnIndex(MyDBOpenHelper.COLUMN_NAME3)
                    ))
                )


                cursor.close()

            }
        }

        //Switch to default View
        cancel_button.setOnClickListener {
            val myIntent = Intent(this, MainActivity::class.java)
            myIntent.putExtra("key", "To MainView!") //Optional parameters
            this.startActivity(myIntent)
        }

        //Function for finish button
        //This function will submit the changes and return to default view
        //For design purposes we wil only add our text box checks and then redirect to the Default view
        finish_button.setOnClickListener {
            val name = findViewById<TextView>(R.id.nameValue)
            val date = findViewById<TextView>(R.id.dateValue)
            val desc = findViewById<TextView>(R.id.descValue)
            ///SQL///
            //Instance of dbhandler class
            val dbHandler = MyDBOpenHelper(this, null)



            //Null Check first Value
            if (!TextUtils.isEmpty(name.text)) {
                //Null Check second value
                if (!TextUtils.isEmpty(date.text)) {
                    //Successful found no null fields
                    //Quick solution to updating by deleting the current reminder and inserting again (exactly the same if not modified)
                    val extras = intent.extras
                    if (extras != null) {
                        var formatter = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
                        val Fdate = formatter.parse(date.text.toString())
                        val value = extras.getString("name")
                        dbHandler.DeleteSelect(value.toString())
                        //Instance of Reminder
                        val rem = Reminder(
                            name.text.toString(),
                            Fdate.toString(),
                            desc.text.toString()
                        )

                        //Insert
                        dbHandler.addReminder(rem)
                    }

                    //back to mainview on successful update
                    val myIntent = Intent(this, MainActivity::class.java)
                    myIntent.putExtra("key", "Back to MainView!") //Optional parameters
                    this.startActivity(myIntent)
                    //notify user
                    Toast.makeText(this, "Successfully Finished Updating!", Toast.LENGTH_SHORT).show()
                }else{
                    //Toast error message no date
                    Toast.makeText(this, "Error! Missing Date", Toast.LENGTH_SHORT).show()
                }
            }else{
                //Toast error message no name
                Toast.makeText(this, "Error! Missing Name", Toast.LENGTH_SHORT).show()
            }

        }
    }
}