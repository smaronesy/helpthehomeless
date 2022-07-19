package app.htheh.helpthehomeless.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import app.htheh.helpthehomeless.R

@BindingAdapter("statusIcon")
fun bindHomelessStatusImage(imageView: ImageView, needsHome: Boolean) {
    if (needsHome) {
        imageView.setImageResource(R.drawable.ic_homeless_emergency)
    } else {
        imageView.setImageResource(R.drawable.ic_has_home)
    }
}

@BindingAdapter("statusText")
fun bindHomelessStatusText(textView: TextView, needsHome: Boolean) {
    if (needsHome) {
        textView.text = "In need of shelter"
    } else {
        textView.text = "already has shelter"
    }
}

@BindingAdapter("firstName")
fun bindHomelessFirstName(textView: TextView, fn: String){
    textView.text = fn
}

@BindingAdapter("lastName")
fun bindHomelessLastName(textView: TextView, ln: String){
    textView.text = ln
}

@BindingAdapter("email")
fun bindHomelessEmail(textView: TextView, email: String){
    textView.text = email
}

@BindingAdapter("phone")
fun bindHomelessPhone(textView: TextView, phone: String){
    textView.text = phone
}

@BindingAdapter("walkScore")
fun bindHomelessAreaWalkscore(textView: TextView, walkScore: String){
    textView.text = walkScore
}