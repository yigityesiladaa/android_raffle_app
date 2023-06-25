package com.kimkazandi.ui.groups

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kimkazandi.R
import com.kimkazandi.listeners.ISharedGroupListener
import com.kimkazandi.models.Raffle

class CustomRafflesAdapter(private val context: Context, private val listener: ISharedGroupListener) :
    RecyclerView.Adapter<CustomRafflesAdapter.GroupsViewHolder>() {

    private var raffles = listOf<Raffle>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomRafflesAdapter.GroupsViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.raffle_card,parent,false)
        return GroupsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CustomRafflesAdapter.GroupsViewHolder, position: Int) {

        val currentRaffleCard = raffles[position]
        Glide.with(context).load(currentRaffleCard.imageUrl).into(holder.imgRaffleImage)
        holder.txtTitle.text = currentRaffleCard.title
        holder.txtLastJoinDate.text = currentRaffleCard.lastJoinDate
        holder.txtMinSpend.text = currentRaffleCard.minSpend
        holder.txtTotalGiftAmount.text = currentRaffleCard.totalGiftAmount

        holder.itemView.setOnClickListener {
            listener.onItemClickListener(currentRaffleCard)
        }

    }

    override fun getItemCount(): Int {
        return raffles.count()
    }

    fun submitList(list: List<Raffle>){
        raffles = list
        notifyDataSetChanged()
    }

    class GroupsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val imgRaffleImage: ImageView = itemView.findViewById(R.id.imgRaffleImage)
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val txtLastJoinDate: TextView = itemView.findViewById(R.id.txtLastJoinDate)
        val txtMinSpend: TextView = itemView.findViewById(R.id.txtMinSpend)
        val txtTotalGiftAmount: TextView = itemView.findViewById(R.id.txtTotalGiftAmount)
    }
}