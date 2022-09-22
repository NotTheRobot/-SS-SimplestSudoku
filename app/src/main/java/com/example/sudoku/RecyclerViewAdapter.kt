package com.example.sudoku

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sudoku.data.ItemCellsData
import com.example.sudoku.ui.PenOrPencil

class GridAdapter constructor(
    context: Context?,
    private val data: ItemCellsData,
    val itemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<GridAdapter.ViewHolder>() {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private val sizeOfCell = context!!.resources.displayMetrics.widthPixels / 10
    private val SMALL_MARGIN = 1
    private val BIG_MARGIN = 6

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view: View = mInflater.inflate(R.layout.grid_cell_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val layoutParams = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.width = sizeOfCell
        layoutParams.height = sizeOfCell

        layoutParams.setMargins(SMALL_MARGIN, SMALL_MARGIN, SMALL_MARGIN, SMALL_MARGIN)

        if (position in 27..35 || position in 54..62) {
            layoutParams.topMargin = BIG_MARGIN
        }
        if(position in 0..80 step 3 && position !in 0..80 step 9){
            layoutParams.leftMargin = BIG_MARGIN
        }

        if(data.pen[position] == 0){
            holder.itemText.text = setPencilLayout(position)
            holder.itemText.textSize = 9.5f
            holder.itemText.setTextColor(Color.argb(255, 158, 158, 158))
        }
        else{
            holder.itemText.text = data.pen[position].toString()
        }

        holder.bind(holder.itemText, itemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            if (payloads[0] == PenOrPencil.PEN) {
                holder.itemText.text = data.pen[position].toString()
            } else if (payloads[0] == PenOrPencil.PENCIL) {
                holder.itemText.text = setPencilLayout(position)
            }
        }
        else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemCount(): Int {
        return data.pen!!.size
    }

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var itemText = itemView.findViewById<TextView>(R.id.cell_text)
        fun bind(view: TextView, clickListener: OnItemClickListener) {
            itemText.setOnClickListener {
                clickListener.onItemClicked(view, adapterPosition)
            }
        }
    }

    private fun setPencilLayout(position: Int): String{
        val set = data.pencil[position]
        var result = ""
        for (i in 1..9) {
            if (i in set) {
                result = result + i.toString()
            } else {
                result = "$result  "
            }
            if (i != 9) {
                if(i % 3 == 0){
                    result = "$result\n"
                }
                else{
                    result = "$result  "
                }
            }
        }
        return result
    }
}

interface OnItemClickListener{
    fun onItemClicked(view: TextView, position: Int)
}
