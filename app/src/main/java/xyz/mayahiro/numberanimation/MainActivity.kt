package xyz.mayahiro.numberanimation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<AppCompatButton>(R.id.start_button).setOnClickListener {
            animate()
        }
    }

    private fun animate() {
        val countUpTextView1 = findViewById<CountUpTextView>(R.id.count_up_text_view1)
        countUpTextView1.setData(Long.MAX_VALUE)
        countUpTextView1.startAnimation()

        val countUpTextView2 = findViewById<CountUpTextView>(R.id.count_up_text_view2)
        countUpTextView2.setData(Long.MAX_VALUE, "$%,d")
        countUpTextView2.startAnimation()
    }
}
