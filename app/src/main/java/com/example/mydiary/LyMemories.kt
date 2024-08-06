package com.example.mydiary

import android.app.DirectAction
import android.os.Bundle
import android.provider.ContactsContract.Directory
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavGraphNavigator
import androidx.navigation.Navigation
import com.example.mydiary.databinding.FragmentLyMemoriesBinding

class LyMemories : Fragment() {
    private var _binding: FragmentLyMemoriesBinding ? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLyMemoriesBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnInsertMoment.setOnClickListener(
            {
               InsertOrShow(it,true,-1);
            }
        )
    }

    fun InsertOrShow(view: View,  process : Boolean,id: Int) //false->Show
    {
        val action = LyMemoriesDirections.actionLyMemoriesToLyMoment(process,id)
        Navigation.findNavController(view).navigate(action)
    }

}