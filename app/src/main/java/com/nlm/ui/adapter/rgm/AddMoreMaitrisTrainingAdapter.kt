package com.nlm.ui.adapter.rgm

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nlm.R
import com.nlm.databinding.ItemChooseFileBinding
import com.nlm.databinding.ItemFacultyMembersTrainingCentersBinding
import com.nlm.databinding.ItemMaitrisTrainingCentersBinding
import com.nlm.model.FacultyMembersTrainingCenter
import com.nlm.model.FileItem
import com.nlm.model.MaitrisTrainingCenter

class AddMoreMaitrisTrainingAdapter(

    val context: Context,
    private val items: MutableList<MaitrisTrainingCenter>,


    ) : RecyclerView.Adapter<AddMoreMaitrisTrainingAdapter.MyViewHolder>() {

    interface OnFileSelectListener {
        fun onChooseFileClicked(position: Int, category: String)
    }

    class MyViewHolder(val mBinding: ItemMaitrisTrainingCentersBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MyViewHolder {
        val mBinding: ItemMaitrisTrainingCentersBinding = DataBindingUtil.inflate(
            LayoutInflater.from(viewGroup.context),
            R.layout.item_maitris_training_centers,
            viewGroup,
            false
        )
        return MyViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]

        holder.mBinding.tvName.setText(item.name)


        holder.mBinding.tvDuration.setText(item.duration)
        holder.mBinding.tv2022.setText(item._2022)
        holder.mBinding.tv2023.setText(item._2023)
        holder.mBinding.tv2024.setText(item._2024)
//addtextchangelistener

        holder.mBinding.tvDuration.addTextChangedListener (object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                item.duration = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed
            }
        })
        holder.mBinding.tvName.addTextChangedListener (object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                item.name = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed
            }
        })
        holder.mBinding.tv2022.addTextChangedListener (object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                item._2022 = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed
            }
        })
        holder.mBinding.tv2023.addTextChangedListener (object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                item._2023 = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed
            }
        })
 holder.mBinding.tv2024.addTextChangedListener (object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                item._2024 = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed
            }
        })




//        holder.mBinding.tvChooseFile.setOnClickListener {
//
//            listener.onChooseFileClicked(position, category)
//            notifyItemChanged(position)
//        }


    }

    override fun getItemCount(): Int = items.size

    // Adding a blank/default item to the list
    fun addItem() {
        items.add(MaitrisTrainingCenter("","","","","")) // Add a blank PostData
        notifyItemInserted(items.size - 1)
    }

    // Removing the last item from the list
    fun removeItem() {
        if (items.size > 1) {
            items.removeAt(items.size - 1)
            notifyItemRemoved(items.size)
        }
    }

}