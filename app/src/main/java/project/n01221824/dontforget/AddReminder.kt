package project.n01221824.dontforget

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast.*
import java.text.SimpleDateFormat
import java.util.*

class AddReminder : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_reminder)


        //Access Objects
        var add_button = findViewById<ImageButton>(R.id.finishbutton)
        var back_button = findViewById<ImageButton>(R.id.backButton)

        //Add Button
        //This will add and return back to default view displaying the reminds
        //For design purposes we wil only add our text box checks and then redirect to the Default view
        //as long they are not null
        add_button.setOnClickListener {
            val name = findViewById<TextView>(R.id.nameValue)
            val date = findViewById<TextView>(R.id.dateValue)
            val desc = findViewById<TextView>(R.id.descValue)

            //Null Check first Value
            if (!TextUtils.isEmpty(name.text)) {
                //Null Check second value
                if (!TextUtils.isEmpty(date.text)) {
                    //Null check for description (make sql database storage easier) not originally planned
                    //Format date properly
                    //Expected to be entered as EG 19-AUG-2021
                    var formatter = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
                    val Fdate = formatter.parse(date.text.toString())
                    if (!TextUtils.isEmpty(desc.text)) {
                       date.setText(Fdate.toString())
                        //////////////////DB STUFF////////////////////////
                        //Instance of DB class
                        val dbHandler = MyDBOpenHelper(this, null)

                        //Instance of Reminder
                        val rem = Reminder(
                            name.text.toString(),
                            date.text.toString(),
                            desc.text.toString()
                        )

                        //Insert
                        dbHandler.addReminder(rem)
                        //////////////////////////////////////////////////

                        //Successful Insert
                        makeText(this, "Success! Added Reminder - " + name.text.toString(), LENGTH_LONG).show()

                        //Clear Text Fields
                        name.setText("")
                        date.setText("")
                        desc.setText("")
                        //Successful
                        val myIntent = Intent(this, MainActivity::class.java)
                        myIntent.putExtra("key", "Back to MainView!") //Optional parameters
                        //Swap back to view
                        this.startActivity(myIntent)
                    }else{
                        //Toast error message no description
                        makeText(this, "Error! Missing Description", LENGTH_SHORT).show()
                    }
                }else{
                    //Toast error message no date
                    makeText(this, "Error! Missing Date", LENGTH_SHORT).show()
                }
            }else{
                //Toast error message no name
                makeText(this, "Error! Missing Name", LENGTH_SHORT).show()
            }

        }

        //back button
        //This function will cancel everything and go back to default view

        back_button.setOnClickListener {
            val myIntent = Intent(this, MainActivity::class.java)
            myIntent.putExtra("key", "Back to MainView!") //Optional parameters
            this.startActivity(myIntent)
        }




    }
}