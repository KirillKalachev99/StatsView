package re.netology.statsview.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import re.netology.statsview.R
import re.netology.statsview.utils.AndroidUtils
import kotlin.math.min
import kotlin.random.Random

class StatsView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : View(context, attributeSet, defStyleAttr, defStyleRes) {

    private var textSize = AndroidUtils.dp(context, 20).toFloat()
    private var lineWidth = AndroidUtils.dp(context, 16).toFloat()
    private var colors = emptyList<Int>()

    init {
        context.withStyledAttributes(attributeSet, R.styleable.StatsView) {
            textSize = getDimension(R.styleable.StatsView_textSize, textSize)
            lineWidth = getDimension(R.styleable.StatsView_lineWidth, lineWidth)
            colors = listOf(
                getColor(R.styleable.StatsView_color1, generateRandomColor()),
                getColor(R.styleable.StatsView_color2, generateRandomColor()),
                getColor(R.styleable.StatsView_color3, generateRandomColor()),
                getColor(R.styleable.StatsView_color4, generateRandomColor())
            )
        }
    }

    var data: List<Float> = emptyList()
        set(value) {
            field = value
            invalidate()
        }

    private var radius: Float = 0F
    private var center = PointF(0F, 0F)
    private var oval = RectF(0F, 0F, 0F, 0F)
    private var topPoint = PointF(0F, 0F)

    private val paint = Paint(
        Paint.ANTI_ALIAS_FLAG
    ).apply {
        strokeWidth = lineWidth
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }
    private val textPaint = Paint(
        Paint.ANTI_ALIAS_FLAG
    ).apply {
        textSize = this@StatsView.textSize
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        val padding = lineWidth / 2 + AndroidUtils.dp(context, 5)
        radius = min(w, h) / 2F - padding

        center = PointF(w / 2F, h / 2F)
        topPoint = PointF(center.x, center.y - radius)
        oval = RectF(
            center.x - radius, center.y - radius,
            center.x + radius, center.y + radius
        )
    }

    override fun onDraw(canvas: Canvas) {
        if (data.isEmpty()) return
        var startAngle = -90F
        val sum = data.sum()

        if (sum == 0f) return
        data.forEachIndexed { index, datum ->
            val angle = datum / sum * 360

            paint.color = colors.getOrElse(index) { generateRandomColor() }

            paint.strokeCap = if (index == 0) {
                Paint.Cap.BUTT
            } else {
                Paint.Cap.ROUND
            }

            canvas.drawArc(oval, startAngle, angle, false, paint)
            startAngle += angle
        }
        paint.apply {
            color = colors.first()
            strokeCap = Paint.Cap.ROUND
        }

        canvas.drawArc(oval, -90f, 0.01f, false, paint)

        val percentage = 100F
        canvas.drawText(
            "%.2f%%".format(percentage),
            center.x,
            center.y + textPaint.textSize / 4,
            textPaint
        )
    }

    private fun generateRandomColor() = Random.nextInt(0xFF000000.toInt(), 0xFFFFFFFF.toInt())
}