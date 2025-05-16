package com.example.lab4.ui.addclass

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.lab4.R

class AddClassFragment : Fragment(R.layout.fragment_add_class) {
    private lateinit var viewModel: AddClassViewModel
    private lateinit var courseSpinner: Spinner
    private lateinit var gradeSpinner: Spinner
    private lateinit var addButton: Button
    private lateinit var studentNameTv: TextView
    private lateinit var termNameTv: TextView

    private val studentId by lazy { requireActivity().intent.getStringExtra("studentId")!! }
    private val termId    by lazy { requireActivity().intent.getStringExtra("termId")!! }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        studentNameTv  = view.findViewById(R.id.studentNameTextView)
        termNameTv     = view.findViewById(R.id.termNameTextView)
        courseSpinner  = view.findViewById(R.id.courseSpinner)
        gradeSpinner   = view.findViewById(R.id.gradeSpinner)
        addButton      = view.findViewById(R.id.addClassButton)

        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(AddClassViewModel::class.java)

        viewModel.courses.observe(viewLifecycleOwner) { courses ->
            courseSpinner.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                courses.map { "${it.catalogId} — ${it.description}" }
            ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
        }

        val grades = listOf("A","AB","B","BC","C","CD","D","F")
        gradeSpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            grades
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        viewModel.students.observe(viewLifecycleOwner) { list ->
            studentNameTv.text = viewModel.studentName(list, studentId)
        }

        viewModel.terms.observe(viewLifecycleOwner) { list ->
            termNameTv.text = viewModel.termName(list, termId)
        }
        addButton.setOnClickListener {
            val selectedCourse = viewModel.courses.value!![courseSpinner.selectedItemPosition]
            val selectedGrade  = grades[gradeSpinner.selectedItemPosition]
            viewModel.addOrUpdateTranscript(studentId, termId, selectedCourse.courseDetailId, selectedGrade)
            requireActivity().finish()
        }
    }
}