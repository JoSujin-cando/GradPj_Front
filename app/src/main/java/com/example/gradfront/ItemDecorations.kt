package com.example.gradfront

import android.graphics.Rect
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView


// SpacingItemDecoration: RecyclerView 항목들 사이에 여백을 추가하는 클래스
class SpacingItem(private val spaceHeight: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        with(outRect) {
            bottom = spaceHeight
            if (parent.getChildAdapterPosition(view) != 0) {
                top = spaceHeight
            }
        }
    }
}