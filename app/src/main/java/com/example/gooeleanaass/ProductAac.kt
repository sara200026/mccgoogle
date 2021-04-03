package com.example.gooeleanaass

import Product
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gooeleanaass.model.ProductMo
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.product.*
import java.util.*

class ProductAac  : AppCompatActivity(), Product.onItemClickListener {
    private var progressDialog: ProgressDialog?=null
    private var firebaseAnalytics: FirebaseAnalytics? = null
    lateinit var db: FirebaseFirestore
    var sTime : Long = 0
    var eTime : Long = 0
    var totalTime : Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product)
        sTime = Calendar.getInstance().timeInMillis
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        trackScreen("Product")
        showDialog()
        var categoryName = intent.getStringExtra("categoryName")
        getAllProducts("$categoryName")
    }
    override fun onItemClick(data: ProductMo, position: Int) {
        selectContent(data.id!!,data.productName!!,data.productImage!!)
        eTime = Calendar.getInstance().timeInMillis
        totalTime = eTime - sTime
        val minutes: Long = totalTime / 1000 / 60
        val seconds = (totalTime / 1000 % 60)
        timeInScreen("$minutes m $seconds s","sara","ProductAac")
        var i = Intent(this,ProductDet::class.java)
        i.putExtra("proId",data.id)
        i.putExtra("prodName",data.productName)
        i.putExtra("prodImage",data.productImage)
        i.putExtra("prodPrice",data.price)
        i.putExtra("prodDescription",data.description)
        i.putExtra("prodCategory",data.categoryName)
        startActivity(i)
    }

    private fun getAllProducts(catName:String){
        val dataProduct = mutableListOf<ProductMo>()
        db.collection("product").whereEqualTo("categoryName",catName)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val id = document.id
                        val data = document.data
                        val name = data["name"] as String?
                        val price = data["price"] as String?
                        val image = data["image"] as String?
                        val description = data["description"] as String?
                        val categoryName = data["categoryName"] as String?
                        dataProduct.add(
                            ProductMo(id,image,price,name,description,categoryName)
                        )
                    }
                    recProduct?.layoutManager =
                        LinearLayoutManager(this, RecyclerView.VERTICAL, false)
                    recProduct.setHasFixedSize(true)
                    val productAdapter = Product(this, dataProduct, this)
                    recProduct.adapter = productAdapter
                }
                hideDialog()
            }
    }
    private fun showDialog() {
        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Products")
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
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "products")
        firebaseAnalytics!!.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }

    fun timeInScreen(time: String, userId:String, pageName:String){

        val time= hashMapOf("time" to time,"userId" to userId,"pageName" to pageName)
        db.collection("Time")
            .add(time)
            .addOnSuccessListener {documentReference ->
                Log.e("TAG","successfully")
            }
            .addOnFailureListener {exception ->
                Log.e("TAG", exception.message.toString())
            }
    }



}