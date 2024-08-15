package com.example.mydiary.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.mydiary.adapter.MomentAdapter
import com.example.mydiary.databinding.FragmentLyMemoriesBinding
import com.example.mydiary.model.Moment
import com.example.mydiary.roomdb.MomentDAO
import com.example.mydiary.roomdb.MomentDb
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class LyMemories : Fragment() {
    private var _binding: FragmentLyMemoriesBinding ? = null
    private val binding get() = _binding!!
    private  lateinit var db : MomentDb
    private  lateinit var momentDAO: MomentDAO
    private  val mDisposable = CompositeDisposable()
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
        mDisposable.clear()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnInsertMoment.setOnClickListener(
            {
               InsertOrShow(it,true,-1);
            }
        )
        binding.rcMemories.layoutManager = LinearLayoutManager(requireContext())
        getData()
    }
    private fun getData()
    {
        mDisposable.add(
            momentDAO.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this :: HandleResponse)
        )
    }
    private fun HandleResponse(moments : List<Moment>){

        val adapter = MomentAdapter(moments)
        binding.rcMemories.adapter = adapter
    }
    fun InsertOrShow(view: View,  process : Boolean,id: Int) //false->Show
    {
        val action = LyMemoriesDirections.actionLyMemoriesToLyMoment(process,id)
        Navigation.findNavController(view).navigate(action)
    }

}