package com.example.flickrbrowserappxml_optional

import android.app.Activity
import android.content.Context
import android.inputmethodservice.Keyboard
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL



class MainActivity : AppCompatActivity() {
    lateinit var ll:LinearLayout
    lateinit var btn:Button
    val parser = XMLParser()
    lateinit var rv:RecyclerView
    lateinit var et:EditText
    lateinit var al:MutableList<Item>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ll = findViewById(R.id.ll)
        btn = findViewById(R.id.button)
        et = findViewById(R.id.editTextTextPersonName)
        al = arrayListOf()
        rv = findViewById(R.id.rv)
        btn.setOnClickListener {

            al.clear()
            rv.adapter?.notifyDataSetChanged()
            APIrequest()
            it.hideKeyboard()


        }

        imageView2.setOnClickListener { hidhiddenimg() }


    }


    fun APIrequest() {
           CoroutineScope(IO).launch {
               var data = async { CheckURL() }.await()
               if (data.isNotEmpty()) {
                   addToUI(data)
               }
           }
       }

    fun CheckURL():String{
               var url = ""
               var userTags = et.text.toString()
               try {
                   url = URL(Constant.url(userTags)).readText(Charsets.UTF_8)
               }catch (e:Exception){
                   Toast.makeText(this,"URL error",Toast.LENGTH_SHORT).show()
               }
               return url
           }


    suspend fun addToUI(data:String){
        withContext(Main){
            var myData = parser.parse(data.byteInputStream())
            al = myData
            Log.d("alal","$al")
            rv.adapter?.notifyDataSetChanged()
            rv.adapter = MyAdapter(this@MainActivity,al)
            rv.layoutManager = LinearLayoutManager(this@MainActivity)
            et.text.clear()
            et.clearFocus()
        }
       }


    fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }



    fun showimg(imm:String?){
        imageView2.visibility = View.VISIBLE
        Glide.with(this).load(imm).into(imageView2)
        ll.visibility = View.GONE
        rv.visibility = View.GONE

    }
    fun hidhiddenimg(){
        imageView2.visibility = View.GONE
        ll.visibility = View.VISIBLE
        rv.visibility = View.VISIBLE

    }
}