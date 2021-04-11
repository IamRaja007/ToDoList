package com.example.todolist_usingsqllite.Database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context:Context):SQLiteOpenHelper(
        context,
        DATABASE_NAME,
        null,
        DATABASE_VERSION
) {

    companion object{
        private const val DATABASE_VERSION= 1
        private const val DATABASE_NAME="ToDoDatabase"
        private const val TABLE_NAME="ToDoTable" //No Blank spaces in betwwen remember

        private const val KEY_ID="_id"
        private const val KEY_NAME="name"
        private const val KEY_EMAIL="email"

    }

    override fun onCreate(db: SQLiteDatabase?) {
       //Creating table with fields
        val CREATE_TODO_TABLE=("CREATE TABLE "+ TABLE_NAME + "("
                + KEY_ID +" INTEGER PRIMARY KEY,"+ KEY_NAME +" TEXT,"
                + KEY_EMAIL +" TEXT"+ ")")                   //all the spaces given are necessary

        //written in format ->
        //CREATE TABLE ToDoTable(_id INTEGER PRIMARY KEY,work TEXT,done INTEGER)

        db?.execSQL(CREATE_TODO_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME)
        onCreate(db)
    }

    //function to insert data
    fun addWork(TMC:TodoModelClass): Long {
        val db=this.writableDatabase

        val contentvalues=ContentValues()
        contentvalues.put(KEY_NAME,TMC.name) //TodoModelClass Work
        contentvalues.put(KEY_EMAIL,TMC.email)

        val success=db.insert(TABLE_NAME,null,contentvalues)

        db.close()    //closing the database

        return success
    }

    //function to read data
    fun viewWork():ArrayList<TodoModelClass>{
        val worklist=ArrayList<TodoModelClass>()

        val selectQuery="SELECT  * FROM $TABLE_NAME"

        val db=this.readableDatabase

        var cursor: Cursor?

        try {
            cursor=db.rawQuery(selectQuery,null)
        }
        catch (e:SQLiteException)
        {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id:Int
        var name:String
        var email:String


        if(cursor.moveToFirst()) {
            do {
                id=cursor.getInt(cursor.getColumnIndex(KEY_ID))
                name=cursor.getString(cursor.getColumnIndex(KEY_NAME))
                email=cursor.getString(cursor.getColumnIndex(KEY_EMAIL))

                val TMC=TodoModelClass(id=id,name=name,email=email)
                worklist.add(TMC)
            }
                while (cursor.moveToNext())
            cursor.close()
        }

        db.close()

        return  worklist

    }

    //function to update work
    fun updateWork(TMC:TodoModelClass):Int{

        val db=this.writableDatabase

        val contentvalues=ContentValues()
        contentvalues.put(KEY_NAME,TMC.name) //TodoModelClass Name
        contentvalues.put(KEY_EMAIL,TMC.email)

        //updating row
        val success=db.update(TABLE_NAME,contentvalues, KEY_ID + "=" + TMC.id , null)

        db.close()    //closing the database

        return success
    }

    //function to delete work
    fun deleteWork(TMC:TodoModelClass):Int {

        val db=this.writableDatabase

        val contentvalues=ContentValues()
        contentvalues.put(KEY_ID,TMC.id)   //TodoModelClass id

        //deleting row
        val success=db.delete(TABLE_NAME,KEY_ID + "=" + TMC.id,null)

        db.close()    //closing the database

        return success

    }


}