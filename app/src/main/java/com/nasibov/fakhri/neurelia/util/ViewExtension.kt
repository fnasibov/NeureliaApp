package com.nasibov.fakhri.neurelia.util

import android.content.ContextWrapper
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar

fun View.getParentActivity(): AppCompatActivity? {
    var context = this.context
    while (context is ContextWrapper) {
        if (context is AppCompatActivity) {
            return context
        }
        context = context.baseContext
    }
    return null
}

fun showSnackbar(@StringRes errorMessage: Int, marginBottom:Int,container: CoordinatorLayout) {
    val snackbar = Snackbar.make(container, errorMessage, Snackbar.LENGTH_SHORT)
    val snackbarView = snackbar.view
    val layoutParams = snackbarView.layoutParams as CoordinatorLayout.LayoutParams
    layoutParams.apply {
        setMargins(
                leftMargin,
                topMargin,
                rightMargin,
                bottomMargin + marginBottom
        )
    }
    snackbar.show()
}