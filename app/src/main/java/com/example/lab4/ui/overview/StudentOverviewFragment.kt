package com.example.lab4.ui.overview

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab4.R
import com.example.lab4.ui.termdetail.TermDetailActivity

class StudentOverviewFragment : Fragment(R.layout.fragment_student_overview) {

    private lateinit var viewModel: StudentOverviewViewModel
    private lateinit var studentSpinner: Spinner
    private lateinit var gpaTextView: TextView
    private lateinit var termRecycler: RecyclerView
    private lateinit var termAdapter: TermAdapter

    private var currentStudentId: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        studentSpinner = view.findViewById(R.id.studentSpinner)
        gpaTextView    = view.findViewById(R.id.gpaTextView)
        termRecycler   = view.findViewById(R.id.termRecyclerView)

        termAdapter = TermAdapter { term ->
            currentStudentId?.let { sid ->
                Intent(requireContext(), TermDetailActivity::class.java).apply {
                    putExtra("studentId", sid)
                    putExtra("termId", term.termId)
                    startActivity(this)
                }
            }
        }
        termRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = termAdapter
        }

        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(StudentOverviewViewModel::class.java)

        viewModel.students.observe(viewLifecycleOwner) { students ->
            val names = students.map { "${it.lastName}, ${it.firstName.first()}." }
            studentSpinner.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                names
            ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

            studentSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) { }
                override fun onItemSelected(parent: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                    val student = students[pos]
                    currentStudentId = student.studentId
                    viewModel.selectStudent(student.studentId)
                }
            }
        }

        viewModel.termsForSelected.observe(viewLifecycleOwner) { terms ->
            termAdapter.submitList(terms)
        }

        viewModel.overallGpa.observe(viewLifecycleOwner) { gpa ->
            gpaTextView.text = "GPA: ${"%.2f".format(gpa)}"
        }
    }
}
