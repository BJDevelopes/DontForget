package project.n01221824.dontforget;

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDBOpenHelper(
    context: Context?,
    //name: String?,
    factory: SQLiteDatabase.CursorFactory?
    // version: Int
) : SQLiteOpenHelper(context, name, factory, version) {


    companion object {

        private val version = 1
        private val name = "MyReminders.db"
        val TABLE_NAME = "Reminders"
        val COLUMN_ID = "_id"
        val COLUM_NAME1 = "Name"
        val COLUMN_NAME2 = "Date"
        val COLUMN_NAME3 = "Desc"
    }

    override fun onCreate(db: SQLiteDatabase?) {

        val CREATE_REMINDERS_TABLE = ("CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUM_NAME1 + " VARCHAR2(20)," +
                COLUMN_NAME2 + " VARCHAR2(20)," +
                COLUMN_NAME3 + " VARCHAR2(20)" + ")")


        db?.execSQL(CREATE_REMINDERS_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME)
        onCreate(db)
    }

    fun addReminder(name: Reminder) {
        val values = ContentValues()
        values.put(COLUM_NAME1, name.reminderName)
        values.put(COLUMN_NAME2, name.reminderDate)
        values.put(COLUMN_NAME3, name.reminderDesc)
        val db = this.writableDatabase
        onCreate(db)
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getReminder(name: String): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUM_NAME1 = ?" , arrayOf(name))
    }

    fun getallReminder(): Cursor? {
        val db = this.readableDatabase
        onCreate(db)
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    fun DeleteAll(){
        val db = this.readableDatabase
        db?.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
    }

    fun DeleteSelect(name: String) {
        val db = this.readableDatabase
        db?.execSQL("DELETE FROM $TABLE_NAME WHERE $COLUM_NAME1 = '" + name + "'")
    }


}