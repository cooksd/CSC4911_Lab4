package com.example.lab4.ui.termdetail

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.lab4.R
import com.example.lab4.ui.addclass.AddClassActivity
import com.example.lab4.ui.termdetail.ClassAdapter

class TermDetailFragment : Fragment(R.layout.fragment_term_detail) {
    private lateinit var viewModel: TermDetailViewModel
    private lateinit var classList: RecyclerView
    private lateinit var termSummary: TextView
    private lateinit var fab: FloatingActionButton
    private lateinit var classAdapter: ClassAdapter

    private val studentId by lazy { requireActivity().intent.getStringExtra("studentId")!! }
    private val termId    by lazy { requireActivity().intent.getStringExtra("termId")!! }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        termSummary = view.findViewById(R.id.termSummaryTextView)
        classList   = view.findViewById(R.id.classRecyclerView)
        fab         = view.findViewById(R.id.addClassFab)

        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(TermDetailViewModel::class.java)

        classAdapter = ClassAdapter { transcript ->
            viewModel.deleteTranscript(transcript)
        }
        classList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = classAdapter
        }

        fab.setOnClickListener {
            Intent(requireContext(), AddClassActivity::class.java).apply {
                putExtra("studentId", studentId)
                putExtra("termId", termId)
                startActivity(this)
            }
        }

        viewModel.transcriptsFor(studentId, termId).observe(viewLifecycleOwner) { list ->
            classAdapter.submitList(list)
            val courseList = viewModel.courses.value.orEmpty()
            termSummary.text = viewModel.buildSummary(
                studentId,
                termId,
                list,
                courseList
            )
        }
    }
}