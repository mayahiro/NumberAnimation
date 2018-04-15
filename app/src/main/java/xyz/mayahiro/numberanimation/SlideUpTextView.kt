package xyz.mayahiro.numberanimation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.animation.OvershootInterpolator
import java.util.*

class SlideUpTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    // draw
    private val density = context.resources.displayMetrics.density
    private val paint = Paint().apply {
        textAlign = Paint.Align.RIGHT
        isAntiAlias = true
    }
    private val rect = Rect()
    private val baseTextSize: Float
    private var fixTextSize = false
    private var textRectBottom = 0

    // text
    private var text = ""
    private var textLength = 0

    // animation
    private var animator: ObjectAnimator? = null
    private val phaseDuration: Int
    private var count = 0
    private var phase = 0f
        set(value) {
            field = value
            invalidate()
        }

    init {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.SlideUpTextView, 0, 0)

        try {
            baseTextSize = a.getDimensionPixelSize(R.styleable.SlideUpTextView_android_textSize, Math.round(120 * density)).toFloat()
            phaseDuration = a.getInt(R.styleable.SlideUpTextView_phaseDuration, 250)
        } finally {
            a.recycle()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        if (TextUtils.isEmpty(text)) {
            return
        }

        canvas?.let {
            val width = canvas.width
            val height = canvas.height

            while (!fixTextSize) {
                paint.getTextBounds(text, 0, textLength, rect)
                if (rect.width() <= width && rect.height() * 1.4 <= height) {
                    fixTextSize = true
                    textRectBottom = rect.bottom
                } else {
                    paint.textSize -= 0.5f
                }
            }

            val subText = text.substring(textLength - count, textLength)
            canvas.drawText(subText, width.toFloat(), height.toFloat() - textRectBottom, paint)

            // slide up
            if (textLength > count) {
                val char = text[textLength - count - 1]
                paint.getTextBounds(subText, 0, subText.length, rect)
                canvas.drawText(char.toString(), width - rect.width().toFloat(), height + height * phase - textRectBottom, paint)
            }
        }
        super.onDraw(canvas)
    }

    fun startAnimation() {
        animator?.let {
            if (it.isRunning) {
                it.end()
                it.cancel()
                clearAnimation()
            }
        }

        animator = ObjectAnimator.ofFloat(this, "phase", 1f, 0f).apply {
            duration = phaseDuration.toLong()
            interpolator = OvershootInterpolator()
            repeatCount = textLength
        }

        animator?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator?) {
                count = 0
            }

            override fun onAnimationRepeat(p0: Animator?) {
                count++
            }

            override fun onAnimationEnd(p0: Animator?) {
                // noting
            }

            override fun onAnimationCancel(p0: Animator?) {
                // noting
            }
        })

        animator?.start()
    }

    fun setData(number: Long, format: String? = null) {
        text = if (format != null) {
            String.format(Locale.getDefault(), format, number)
        } else {
            number.toString()
        }

        textLength = text.length

        paint.textSize = baseTextSize
        fixTextSize = false
    }
}
