package xyz.mayahiro.numberanimation

import android.animation.ObjectAnimator
import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import java.util.*

class CountUpTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatTextView(context, attrs, defStyleAttr) {
    private var number: Long = 0L
    private var format: String? = null

    // animation
    private var animator: ObjectAnimator? = null
    private var phase: Float = 0f
        set(value) {
            field = value

            text = if (format != null) {
                String.format(Locale.getDefault(), format!!, Math.round(number * phase.toDouble()))
            } else {
                Math.round(number * phase.toDouble()).toString()
            }
        }

    fun startAnimation() {
        animator?.let {
            if (it.isRunning) {
                it.end()
                it.cancel()
                clearAnimation()
            }
        }

        animator = ObjectAnimator.ofFloat(this, "phase", 0f, 1f).apply {
            duration = 1000L
            startDelay = 250
        }

        animator?.start()
    }

    fun setData(number: Long, format: String? = null) {
        this.number = number
        this.format = format
    }
}
