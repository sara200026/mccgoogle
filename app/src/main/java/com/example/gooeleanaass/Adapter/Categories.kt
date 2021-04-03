package com.example.gooeleanaass.Adapter


import CategoryMo
import android.content.Context
import android.icu.util.ULocale
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gooeleanaass.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.category_item.view.*
import java.util.*
class Categories(var activity: Context?, var data: MutableList<CategoryMo>, var clickListener: onItemClickListener): RecyclerView.Adapter<Categories.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val category_Name=itemView.Category_Name
        val category_Image  =itemView.Category_Image
        fun initialize(data: CategoryMo, action:onItemClickListener){
            Picasso.get().load(data.categoryImage).into(category_Image)
            category_Name.text = data.categoryName
            itemView.setOnClickListener {
                action.onItemClick(data, adapterPosition)
            }}}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var myView: View = LayoutInflater.from(activity).inflate(R.layout.category_item,parent,false)
        val myHolder:MyViewHolder = MyViewHolder(myView)
        return myHolder
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.initialize(data.get(position), clickListener)
    }
    interface onItemClickListener{
        fun onItemClick(data:CategoryMo, position: Int)
    }
    override fun getItemCount(): Int {
        return data.size
    }
}