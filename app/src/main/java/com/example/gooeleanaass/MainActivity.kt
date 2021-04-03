package com.example.gooeleanaass

import CategoryMo
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gooeleanaass.Adapter.Categories
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.product.*
import kotlinx.android.synthetic.main.product.recProduct
import java.util.*

class MainActivity : AppCompatActivity() , Categories.onItemClickListener {
    private var progressDialog: ProgressDialog?=null
    private var firebaseAnalytics: FirebaseAnalytics? = null
    lateinit var db: FirebaseFirestore
    var sTime : Long = 0
    var eTime : Long = 0
    var totalTime : Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = Firebase.firestore

        sTime = Calendar.getInstance().timeInMillis
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        trackScreen("category")
        showDialog()
        getAllCategories()
    }
    override fun onItemClick(data: CategoryMo, position: Int) {
        selectContent(data.id,data.categoryName!!,data.categoryImage!!)
        eTime = Calendar.getInstance().timeInMillis
        totalTime = eTime - sTime
        val minutes: Long = totalTime / 1000 / 60
        val seconds = (totalTime / 1000 % 60)
        timeInScreen("$minutes m $seconds s","sara","MainActivity")
        var i = Intent(this,ProductAac::class.java)
        i.putExtra("id",data.id)
        i.putExtra("categoryName",data.categoryName)
        i.putExtra("categoryImage",data.categoryImage)
        startActivity(i)
    }
    private fun getAllCategories(){
        val categoryList= mutableListOf<CategoryMo>()
        db.collection("Category")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val id = document.id
                        val data = document.data
                        val myCategoryName = data["category_name"] as String?
                        val myCategoryImage = data["category_image"] as String?
                        categoryList.add(CategoryMo(id, myCategoryImage, myCategoryName))
                    }
                    recCategory?.layoutManager =
                        LinearLayoutManager(this, RecyclerView.VERTICAL, false)
                    recCategory.setHasFixedSize(true)
                    val categoriesAdapter = Categories(this, categoryList, this)
                    recCategory.adapter = categoriesAdapter
                }
                hideDialog()
            }}

    private fun showDialog() {
        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Categories")
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
    }

    private fun hideDialog(){
        if(progressDialog!!.isShowing){
            progressDialog!!.dismiss()
        }
    }


    private fun selectContent(id:String, name:String, contentType:String){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, contentType)
        firebaseAnalytics!!.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }
    private fun trackScreen(screenName:String){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "MainActivity")
        firebaseAnalytics!!.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }
    fun timeInScreen(time: String, userId:String, pageName:String){
        val time= hashMapOf("time" to time,"userId" to userId,"pageName" to pageName)
        db.collection("Time")
            .add(time)
            .addOnSuccessListener {documentReference ->
                Log.e("TAG","Successfully")
            }
            .addOnFailureListener {exception ->
                Log.e("TAG", exception.message.toString())
            }}}