package com.tuwien.buildinginteractioninterfaces.prototype.presentation

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tuwien.buildinginteractioninterfaces.prototype.R
import com.tuwien.buildinginteractioninterfaces.prototype.domain.model.BenchmarkModel

class BenchmarkAdapter(var benchmarks: Array<BenchmarkModel>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class ViewHolder(mTextView: View): RecyclerView.ViewHolder(mTextView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent!!.context).inflate(R.layout.benchmark_adapter, parent, false)

        val vh = ViewHolder(v)
        return vh
    }

    override fun getItemCount(): Int {
        return benchmarks.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val id = holder.itemView.findViewById<TextView>(R.id.benchmark_id)
        val date = holder.itemView.findViewById<TextView>(R.id.benchmark_date)

        id.text = benchmarks[position].uid.toString()
        date.text = benchmarks[position].timestamp.toString()
    }

}