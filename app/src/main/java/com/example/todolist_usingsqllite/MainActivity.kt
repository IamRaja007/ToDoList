package com.example.todolist_usingsqllite

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist_usingsqllite.Database.DatabaseHandler
import com.example.todolist_usingsqllite.Database.TodoModelClass
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_update.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_addrecord.setOnClickListener {

            addRecord()

        }
        setTheRecyclerView()
    }

    private fun addRecord(){
        Log.d("rcvd","addRecord functin")
        val newname=et_name.text.toString()
        val newemail=et_email.text.toString()

        val databasehandler=DatabaseHandler(this)

        if(newname.isEmpty() && newemail.isEmpty())
        {
            Toast.makeText(this,"Name or E-mail cannot be Empty",Toast.LENGTH_SHORT).show()

        }
        else
        {
            val status=databasehandler.addWork(TodoModelClass(0,newname,newemail))
            if(status > -1)
            {
                Toast.makeText(this,"Work Saved!",Toast.LENGTH_SHORT).show()
                et_name.text.clear()
                et_email.text.clear()

                setTheRecyclerView()
            }

        }


    }

    private fun setTheRecyclerView()
    {

        if (getItemList().size > 0) {

            rvitemslist.visibility=View.VISIBLE
            tvNoRecordsAvailable.visibility=View.GONE

            // Set the LayoutManager that this RecyclerView will use.
            rvitemslist.layoutManager = LinearLayoutManager(this)
            // Adapter class is initialized and list is passed in the param.
            val itemAdapter = ItemAdapter(this, getItemList())
            // adapter instance is set to the recyclerview to inflate the items.
            rvitemslist.adapter = itemAdapter
        }
        else
        {
            rvitemslist.visibility=View.GONE
            tvNoRecordsAvailable.visibility=View.VISIBLE

        }
    }

    private fun getItemList():ArrayList<TodoModelClass>
    {
        val databasehandler=DatabaseHandler(this)

        val worklist=databasehandler.viewWork()

        return worklist
    }

    fun updateRecordDialog(TMC: TodoModelClass) {
        val updateDialog = Dialog(this, R.style.Theme_Dialog)     //see themes.xml in themes folder
        updateDialog.setCancelable(false)
        updateDialog.setContentView(R.layout.dialog_update)

        updateDialog.etupdatename.setText(TMC.name)
        updateDialog.etupdateemail.setText(TMC.email)

        updateDialog.btnupdate.setOnClickListener {

            val name = updateDialog.etupdatename.text.toString()
            val email =updateDialog.etupdateemail.text.toString()

            val databaseHandler: DatabaseHandler = DatabaseHandler(this)

            if (!name.isEmpty() && !email.isEmpty()) {
                val status =
                        databaseHandler.updateWork(TodoModelClass(TMC.id, name, email))
                if (status > -1) {
                    Toast.makeText(applicationContext, "Record Updated.", Toast.LENGTH_LONG).show()

                    setTheRecyclerView()

                    updateDialog.dismiss() // Dialog will be dismissed
                }
            } else {
                Toast.makeText(
                        applicationContext,
                        "Name or Email cannot be blank",
                        Toast.LENGTH_LONG
                ).show()
            }
        }
        updateDialog.btncancel.setOnClickListener {
            updateDialog.dismiss()
        }
        //Start the dialog and display it on screen.
        updateDialog.show()
    }


    fun deleteRecordAlertDialog(TMC: TodoModelClass) {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Delete Record")
        //set message for alert dialog
        builder.setMessage("Are you sure you want to delete the record of ' ${TMC.name} ' ")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, which ->

            //creating the instance of DatabaseHandler class
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
            //calling the deleteEmployee method of DatabaseHandler class to delete record
            val status = databaseHandler.deleteWork(TodoModelClass(TMC.id,"",""))
            if (status > -1) {
                Toast.makeText(
                        applicationContext,
                        "Record deleted successfully.",
                        Toast.LENGTH_LONG
                ).show()

                setTheRecyclerView()
            }

            dialogInterface.dismiss() // Dialog will be dismissed
        }
        //performing negative action
        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss() // Dialog will be dismissed
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false) // Will not allow user to cancel after clicking on remaining screen area.
        alertDialog.show()  // show the dialog to UI
    }
}