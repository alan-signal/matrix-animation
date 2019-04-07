package com.example.matrixanimation

import android.graphics.Matrix
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun a(view: View) {
        val mv: MyView = findViewById(R.id.myView)
        mv.animateAppend(Matrix().apply {
            postRotate(45f)
            //postRotate(50f)
            postTranslate(-0.2f, 0f)
            //postScale(1.1f, 1.1f)
        })
    }

    fun b(view: View) {
        val mv: MyView = findViewById(R.id.myView)
        mv.animateSet(Matrix())
    }
}
