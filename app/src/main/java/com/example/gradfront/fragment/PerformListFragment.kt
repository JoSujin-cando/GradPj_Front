package com.example.gradfront.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gradfront.*
import com.example.gradfront.databinding.FragmentPerformListBinding

class PerformListFragment : Fragment() {
    lateinit var binding: FragmentPerformListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPerformListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 설정
        binding.perfRv.layoutManager = LinearLayoutManager(requireContext())

        // MainAdapter에 클릭 리스너 설정
        val adapter = PerformListAdapter(getData()) { item ->
            // 아이템 클릭 시 PerformList2Activity로 데이터 전달
            val intent = Intent(requireContext(), PerformList2::class.java).apply {
                putExtra("title", item.title)
                putExtra("subtitle", item.subtitle)
                putExtra("imageResId", item.imageResId)
            }
            startActivity(intent)
        }
        binding.perfRv.adapter = adapter

        //itemDecoration
        binding.perfRv.addItemDecoration(SpacingItem(20))
    }

    private fun getData(): List<ItemData> {
        return listOf(
            ItemData(R.drawable.diskimg, "Club FF", "행로난"),
            ItemData(R.drawable.ic_baseline_account_circle_24, "Club BB", "몽롱이"),
            ItemData(R.drawable.song, "Club CC", "시루봉"),
            ItemData(R.drawable.diskimg, "Club FF", "행로난"),
            ItemData(R.drawable.ic_baseline_account_circle_24, "Club BB", "몽롱이"),
            ItemData(R.drawable.song, "Club CC", "시루봉"),
            ItemData(R.drawable.diskimg, "Club FF", "행로난"),
            ItemData(R.drawable.ic_baseline_account_circle_24, "Club BB", "몽롱이"),
            ItemData(R.drawable.song, "Club CC", "시루봉")
        )
    }
}