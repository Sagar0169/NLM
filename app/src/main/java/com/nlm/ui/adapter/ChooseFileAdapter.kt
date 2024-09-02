package com.nlm.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nlm.R
import com.nlm.databinding.ItemChooseFileBinding
import com.nlm.model.FileItem

class ChooseFileAdapter(

    val context: Context,
    private val items: MutableList<FileItem>,
    private val category: String,
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
        val item = items[position]
        holder.mBinding.tvDocumentName.text = item.fileName
        holder.mBinding.ivDelete.setOnClickListener{
            items.removeAt(items.size - 1)
            notifyItemRemoved(items.size)
        }

        item.fileUri?.let { uri ->
            Glide.with(holder.itemView.context)
                .load(uri)
                .placeholder(R.drawable.place_holder_images) // Add a placeholder image if needed
                .into(holder.mBinding.ivPic)
        } ?: run {
            // If there's no URI, you might want to clear the ImageView or show a default image
            holder.mBinding.ivPic.setImageResource(R.drawable.place_holder_images)
        }


//        holder.mBinding.tvChooseFile.setOnClickListener {
//
//            listener.onChooseFileClicked(position, category)
//            notifyItemChanged(position)
//        }


    }

    override fun getItemCount(): Int = items.size


}