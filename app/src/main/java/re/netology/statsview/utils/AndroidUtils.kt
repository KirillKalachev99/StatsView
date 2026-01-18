package re.netology.statsview.utils

import android.content.Context
import kotlin.math.ceil

object AndroidUtils {

    fun dp(context: Context, dp: Int) =
        ceil(dp * context.resources.displayMetrics.density).toInt()
}