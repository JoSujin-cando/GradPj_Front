package com.example.gradfront

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.gradfront.data.BookingResponse
import com.example.gradfront.data.LiveData
import com.example.gradfront.data.LiveDataWithClub
import com.example.gradfront.databinding.ItemRecyclerViewBinding

class MyPageAdapter(private val dataList: List<Pair<BookingResponse, LiveDataWithClub?>>, private val MyPageClick: (BookingResponse, LiveDataWithClub?) -> Unit) :
    RecyclerView.Adapter<MyPageAdapter.MyViewHolder>() {

    class MyViewHolder(val binding: ItemRecyclerViewBinding) :
        RecyclerView.ViewHolder(binding.root)  //뷰홀더

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val (booking, live) = dataList[position]
        holder.binding.textView20.text = booking.liveTitle
        holder.binding.textView28.text =  live?.liveData?.bandLineup ?: "Unknown Lineup"
        holder.binding.imageView4.load(live?.liveData?.image?: R.drawable.ic_launcher_background)

        // 아이템 클릭 리스너 설정: RecyclerView 아이템 클릭 이벤트 처리
        holder.itemView.setOnClickListener {
            MyPageClick(booking, live)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}