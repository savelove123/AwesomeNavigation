package com.felix.android.common

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter

object BindingUtils {

    @BindingAdapter( "is_visible")
    @JvmStatic
    fun setVisible(view: View, visible:Boolean ){
        view.visibility =if( visible ) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter( "addTextChangedListener")
    fun addTextChangedListener(view: EditText, textWatcher: TextWatcher ){
        view.addTextChangedListener( textWatcher )
    }

    @JvmStatic
    @BindingAdapter("addActionDoneListener")
    fun addActionDoneListener( view:EditText, onEditorActionListener: TextView.OnEditorActionListener){
        view.setOnEditorActionListener( onEditorActionListener )
    }

}

fun createActionDoneListener(
    actionDone:()->Unit = {}
) = TextView.OnEditorActionListener { _, actionId, _ ->
    if (actionId == EditorInfo.IME_ACTION_DONE) {
        actionDone()
        true
    } else {
        false
    }
}


fun createTextWatcher(
    beforeTextChanged: (s: CharSequence?, start: Int, count: Int, after: Int) -> Unit = { _, _, _, _ -> },
    onTextChanged: (s: CharSequence?, start: Int, before: Int, count: Int) -> Unit = { _, _, _, _ -> },
    afterTextChanged: (s: Editable?) -> Unit = {}
    ) = object : TextWatcher {


    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        beforeTextChanged(s, start, count, after)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        onTextChanged(s, start, before, count)
    }
    override fun afterTextChanged(s: Editable?) {
        afterTextChanged(s)
    }

}