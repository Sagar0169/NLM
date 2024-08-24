package mission.vatsalya.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mission.vatsalya.R
import mission.vatsalya.ui.activity.RegistrationActivity
import mission.vatsalya.ui.fragment.SightedBasicDetailsFragment

class RelationshipAdapter(private val relationList: List<String>,private val callBackItem: SightedBasicDetailsFragment) :
    RecyclerView.Adapter<RelationshipAdapter.RelationshipViewHolder>() {

    // ViewHolder class to hold the view elements
    class RelationshipViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvStateName: TextView = itemView.findViewById(R.id.tvStateName)
    }

    // Inflate the item layout and create the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelationshipViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_state, parent, false)
        return RelationshipViewHolder(itemView)
    }

    // Bind the data to the views in each item
    override fun onBindViewHolder(holder: RelationshipViewHolder, position: Int) {
        val stateName = relationList[position]
        holder.tvStateName.text = stateName

        holder.tvStateName.setOnClickListener {
            callBackItem.onClickItem(stateName)
        }
    }

    // Return the total number of items
    override fun getItemCount(): Int {
        return relationList.size
    }
}
