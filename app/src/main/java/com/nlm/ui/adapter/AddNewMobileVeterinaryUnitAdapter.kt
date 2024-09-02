package com.nlm.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nlm.databinding.ItemAddMobileveterinaryUnitBinding
import com.nlm.model.Indicator
import com.nlm.utilities.hideView
import com.nlm.utilities.showView

class AddNewMobileVeterinaryUnitAdapter(
    private val questions: List<Indicator>,
    private val isFrom: Int
) :
    RecyclerView.Adapter<AddNewMobileVeterinaryUnitAdapter.AddNewMobileVeterinaryUnitViewHolder>() {

    inner class AddNewMobileVeterinaryUnitViewHolder(private val binding: ItemAddMobileveterinaryUnitBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(question: Indicator, position: Int) {
            binding.tvIndicatorName.text = question.indicatorNumber
            binding.tvQuestionDescription.text = question.indicatorText
            when (isFrom) {
                4 -> {
                    when (position) {

                        0 -> {
                            binding.rbGroup.showView()
                            binding.etInput.hideView()
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddNewMobileVeterinaryUnitViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAddMobileveterinaryUnitBinding.inflate(inflater, parent, false)
        return AddNewMobileVeterinaryUnitViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddNewMobileVeterinaryUnitViewHolder, position: Int) {
        holder.bind(questions[position], position)

    }

    override fun getItemCount(): Int = questions.size
}
