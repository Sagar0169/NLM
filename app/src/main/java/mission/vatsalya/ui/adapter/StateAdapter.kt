package mission.vatsalya.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mission.vatsalya.R
import mission.vatsalya.ui.activity.RegistrationActivity

class StateAdapter(private val stateList: List<String>,private val callBackItem: RegistrationActivity) :
    RecyclerView.Adapter<StateAdapter.StateViewHolder>() {

    // ViewHolder class to hold the view elements
    class StateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvStateName: TextView = itemView.findViewById(R.id.tvStateName)
    }

    // Inflate the item layout and create the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StateViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_state, parent, false)
        return StateViewHolder(itemView)
    }

    // Bind the data to the views in each item
    override fun onBindViewHolder(holder: StateViewHolder, position: Int) {
        val stateName = stateList[position]
        holder.tvStateName.text = stateName

        holder.tvStateName.setOnClickListener {
            callBackItem.onClickItem(stateName)
        }
    }

    // Return the total number of items
    override fun getItemCount(): Int {
        return stateList.size
    }
}
