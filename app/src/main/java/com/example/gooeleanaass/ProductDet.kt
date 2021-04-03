package com.example.gooeleanaass

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.product_det.*
import java.util.*

class ProductDet : AppCompatActivity() {


    lateinit var db: FirebaseFirestore
    private var firebaseAnalytics: FirebaseAnalytics? = null
    var sTime : Long = 0
    var eTime : Long = 0
    var totalTime : Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_det)
        sTime = Calendar.getInstance().timeInMillis
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        trackScreen("ProductDet")
        val p_Name = intent.getStringExtra("prodName")
        val p_Image = intent.getStringExtra("prodImage")
        val p_Price = intent.getStringExtra("prodPrice")
        val p_Description = intent.getStringExtra("prodDescription")
        Picasso.get().load(p_Image).into(product_image)
        product_name.text = p_Name
        product_price.text = p_Price.toString()
        product_description.text = p_Description
    }
    private fun trackScreen(screenName:String){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "product_Det")
        firebaseAnalytics!!.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }
    fun timeScreen(time: String, userId:String, pageName:String){
        val time= hashMapOf("time" to time,"userId" to userId,"pageName" to pageName)
        db.collection("Time")
            .add(time)
            .addOnSuccessListener {
                Log.e("TAG", "successfully")
            }
            .addOnFailureListener {
                Log.e("TAG", "failuer")
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        eTime = Calendar.getInstance().timeInMillis
        totalTime = eTime - sTime

        val minutes: Long = totalTime / 1000 / 60
        val seconds = (totalTime / 1000 % 60)
        timeScreen("$minutes m $seconds s","sara","ProductDet")
    }
}
