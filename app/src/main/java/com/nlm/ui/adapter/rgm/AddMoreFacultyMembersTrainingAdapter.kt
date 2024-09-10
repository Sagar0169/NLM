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
import com.nlm.model.FacultyMembersTrainingCenter
import com.nlm.model.FileItem

class AddMoreFacultyMembersTrainingAdapter(

    val context: Context,
    private val items: MutableList<FacultyMembersTrainingCenter>,


    ) : RecyclerView.Adapter<AddMoreFacultyMembersTrainingAdapter.MyViewHolder>() {

    interface OnFileSelectListener {
        fun onChooseFileClicked(position: Int, category: String)
    }

    class MyViewHolder(val mBinding: ItemFacultyMembersTrainingCentersBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MyViewHolder {
        val mBinding: ItemFacultyMembersTrainingCentersBinding = DataBindingUtil.inflate(
            LayoutInflater.from(viewGroup.context),
            R.layout.item_faculty_members_training_centers,
            viewGroup,
            false
        )
        return MyViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]

        holder.mBinding.etFilledPost.setText(item.noOfFilledPosts)


        holder.mBinding.etVacantPost.setText(item.vacantPosts)
        holder.mBinding.etPostContractualBasis.setText(item.postFilledOnContractual)
        holder.mBinding.etSanctionedPost.setText(item.sanctionedPost)
//addtextchangelistener

        holder.mBinding.etFilledPost.addTextChangedListener (object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                item.noOfFilledPosts = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed
            }
        })
        holder.mBinding.etVacantPost.addTextChangedListener (object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                item.vacantPosts = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed
            }
        })
        holder.mBinding.etPostContractualBasis.addTextChangedListener (object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                item.postFilledOnContractual = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed
            }
        })
        holder.mBinding.etSanctionedPost.addTextChangedListener (object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                item.sanctionedPost = s.toString()
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
        items.add(FacultyMembersTrainingCenter("","","","")) // Add a blank PostData
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