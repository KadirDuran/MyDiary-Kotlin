package com.example.mydiary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.mydiary.databinding.RcRowBinding
import com.example.mydiary.model.Moment
import com.example.mydiary.view.LyMemoriesDirections

class MomentAdapter(val momentList:List<Moment>) : RecyclerView.Adapter<MomentAdapter.MomentHolder>() {

    class MomentHolder(val binding : RcRowBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MomentHolder {
        val rcRowBinding  = RcRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  MomentHolder(rcRowBinding)
    }

    override fun getItemCount(): Int {
       return momentList.size
    }

    override fun onBindViewHolder(holder: MomentHolder, position: Int) {
        holder.binding.txtMomentName.text = momentList[position].title
        holder.itemView.setOnClickListener {
            val action = LyMemoriesDirections.actionLyMemoriesToLyMoment(false,momentList[position].id)
            Navigation.findNavController(it).navigate(action)
        }
    }
}