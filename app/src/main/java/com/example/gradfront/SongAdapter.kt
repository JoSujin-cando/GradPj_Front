package com.example.gradfront

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.gradfront.data.SongRecommendResponse
import com.example.gradfront.databinding.ItemRecyclerViewBinding
import com.example.gradfront.fragment.SongFragment2

class SongAdapter(private val trackList: ArrayList<SongRecommendResponse>,
                  private val fragment: SongFragment2) :
    RecyclerView.Adapter<SongAdapter.TrackViewHolder>() {

    class TrackViewHolder(val binding: ItemRecyclerViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = ItemRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = trackList[position]
        holder.binding.textView20.text = track.trackName
        holder.binding.textView28.text = track.artistName

        // 이미지 URL이 null이 아니고 유효한지 확인
        holder.binding.imageView4.load(track.imageUrl) {
            transformations(CircleCropTransformation()) // 선택적으로 이미지 변환
        }

        // 클릭 리스너 추가
        holder.itemView.setOnClickListener {
            // 클릭된 아이템의 previewUrl을 Fragment에 전달
            fragment.playPreview(track.previewUrl) // 변경된 부분
        }

    }

    override fun getItemCount(): Int = trackList.size
}