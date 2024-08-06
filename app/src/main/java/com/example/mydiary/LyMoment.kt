package com.example.mydiary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mydiary.databinding.FragmentLyMemoriesBinding
import com.example.mydiary.databinding.FragmentLyMomentBinding
import kotlinx.coroutines.selects.select

class LyMoment : Fragment() {
    private var _binding: FragmentLyMomentBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLyMomentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            ViewControl(LyMomentArgs.fromBundle(it).InsertOrShow,LyMomentArgs.fromBundle(it).Id);
        }



        binding.imgMoment.setOnClickListener { SelectImage(it) }
        binding.btnSave.setOnClickListener{ Save(it) }
        binding.btnDelete.setOnClickListener{ Delete(it) }

    }
    fun SelectImage(view: View)
    {

    }
    fun Save(view: View)
    {

    }
    fun Delete(view: View)
    {

    }
    fun ViewControl(value : Boolean, id:Int) //false - Show
    {
        if(value) ButtonState(false)
        else ButtonState(true)

    }
    fun ButtonState(value : Boolean)
    {
        binding.btnDelete.isEnabled=value
        binding.btnSave.isEnabled=!value
    }
}
