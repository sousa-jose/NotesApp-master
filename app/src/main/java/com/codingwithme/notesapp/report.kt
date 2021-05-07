package com.codingwithme.notesapp

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.codingwithme.notesapp.Common.Common
import com.codingwithme.notesapp.Model.APIResponse
import com.codingwithme.notesapp.Model.User
import com.codingwithme.notesapp.Model.modelReport
import com.codingwithme.notesapp.Model.reportOutput
import com.codingwithme.notesapp.Remote.Api
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_create_note.*
import kotlinx.android.synthetic.main.report.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.*
import java.io.*

lateinit var longitude : String
lateinit var latitude : String
lateinit var address : String
lateinit var id_user : String

class report: AppCompatActivity() {

    internal lateinit var mService: Api

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.report)
        mService = Common.api

        //var id_user : String? = null

        val sharedpreferences1 = getSharedPreferences("localizacao", Context.MODE_PRIVATE)
        longitude = sharedpreferences1.getString("longitude", "100").toString()
        latitude= sharedpreferences1.getString("latitude", "100").toString()
        address = sharedpreferences1.getString("morada", "Rua candido de olivera").toString()
        id_user = intent.getStringExtra("id").toString()
        Log.d("ze", id_user)
        findViewById<TextView>(R.id.morada).text = address

        val reportar = findViewById<Button>(R.id.botaoRepor)


        reportar.setOnClickListener {
           reportButton()
        }



        img_pick_btn.setOnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED){
                    //permissao negada
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);

                    requestPermissions(permissions, PERMISSION_CODE);
                }
                else{
                    //permissao dada
                    pickImagemFromGallery()
                }
            }
            else{
                pickImagemFromGallery()
            }
        }

    }

    private fun pickImagemFromGallery(){

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)

    }

    companion object{

        private val IMAGE_PICK_CODE = 1000;
        private val PERMISSION_CODE = 1001;

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        pickImagemFromGallery()
                }
                else{
                    Toast.makeText(this, "Não tem permissão para aceder a galeria", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            imageView.setImageURI(data?.data)
        }
    }

    private fun convertBitmapToFile(fileName: String, bitmap: Bitmap): File {
        //create a file to write bitmap data
        val file = File(this@report.cacheDir, fileName)
        file.createNewFile()

        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos)
        val bitMapData = bos.toByteArray()

        //write the bytes in file
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            fos?.write(bitMapData)
            fos?.flush()
            fos?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }


    private fun adicionarReport(latitude:Double, longitude:Double, descricao:String, morada:String, tipo:String, id_user:String ){

        val imgBitmap: Bitmap = findViewById<ImageView>(R.id.imageView).drawable.toBitmap()
        val imageFile: File = convertBitmapToFile("file", imgBitmap)
        val imgFileRequest: RequestBody = RequestBody.create(MediaType.parse("image/*"), imageFile)
        val foto: MultipartBody.Part = MultipartBody.Part.createFormData("file", imageFile.name, imgFileRequest)

        val repo2 =  RequestBody.create(MediaType.parse("text/plain"), descricao)
        val repo6 = RequestBody.create(MediaType.parse("text/plain"), latitude.toString())
        val repo7 = RequestBody.create(MediaType.parse("text/plain"), longitude.toString())
        val repo3 =  RequestBody.create(MediaType.parse("text/plain"), morada)
        val repo4 =  RequestBody.create(MediaType.parse("text/plain"), tipo)
        val repo8 =  RequestBody.create(MediaType.parse("text/plain"), id_user)

        val call = mService.adicionarReport(foto , repo6 ,repo7, repo2, repo3, repo4, repo8)
        call.enqueue(object: Callback<reportOutput> {
                override fun onFailure(call: Call<reportOutput>, t: Throwable) {
                    Toast.makeText(this@report,"Ponto Registado!", Toast.LENGTH_SHORT).show()
                    Log.d("CARREGADO", t.toString())
                }
                override fun onResponse(call: Call<reportOutput>, response: Response<reportOutput>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@report, response.body().toString(), Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    }
                }

            })
    }

    private fun reportButton(){

        val descrever = findViewById<EditText>(R.id.desc).text.toString()
        val type = findViewById<EditText>(R.id.tipo).text.toString()

        Log.d("ze", id_user)
        adicionarReport( latitude!!.toDouble(), longitude!!.toDouble(),  descrever, address!!, type, id_user)
    }
}