package com.example.upload_multiple_images

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity() {


    lateinit var button: Button
    lateinit var button_upload: Button
    lateinit var textView: TextView
    var string:String=""
    lateinit var bitmap:Bitmap

    var images: ArrayList<Bitmap> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.button_select)
        textView = findViewById(R.id.textView)
        button_upload = findViewById(R.id.button_upload)

        button.setOnClickListener {


            val intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
        }


        button_upload.setOnClickListener {

            if (!images.isEmpty()) {
                string=""
                for (uri in images) {
                    string=string+uri.toString()+"\n"

//                    upload here images
                    upload_image(uri)

                }


                textView.setText(string)
            }else{
                Toast.makeText(this,"No images Selected.", Toast.LENGTH_SHORT).show()
            }

        }


    }

    fun upload_image(bitmapp: Bitmap) {


        val byteArrayOutputStreamObject: ByteArrayOutputStream

        byteArrayOutputStreamObject = ByteArrayOutputStream()
        bitmapp.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject)
        val byteArrayVar: ByteArray = byteArrayOutputStreamObject.toByteArray()
        val image: String = Base64.encodeToString(byteArrayVar, Base64.DEFAULT)

        try {
            val retrofit = Retrofithelper.getInstance()
            val studentService = retrofit.create(ImageService::class.java)

            val call = studentService.upload_image("dispach","11111",image)
            call.enqueue(object : Callback<Image_Response> {

                override fun onResponse(call: Call<Image_Response>, response: Response<Image_Response>) {

                    if(response.isSuccessful){
                        try {
                            Toast.makeText(this@MainActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                            Log.d("sr", response.body()?.message.toString())
                        }catch (e:Exception){
                            Log.d("sr", response.body()?.error.toString())
                        }

                    }else{
                        Toast.makeText(this@MainActivity, response.code(), Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onFailure(call: Call<Image_Response>, t: Throwable) {
                    Toast.makeText(this@MainActivity,t.toString(), Toast.LENGTH_SHORT).show()
                    Log.d("sr", t.toString())
                }
            })
        } catch (e: Exception) {
            Toast.makeText(this@MainActivity, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        // When an Image is picked
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                images.clear()
                textView.setText("")
                if (data == null) {
                    // something is wrong
                    Toast.makeText(this, "data not collected", Toast.LENGTH_SHORT).show()
                }

                val clipData = data?.clipData
                if (clipData != null) { // handle multiple photo

                    for (i in 0 until clipData.itemCount) {
                        val uri = clipData.getItemAt(i).uri
                        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                        images.add(bitmap)

                    }
                } else { // handle single photo
                    val uri = data?.data
//                    importPhoto(uri)
                }
            }
        }
    }


}