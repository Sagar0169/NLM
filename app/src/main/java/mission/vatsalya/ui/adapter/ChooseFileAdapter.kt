package mission.vatsalya.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import mission.vatsalya.R
import mission.vatsalya.databinding.ItemChooseFileBinding
import mission.vatsalya.ui.activity.RegistrationActivity

class ChooseFileAdapter(

    private val items: MutableList<String>,
    private val category:String,
    private val listener: OnFileSelectListener,



) : RecyclerView.Adapter<ChooseFileAdapter.MyViewHolder>() {

    interface OnFileSelectListener {
        fun onChooseFileClicked(position: Int, category: String)
    }

    class MyViewHolder(val mBinding: ItemChooseFileBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MyViewHolder {
        val mBinding: ItemChooseFileBinding = DataBindingUtil.inflate(
            LayoutInflater.from(viewGroup.context),
            R.layout.item_choose_file,
            viewGroup,
            false
        )
        return MyViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val fileName = items[position]
        holder.mBinding.tvFileName.text = fileName

        holder.mBinding.tvChooseFile.setOnClickListener {

                listener.onChooseFileClicked(position, category) // Pass context here
//



            notifyItemChanged(position)
        }



    }

    override fun getItemCount(): Int = items.size

//    fun addItem() {
//        if (items.size < 5) {
//            items.add("No File Chosen")
//            notifyItemInserted(items.size - 1)
//        }
//    }
//
//    fun removeItem(position: Int) {
//        if (items.size > 1) {
//            items.removeAt(position)
//            notifyItemRemoved(position)
//        }
//    }
}