package com.example.mydiary.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.room.Room
import com.example.mydiary.databinding.FragmentLyMemoriesBinding
import com.example.mydiary.roomdb.MomentDAO
import com.example.mydiary.roomdb.MomentDb

class LyMemories : Fragment() {
    private var _binding: FragmentLyMemoriesBinding ? = null
    private val binding get() = _binding!!
    private  lateinit var db : MomentDb
    private  lateinit var momentDAO: MomentDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Room.databaseBuilder(requireContext(),MomentDb::class.java,"Moment").build()
        momentDAO = db.MomentDAO()

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