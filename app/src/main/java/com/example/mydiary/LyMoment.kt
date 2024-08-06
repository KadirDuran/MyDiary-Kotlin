package com.example.mydiary

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mydiary.databinding.FragmentLyMemoriesBinding
import com.example.mydiary.databinding.FragmentLyMomentBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.selects.select

class LyMoment : Fragment() {
    private var _binding: FragmentLyMomentBinding? = null
    private val binding get() = _binding!!
    private  lateinit var permissionLauncher : ActivityResultLauncher<String>
    private  lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private var  selectImg: Uri?=null //URL
    private var  selectBitmap: Bitmap?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLauncher()
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
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        {
            if(ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED)
            {
                if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),Manifest.permission.READ_MEDIA_IMAGES))
                {
                    Snackbar.make(view,"Görsel yüklemek için izin vermeniz gerekmektedir.",Snackbar.LENGTH_INDEFINITE).setAction(
                        "İzin Ver",View.OnClickListener {
                            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                        }
                    ).show()
                }else
                {
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }
            }else
            {
                val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intent)
            }

        }else
        {
        if(ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                Snackbar.make(view,"Görsel yüklemek için izin vermeniz gerekmektedir.",Snackbar.LENGTH_INDEFINITE).setAction(
                    "İzin Ver",View.OnClickListener {
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                ).show()
            }else
            {
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }else
        {
            val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intent)
        }
        }
    }
    fun registerLauncher()
    {
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        {
            if(it.resultCode == AppCompatActivity.RESULT_OK)
            {
               val intentData =  it.data
                if(intentData != null)
                {
                    selectImg = intentData.data

                    try {
                        if(Build.VERSION.SDK_INT>=28)
                        {
                            val source = ImageDecoder.createSource(requireActivity().contentResolver,selectImg!!)
                            selectBitmap = ImageDecoder.decodeBitmap(source)
                            binding.imgMoment.setImageBitmap(selectBitmap)

                        }else
                        {
                            selectBitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, selectImg)
                            binding.imgMoment.setImageBitmap(selectBitmap)
                        }
                    }catch (e : Exception)
                    {
                        println(e.localizedMessage)
                    }

                }
            }
        }
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission())
        {
          if(it)
            {
              val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intent)
            }else
          {
             Toast.makeText(requireContext(),"İzin verilmedi.",Toast.LENGTH_LONG).show()

          }
        }
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
