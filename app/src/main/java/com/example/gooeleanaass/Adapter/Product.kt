
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gooeleanaass.R
import com.example.gooeleanaass.model.ProductMo

import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.product_det.view.*
import kotlinx.android.synthetic.main.product_item.view.*
class Product(var activity: Context?, var data: MutableList<ProductMo>, var clickListener: Product.onItemClickListener) : RecyclerView.Adapter<Product.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val product_Image  =itemView.Product_Image
        val product_name=itemView.Product_Name
        fun initialize(data:ProductMo, action:onItemClickListener){
            Picasso.get().load(data.productImage).into(product_Image)
            product_name.text = data.productName
            itemView.setOnClickListener {
                action.onItemClick(data,adapterPosition)
            }}}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Product.MyViewHolder {
        var myView: View = LayoutInflater.from(activity).inflate(R.layout.product_item,parent,false)
        val myHolder:MyViewHolder = MyViewHolder(myView)
        return myHolder}
    override fun onBindViewHolder(holder: Product.MyViewHolder, position: Int) {
        holder.initialize(data.get(position), clickListener)}
    override fun getItemCount(): Int {
        return data.size}
    interface onItemClickListener{
        fun onItemClick(data:ProductMo, position: Int)
    }}