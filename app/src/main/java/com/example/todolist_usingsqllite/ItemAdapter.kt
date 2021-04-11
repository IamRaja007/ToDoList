package com.example.todolist_usingsqllite

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist_usingsqllite.Database.TodoModelClass
import kotlinx.android.synthetic.main.items_row.view.*

class ItemAdapter(val context:Context,val items:ArrayList<TodoModelClass>)
    :RecyclerView.Adapter<ItemAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(context).inflate(
                        R.layout.items_row,
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=items.get(position)

        holder.tvName.text=item.name
        holder.tvEmail.text=item.email

        // Updating the background color according to the odd/even positions in list.
        if (position % 2 == 0) {
            holder.llMain.setBackgroundColor(
                    ContextCompat.getColor(
                            context,
                            R.color.colorLightGray         //check colors.xml file in values
                    )
            )
        } else {
            holder.llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.white))   //check in colors.xml file
        }

        holder.ivEdit.setOnClickListener {

            if (context is MainActivity) {
                context.updateRecordDialog(item)
            }
        }

        holder.ivDelete.setOnClickListener {

            if (context is MainActivity) {
                context.deleteRecordAlertDialog(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        /* Here all variables like ' llmain ', 'tvName' etc
           they all are customised names that we have given to the IDs that are in the ' items_row ' inflated view


         */
        val llMain = view.rveachlistdemo
        val tvName = view.tvname
        val tvEmail = view.tvmail
        val ivEdit = view.ivedit
        val ivDelete = view.ivdelete
    }

}

