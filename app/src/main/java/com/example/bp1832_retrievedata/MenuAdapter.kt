package com.example.bp1832_retrievedata

import android.graphics.Bitmap
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bp1832_retrievedata.model.MenuModel


class MenuAdapter( private val data: ArrayList<MenuModel> ):
    RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    override fun getItemCount(): Int {
        return  data.size
    }

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): MenuViewHolder {
        val layoutInflater = LayoutInflater.from( parent?.context )
        val cellForRow     = layoutInflater.inflate( R.layout.cardview_menu, parent, false )

        return MenuViewHolder( cellForRow )
    }

    override fun onBindViewHolder( holder: MenuViewHolder, position: Int ) {
        holder.bind( data[position] )
    }

    /** Untuk Menentukan Palette yang Digunakan untuk Menampilkan Data **/
    inner class MenuViewHolder( v:View ):RecyclerView.ViewHolder( v ) {
        val textNama: TextView
        val textHarga: TextView
        val imageMenu: ImageView

        init {
            textNama = v.findViewById(R.id.textNamaMenu)
            textHarga = v.findViewById(R.id.textHargaMenu)
            imageMenu = v.findViewById(R.id.imageMenu)
        }

        fun bind(data: MenuModel) {
            val nama: String = data.name
            val harga: Int = data.price
            val gambar: Bitmap = data.image

            textNama.text = nama
            textHarga.text = harga.toString()
            imageMenu.setImageBitmap(gambar)
        }
    }
}