package com.example.gradfront

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.gradfront.data.LiveData
import com.example.gradfront.databinding.ItemRecyclerViewBinding

class MyPageAdapter(private val dataList: List<LiveData>, private val MyPageClick: (LiveData) -> Unit) :
    RecyclerView.Adapter<MyPageAdapter.MyViewHolder>() {

    class MyViewHolder(val binding: ItemRecyclerViewBinding) :
        RecyclerView.ViewHolder(binding.root)  //뷰홀더

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = dataList[position]
        holder.binding.textView20.text = item.title
        holder.binding.textView28.text = item.bandLineup
        holder.binding.imageView4.load(item.image)

        // 아이템 클릭 리스너 설정: RecyclerView 아이템 클릭 이벤트 처리
        holder.itemView.setOnClickListener {
            MyPageClick(item)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}