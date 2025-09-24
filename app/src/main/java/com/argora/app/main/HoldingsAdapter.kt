package com.argora.app.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.argora.app.data.Holding
import com.argora.app.databinding.ItemHoldingBinding
import java.text.NumberFormat
import java.util.Locale

class HoldingsAdapter(private val holdings: MutableList<Holding>) : RecyclerView.Adapter<HoldingsAdapter.HoldingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoldingViewHolder {
        val binding = ItemHoldingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HoldingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HoldingViewHolder, position: Int) {
        holder.bind(holdings[position])
    }

    override fun getItemCount() = holdings.size

    fun updateData(newHoldings: List<Holding>) {
        holdings.clear()
        holdings.addAll(newHoldings)
        notifyDataSetChanged()
    }

    class HoldingViewHolder(private val binding: ItemHoldingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(holding: Holding) {
            binding.tvStockName.text = holding.name
            binding.tvStockTicker.text = holding.ticker

            val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
            val currentValue = holding.currentPrice * holding.quantity
            binding.tvCurrentValue.text = currencyFormat.format(currentValue)

            binding.tvQuantity.text = "${holding.quantity} shares"
        }
    }
}