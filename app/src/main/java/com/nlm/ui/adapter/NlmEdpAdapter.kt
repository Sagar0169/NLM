package com.nlm.ui.adapter


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nlm.R
import com.nlm.model.NlmEdp
import com.nlm.model.OnlyCreated
import com.nlm.ui.activity.AddNewAssistanceForEaActivity
import com.nlm.ui.activity.AddNewMobileVeterinaryUnit
import com.nlm.ui.activity.AddNlmEdpActivity
import com.nlm.utilities.showView


class NlmEdpAdapter(private val onlyCreated: List<NlmEdp>, private val isFrom: Int) :
    RecyclerView.Adapter<NlmEdpAdapter.NlmEdpAdapterViewHolder>() {

    // ViewHolder class to hold the view elements
    class NlmEdpAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvStateName: TextView = itemView.findViewById(R.id.tvCommentChange)
        val tvComment: TextView = itemView.findViewById(R.id.tvComment)
        val tvCreated: TextView = itemView.findViewById(R.id.tvDate)
        val ivView: ImageView = itemView.findViewById(R.id.ivView)
        val ivEdit: ImageView = itemView.findViewById(R.id.ivEdit)
    }


    // Inflate the item layout and create the holder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NlmEdpAdapterViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_nlm_edp, parent, false)
        return NlmEdpAdapterViewHolder(itemView)
    }

    // Bind the data to the views in each item
    override fun onBindViewHolder(holder: NlmEdpAdapterViewHolder, position: Int) {
        val item = onlyCreated[position]
        holder.tvStateName.text = item.comment
        holder.tvCreated.text = item.created

        when(isFrom){
            0->{
                holder.ivView.setOnClickListener {
                    val intent = Intent(holder.itemView.context, AddNlmEdpActivity::class.java)
                    holder.ivView.context.startActivity(intent)
                }
                holder.ivEdit.setOnClickListener {
                    val intent = Intent(holder.itemView.context, AddNlmEdpActivity::class.java)
                    holder.ivEdit.context.startActivity(intent)
                }
            }
            1->{
                holder.tvComment.text = "Development Of Reading Material :"
                holder.ivView.setOnClickListener {
                    val intent = Intent(holder.itemView.context, AddNewAssistanceForEaActivity::class.java)
                    holder.ivView.context.startActivity(intent)
                }
                holder.ivEdit.setOnClickListener {
                    val intent = Intent(holder.itemView.context, AddNewAssistanceForEaActivity::class.java)
                    holder.ivEdit.context.startActivity(intent)
                }
            }
        }


//        holder.itemView.setOnClickListener {
//            val intent = Intent(holder.itemView.context, NodalOfficerDetailActivity::class.java)
//            intent.putExtra("nodalOfficer", item)
//            holder.itemView.context.startActivity(intent)
//        }

    }

    // Return the total number of items
    override fun getItemCount(): Int {
        return onlyCreated.size
    }
}
