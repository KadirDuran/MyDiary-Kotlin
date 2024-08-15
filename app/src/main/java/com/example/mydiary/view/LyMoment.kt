package com.example.mydiary.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Path.Direction
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mydiary.adapter.MomentAdapter
import com.example.mydiary.databinding.FragmentLyMomentBinding
import com.example.mydiary.model.Moment
import com.example.mydiary.roomdb.MomentDAO
import com.example.mydiary.roomdb.MomentDb
import com.google.android.material.snackbar.Snackbar
import java.io.ByteArrayOutputStream
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable


class LyMoment : Fragment() {
    private var _binding: FragmentLyMomentBinding? = null
    private val binding get() = _binding!!
    private  lateinit var permissionLauncher : ActivityResultLauncher<String>
    private  lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private var  selectImg: Uri?=null //URL
    private var  selectBitmap: Bitmap?=null
    private var dataMoment : Moment?=null
    private  val  mDisposable= CompositeDisposable()
    private  lateinit var db : MomentDb
    private  lateinit var momentDAO: MomentDAO


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLauncher()
        db = Room.databaseBuilder(requireContext(),MomentDb::class.java,"Moment").build()
        momentDAO = db.MomentDAO()
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
        mDisposable.clear()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imgMoment.setOnClickListener { SelectImage(it) }
        binding.btnSave.setOnClickListener{ Save(it) }
        binding.btnDelete.setOnClickListener{ Delete(it) }
        arguments?.let {
            ViewControl(LyMomentArgs.fromBundle(it).InsertOrShow,LyMomentArgs.fromBundle(it).Id);
        }





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
        val title = binding.txtTitle.text.toString()
        val moment = binding.txtMoment.text.toString()
        if(selectBitmap != null)
        {
            val smallBitmap = SmallBitmapCreate(selectBitmap!!,300)
            val outputStream = ByteArrayOutputStream()
            smallBitmap.compress(Bitmap.CompressFormat.PNG,50,outputStream)
            val byteArray = outputStream.toByteArray()
            val moment = Moment(title, moment,byteArray)
            //Rxjava
            mDisposable.add(
                momentDAO.insert(moment)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::HandleResponse))
        }
    }

    private fun HandleResponse()
    {
        val action = LyMomentDirections.actionLyMomentToLyMemories()
        Navigation.findNavController(requireView()).navigate(action)
    }
    fun Delete(view: View)
    {
        if(dataMoment!=null)
        {
            mDisposable.add(
                momentDAO.delete(dataMoment!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::HandleResponse))
        }


    }

    private fun SmallBitmapCreate(img : Bitmap,maxSize : Int) : Bitmap
    {
        var width = img.width
        var height  = img.height
        val bitmapRate : Double=width.toDouble()/ height.toDouble()

        if(bitmapRate>1)
        {//Yatay
            width = maxSize
            val minH=width/bitmapRate
            height = minH.toInt()

        }else
        {
            height = maxSize
            val minW=height*bitmapRate
            width = minW.toInt()
        }

        return  Bitmap.createScaledBitmap(img,width,height,true)
    }
    fun ViewControl(value : Boolean, id:Int) //false - Show
    {
        if(value)
        {//Ekleme işlemi
            ButtonState(false)
        }
        else {
            mDisposable.add(
                momentDAO.getOneMoment(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleResponse)
            )
            ButtonState(true)
        }

    }
    private fun handleResponse(moment: Moment)
    {
        dataMoment=moment
        binding.txtMoment.setText(moment.momenttxt)
        binding.txtTitle.setText(moment.title)
        val bitmap = BitmapFactory.decodeByteArray(moment.image,0,moment.image.size)
        binding.imgMoment.setImageBitmap(bitmap)
    }
    fun ButtonState(value : Boolean)
    {
        binding.btnDelete.isEnabled=value
        binding.btnSave.isEnabled=!value
    }
}
