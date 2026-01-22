package re.netology.statsview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import re.netology.statsview.ui.StatsView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val view = findViewById<StatsView>(R.id.statsView)
        val label = findViewById<TextView>(R.id.label)

        view.data = listOf(
            500F,
            500F,
            500F,
            500F,
        )


        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 2000
            addUpdateListener {
                val value = it.animatedValue as Float
                view.progress = value
                view.rotationAngle = 360f * value
            }

            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    label.text = "OnAnimationStart"
                }

                override fun onAnimationEnd(animation: Animator) {
                    label.text = "OnAnimationEnd"
                }
            })
        }.start()
    }
}