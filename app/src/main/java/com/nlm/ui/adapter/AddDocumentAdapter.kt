package com.nlm.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.nlm.callBack.DeleteItemCallBack
import com.nlm.callBack.SizeItemCallBack

import com.nlm.model.UploadDocumentsData
import com.nlm.utilities.Utility.formatFileSize
import com.nlm.utilities.Utility.getFileSizeFromUrl
import com.nlm.utilities.Utility.getFileType
import com.nlm.utilities.hideView
import com.nlm.utilities.showView
import nlm.R
import nlm.databinding.AddDocsItemBinding

class AddDocumentAdapter(
    val isFrom: Int,
    val context: Context,
    private var documents: MutableList<UploadDocumentsData>,
    private val deleteCallBackItem: DeleteItemCallBack,
    private val sizeCallBackItem: SizeItemCallBack
) : RecyclerView.Adapter<AddDocumentAdapter.MyViewHolder>() {

    class MyViewHolder(val mBinding: AddDocsItemBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val mBinding: AddDocsItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.add_docs_item,
            parent,
            false
        )
        return MyViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = documents[position]
        if (isFrom == 1) {
//            holder.mBinding.ivDownload.hideView()
            holder.mBinding.ivDelete.showView()
        } else {
//            holder.mBinding.ivDownload.showView()
            holder.mBinding.ivDelete.hideView()
        }

        val (isSupported, fileExtension) = getFileType(item.documents_url.plus(item.document_name))
        if (isSupported) {
            when (fileExtension) {
                "pdf" -> {
                    holder.mBinding.ivPic.let {
                        Glide.with(context).load(R.drawable.ic_pdf).into(
                            it
                        )
                    }
                }

                "png" -> {
                    holder.mBinding.ivPic.let {
                        Glide.with(context).load(item.documents_url.plus(item.document_name)).into(
                            it
                        )
                    }
                }

                "jpg" -> {
                    holder.mBinding.ivPic.let {
                        Glide.with(context).load(item.documents_url.plus(item.document_name)).into(
                            it
                        )
                    }
                }
            }
        }
        holder.mBinding.tvDocumentName.text = item.document_name
        CoroutineScope(Dispatchers.Main).launch {
            holder.mBinding.tvDocumentSize.text =
                getFileSizeFromUrl(item.documents_url.plus(item.document_name))?.let {
                    formatFileSize(
                        it
                    )
                }
        }
        sizeCallBackItem.onClickItemSize(documents.size)

//        holder.mBinding.ivDelete.setOnClickListener {
//            Utility.showDeleteDialog(context, object :
//                DialogCallbackDelete {
//                override fun onYes(id: String) {
//                    item.id?.let { it1 -> deleteCallBackItem.onClickItem(it1, position) }
//                }
//
//                override fun onNo() {
//                }
//            })
//        }
//        holder.mBinding.ivDownload.setOnClickListener {
//            if (isSupported) {
//                when (fileExtension) {
//                    "pdf" -> {
//                        val downloader = AndroidDownloader(context)
//                        item.document_name?.let { it1 ->
//                            downloader.downloadFile(
//                                item.documents_url.plus(item.document_name), "application/pdf",
//                                it1
//                            )
//                        }
//                    }
//
//                    "png" -> {
//                        val downloader = AndroidDownloader(context)
//                        item.document_name?.let { it1 ->
//                            downloader.downloadFile(
//                                item.documents_url.plus(item.document_name), "image/png",
//                                it1
//                            )
//                        }
//                    }
//
//                    "jpg" -> {
//                        val downloader = AndroidDownloader(context)
//                        item.document_name?.let { it1 ->
//                            downloader.downloadFile(
//                                item.documents_url.plus(item.document_name), "image/jpg",
//                                it1
//                            )
//                        }
//                    }
//                }
//            }
////            item.id?.let { it1 -> downloadCallBackItem.onClickItemDownload(item.documents_url.plus(item.document_name)) }
//        }
    }

    override fun getItemCount(): Int {
        return documents.size

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun removeItem(position: Int){
        documents.removeAt(position)
        notifyItemRemoved(position)
    }
}