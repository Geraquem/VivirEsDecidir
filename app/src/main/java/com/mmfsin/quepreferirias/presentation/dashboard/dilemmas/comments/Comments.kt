package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.comments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mmfsin.quepreferirias.databinding.BsheetCommentsBinding
import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.presentation.dashboard.dilemmas.adapter.RecentCommentsAdapter

class Comments(private val comments: List<Comment>) : BottomSheetDialogFragment() {

    private lateinit var binding: BsheetCommentsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BsheetCommentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpComments()
    }

    private fun setUpComments() {
        binding.apply {
            rvComments.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = RecentCommentsAdapter(comments)
            }
        }
    }
}