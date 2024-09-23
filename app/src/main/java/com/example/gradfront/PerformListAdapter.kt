package com.example.gradfront

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.gradfront.data.LiveData
import com.example.gradfront.data.LiveDataWithClub
import com.example.gradfront.databinding.ItemRecyclerViewBinding


// ViewHolder와 Adapter 정의: item_recycler_view.xml에 있는 뷰를 바인딩하고 각 아이템에 필요한 정보도
class PerformListAdapter(private val dataList: List<LiveDataWithClub>, private val PerfListClick: (LiveDataWithClub) -> Unit) :
    RecyclerView.Adapter<PerformListAdapter.PerfViewHolder>() {

    class PerfViewHolder(val binding: ItemRecyclerViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerfViewHolder {
        val binding = ItemRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PerfViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PerfViewHolder, position: Int) {
        val item = dataList[position]
        holder.binding.textView20.text = item.liveData.title
        holder.binding.textView28.text = item.liveData.bandLineup
        holder.binding.imageView4.load(item.liveData.image)

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