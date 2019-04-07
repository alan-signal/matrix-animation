package com.example.matrixanimation

import android.graphics.Matrix
import android.util.Log

class AnimMatrix private constructor(from: Matrix, to: Matrix) {

    class Builder {
        private val from: Matrix = Matrix()

        fun from(from: Matrix): AnimMatrix.Builder {
            this.from.set(from)
            return this
        }

        fun previous(aMatrix: AnimMatrix?): AnimMatrix.Builder {
            if (aMatrix != null) {
                from.postConcat(aMatrix.value())
            }
            return this
        }

        fun build(to: Matrix) = AnimMatrix(from, to)
    }

    private val toCopy: Matrix = Matrix(to)

    private val a: Matrix = Matrix()
    private val temp: Matrix = Matrix()
    private val fTemp: FloatArray = FloatArray(9)

    private var canAnimate: Boolean

    var animFraction: Float = 0f

    init {
        canAnimate = toCopy.invert(a)
        a.postConcat(from)
    }

    /**
     * animValue = 0 returns to-1
     *
     * animValue = 1 returns identity
     */
    fun value(): Matrix {
        if (canAnimate) {
            Log.d("ALAN", "preAnim $a $animFraction")

            a.getValues(fTemp)

            for (i in 0 until 9) {
                fTemp[i] = animFraction * iValues[i] + (1f - animFraction) * fTemp[i]
            }

            temp.setValues(fTemp)
            Log.d("ALAN", "postAnim $a")

            return temp
        } else {
            Log.d("ALAN", "Can't invert")
            return toCopy
        }
    }

    companion object {
        private val iValues: FloatArray = FloatArray(9).apply { Matrix().getValues(this) }
    }
}
