package com.samet.kotlinartbook

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PathPermission
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.IntentCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.samet.kotlinartbook.databinding.ActivityArtBinding
import java.io.ByteArrayOutputStream

class ArtActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArtBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    var selectedBitmap: Bitmap? = null
    private lateinit var database : SQLiteDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityArtBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        database = this.openOrCreateDatabase("Arts", MODE_PRIVATE,null)


        registerLauncher()

        val intent = intent
        val info = intent.getStringExtra("info")
        if(info == "new"){
            binding.artNameText.setText("")
            binding.artistNameText.setText("")
            binding.yearText.setText("")
            binding.button.visibility = View.VISIBLE
            binding.imageView.setImageResource(R.drawable.select)
            println("new")
        }else if (info == "old"){
            binding.button.visibility = View.INVISIBLE
            val selectedId = intent.getIntExtra("id",0)
            println(selectedId)
            val cursor = database.rawQuery("SELECT * FROM arts WHERE id = ?", arrayOf(selectedId.toString()))

            val artNameIx = cursor.getColumnIndex("artname")
            val artistNameIx = cursor.getColumnIndex("artistname")
            val yearIx = cursor.getColumnIndex("year")
            val imageIx = cursor.getColumnIndex("image")

            while (cursor.moveToNext()) {
                binding.artNameText.setText(cursor.getString(artNameIx))
                binding.artistNameText.setText(cursor.getString(artistNameIx))
                binding.yearText.setText(cursor.getString(yearIx))

                val byteArray = cursor.getBlob(imageIx)
                val bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
                binding.imageView.setImageBitmap(bitmap)

            }

            cursor.close()
        }


    }

    fun save(view : View){
        val artName = binding.artNameText.text.toString()
        val artistname = binding.artistNameText.text.toString()
        val year = binding.yearText.text.toString()
        if (selectedBitmap!=null){
            val smallBitmap = makeSmallerBitmap(selectedBitmap!!,300)
            // byte dizisi oluşturmak (image kaydetmek için)
            val outputStream = ByteArrayOutputStream()
            smallBitmap.compress(Bitmap.CompressFormat.PNG, 50,outputStream)
            val byteArray = outputStream.toByteArray()


            try {
                //val database = this.openOrCreateDatabase("Arts", MODE_PRIVATE,null)
                database.execSQL("CREATE TABLE IF NOT EXISTS arts(id INTEGER PRİMARY KEY,artname VARCHAR,artistname VARCHAR, year VARCHAR,image BLOB)")

                val sqlString = "INSERT INTO arts (artname,artistname,year,image) VALUES(?,?,?,?)"
                val statement = database.compileStatement(sqlString)
                statement.bindString(1,artName)
                statement.bindString(2,artistname)
                statement.bindString(3,year)
                statement.bindBlob(4,byteArray)
                statement.execute()



            }catch (e:Exception){
                e.printStackTrace()
            }

            val intent = Intent(this@ArtActivity,MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)// bundan önce ne kadar activity varsa kapat
            startActivity(intent)


        }
    }
    fun makeSmallerBitmap(image : Bitmap ,maxSize:Int) : Bitmap{
        var width = image.width
        var height= image.height

        var bitmapRatio :Double = width.toDouble()/height.toDouble()
        if(bitmapRatio>1){
            //yatay görsel
            width= maxSize
            val scaledHeight = width/ bitmapRatio
            height= scaledHeight.toInt()

        }else{
            height = maxSize
            val scaledWidth = height *bitmapRatio
            width =scaledWidth.toInt()
        }


        return  Bitmap.createScaledBitmap(image,width,height,true)
    }

    fun selectImage(view: View){

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU){
            //Android 33+ Read media images
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_MEDIA_IMAGES)!= PackageManager.PERMISSION_GRANTED){
                //izin verilmediyse
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_MEDIA_IMAGES)){
                    // rationale
                    Snackbar.make(view,"Permission needed for gallery",Snackbar.LENGTH_INDEFINITE).setAction("Give Permission",View.OnClickListener {
                        //izin isteme kısmı
                        permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    }).show()
                }else{
                    //izin isteme kısmı
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }


            }else{
                // galery e gidilcek
                val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)

            }
        }else
        {
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            //izin verilmediyse
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                // rationale
                Snackbar.make(view,"Permission needed for gallery",Snackbar.LENGTH_INDEFINITE).setAction("Give Permission",View.OnClickListener {
                    //izin isteme kısmı
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }).show()
            }else{
                //izin isteme kısmı
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            }else{
            // galery e gidilcek
            val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)

            }

        }


    }

    private fun registerLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
            if(result.resultCode== RESULT_OK){
               val intentFromResult = result.data
                if(intentFromResult!=null){
                    val imageData = intentFromResult.data
                    if(imageData != null){
                        try {
                            if(Build.VERSION.SDK_INT>=28){
                                val source = ImageDecoder.createSource(contentResolver,imageData)
                                selectedBitmap = ImageDecoder.decodeBitmap(source)
                                binding.imageView.setImageBitmap(selectedBitmap)
                            }else{
                                selectedBitmap = MediaStore.Images.Media.getBitmap(contentResolver,imageData)
                                binding.imageView.setImageBitmap(selectedBitmap)
                            }

                        }catch (e:Exception){
                            e.printStackTrace()
                        }
                    }
                }
            }

            }

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){result ->
            if(result){
                //permission granted (verildi)
                val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
                //activityResultLauncher --> bir sonuç için aktiviteyi başlatıyoruz
            }else{
                // permission denied
                Toast.makeText(this@ArtActivity,"permission needed",Toast.LENGTH_LONG).show()
            }
        }
    }
}