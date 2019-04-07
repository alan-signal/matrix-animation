package com.example.matrixanimation

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator

class MyView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    init {
        setWillNotDraw(false)
    }

    private val paint = Paint().also {
        it.color = 0xff00ff00.toInt()
    }

    private val vpMatrix = Matrix()
    private val xMatrix = Matrix()
    private val userMatrix = Matrix()

    private var anim: ValueAnimator? = null

    private var aMatrix: AnimMatrix? = null

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        vpMatrix.reset()
        xMatrix.reset()

        vpMatrix.postScale(1f / width, 1f / height)
        // now we're in a VP of 1x1
        vpMatrix.postTranslate(-0.5f, -0.5f)
        // now we're in a VP of -0.5..0.5, -0.5..0.5

        //xMatrix.postRotate(30f)
        xMatrix.postScale(0.3f, 0.3f)
        xMatrix.postTranslate(0.2f, 0f)

//        val temp = Matrix(xMatrix)
//
//        xMatrix.postRotate(50f)
//        xMatrix.postTranslate(-0.2f, 0f)
//        xMatrix.postScale(5f, 5f)
//
//        if (anim == null) {
//            aMatrix = AnimMatrix(temp, xMatrix)
//            anim = ValueAnimator.ofFloat(0f, 1f)
//                .apply {
//                    interpolator = LinearInterpolator()
//                    duration = 5000
//                    addUpdateListener {
//                        aMatrix!!.animFraction = it.animatedFraction
//                        invalidate()
//                    }
//                    start()
//                }
//        }

        //vpMatrix.postConcat(xMatrix)
        vpMatrix.postConcat(userMatrix)
        aMatrix?.let {
            vpMatrix.postConcat(it.value())
        }

        // zoom up from centre, keep square
        vpMatrix.postScale(width.toFloat(), width.toFloat())
        vpMatrix.postTranslate(width.toFloat() * 0.5f, height.toFloat() * 0.5f)

        canvas.matrix = vpMatrix
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }

    fun animateAppend(new: Matrix) {
        val builder = AnimMatrix.Builder()
            .from(userMatrix)
            .previous(aMatrix)

        userMatrix.postConcat(new)

        animate(builder)
    }

    fun animateSet(new: Matrix) {
        val builder = AnimMatrix.Builder()
            .from(userMatrix)
            .previous(aMatrix)

        userMatrix.set(new)

        animate(builder)
    }

    private fun animate(builder: AnimMatrix.Builder) {
        aMatrix = builder.build(userMatrix)
        anim = ValueAnimator.ofFloat(0f, 1f)
            .apply {
                interpolator = DecelerateInterpolator()
                duration = 750
                addUpdateListener {
                    aMatrix!!.animFraction = it.animatedFraction
                    invalidate()
                }
                start()
            }
    }
}
