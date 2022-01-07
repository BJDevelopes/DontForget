package project.n01221824.dontforget

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class AboutMe : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_me)

        //Access
        var back_button = findViewById<ImageButton>(R.id.BACKbutton)

        back_button.setOnClickListener {
            val myIntent = Intent(this, MainActivity::class.java)
            myIntent.putExtra("key", "Back to MainView!") //Optional parameters
            this.startActivity(myIntent)
        }

    }
}