package com.example.gradfront

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gradfront.databinding.ItemRecyclerViewBinding

// ViewHolder와 Adapter 정의: item_recycler_view.xml에 있는 뷰를 바인딩하고 각 아이템에 필요한 정보도
class MainAdapter(private val dataList: List<ItemData>, private val MainClick: (ItemData) -> Unit) :
    RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    class MainViewHolder(val binding: ItemRecyclerViewBinding) : RecyclerView.ViewHolder(binding.root)  //뷰홀더

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = ItemRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val item = dataList[position]
        holder.binding.textView20.text = item.title
        holder.binding.textView28.text = item.subtitle
        holder.binding.imageView4.setImageResource(item.imageResId)

        // 아이템 클릭 리스너 설정: RecyclerView 아이템 클릭 이벤트 처리
        holder.itemView.setOnClickListener {
            MainClick(item)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}

data class ItemData(val imageResId: Int, val title: String, val subtitle: String)
