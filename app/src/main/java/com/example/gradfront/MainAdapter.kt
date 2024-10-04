package com.example.gradfront

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.gradfront.data.LiveData
import com.example.gradfront.data.LiveDataWithClub
import com.example.gradfront.databinding.ItemRecyclerViewBinding
import java.text.SimpleDateFormat
import java.util.*

// ViewHolder와 Adapter 정의: item_recycler_view.xml에 있는 뷰를 바인딩하고 각 아이템에 필요한 정보도
class MainAdapter(private val dataList: List<LiveDataWithClub>, private val MainClick: (LiveDataWithClub) -> Unit) :
    RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    class MainViewHolder(val binding: ItemRecyclerViewBinding) : RecyclerView.ViewHolder(binding.root)  //뷰홀더

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = ItemRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val item = dataList[position]
        /**/
        if(item.liveData.date == getCurrentDate()){ //만약 공연 날짜가 오늘이면 MainRv 아이템(오늘 공연 리스트)에 정보 전달
            holder.binding.textView20.text = item.liveData.title+" "+item.liveData.startTime
            holder.binding.textView28.text = item.liveData.bandLineup
            holder.binding.imageView4.load(item.liveData.image)
        }

        // 아이템 클릭 리스너 설정: RecyclerView 아이템 클릭 이벤트 처리
        holder.itemView.setOnClickListener {
            MainClick(item)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    /*onBindViewHolder의 if 문을 위해 MainFragment에서 가져옴: 현재 날짜를 가져오는 함수*/
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }
}
