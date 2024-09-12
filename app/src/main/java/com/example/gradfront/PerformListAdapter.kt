package com.example.gradfront

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gradfront.databinding.ItemRecyclerViewBinding


// ViewHolder와 Adapter 정의: item_recycler_view.xml에 있는 뷰를 바인딩하고 각 아이템에 필요한 정보도
class PerformListAdapter(private val dataList: List<ItemData>, private val PerfListClick: (ItemData) -> Unit) :
    RecyclerView.Adapter<PerformListAdapter.PerfViewHolder>() {

    class PerfViewHolder(val binding: ItemRecyclerViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerfViewHolder {
        val binding = ItemRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PerfViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PerfViewHolder, position: Int) {
        val item = dataList[position]
        holder.binding.textView20.text = item.title
        holder.binding.textView28.text = item.subtitle
        holder.binding.imageView4.setImageResource(item.imageResId)

        // 아이템 클릭 리스너 설정: RecyclerView 아이템 클릭 이벤트 처리
        holder.itemView.setOnClickListener {
            PerfListClick(item)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}
//data class ItemData(val imageResId: Int, val title: String, val subtitle: String) 왜 안 되지?