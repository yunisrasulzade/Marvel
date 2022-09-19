package com.example.core.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.core.util.Utils.showLoadingDialog
import com.google.android.material.snackbar.Snackbar

abstract class BaseActivity<T : ViewBinding, V : BaseViewModel> :
    AppCompatActivity() {
    private var mProgressDialog: Dialog? = null
    private var mViewModel: V? = null

    abstract fun inflateLayout(layoutInflater: LayoutInflater): T

    private var _binding: T? = null
    protected val binding: T get() = requireNotNull(_binding)

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun getViewModel(): V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performDataBinding()
    }

    private fun performDataBinding() {
        _binding = inflateLayout(layoutInflater)
        setContentView(_binding?.root)

        mViewModel = if (mViewModel == null) getViewModel() else mViewModel

        getViewModel().loadingState.observe(this) { state ->
            if (state) showLoading() else hideLoading()
        }
    }

    open fun showLoading() {
        hideLoading()
        mProgressDialog = showLoadingDialog(this)
    }

    open fun hideLoading() {
        mProgressDialog?.let {
            if (it.isShowing) {
                mProgressDialog?.cancel()
            }
        }
    }

    open fun showMessage(message: String) {
        val parentLayout = findViewById<View>(android.R.id.content)
        Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG).show()
    }
}
