package com.example.firebase.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firebase.R
import com.example.firebase.model.Paylasim


class DusunceAdapter(private val dataSet: ArrayList<Paylasim>) :
    RecyclerView.Adapter<DusunceAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val kullaniciAdi: TextView
        val yorum: TextView

        init {
            // Define click listener for the ViewHolder's View
            kullaniciAdi = view.findViewById(R.id.kullanici_adi)
            yorum = view.findViewById(R.id.yorum)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.reyclerview_row, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.kullaniciAdi.text = dataSet[position].kullaniciAdi
        viewHolder.yorum.text = dataSet[position].kullaniciYorum
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
