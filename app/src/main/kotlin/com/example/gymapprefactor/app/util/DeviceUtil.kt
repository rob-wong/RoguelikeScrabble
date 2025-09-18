package com.example.gymapprefactor.app.util

import android.content.Context
import android.util.DisplayMetrics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


object DeviceUtil {
    private const val PREVIEW_COLUMN_SIZE = 50f
    private const val PORTRAIT_COLUMNS = 8f
    private const val LANDSCAPE_COLUMNS = 12f

    private var columnSize: Float = PREVIEW_COLUMN_SIZE
    private var displayMetrics: DisplayMetrics? = null

    var isLandscape: Boolean = false

    fun init(context: Context) {
        displayMetrics = context.resources.displayMetrics
        displayMetrics?.let {
            val screenWidth = it.widthPixels
            val screenHeight = it.heightPixels

            determineScreenOrientation(screenWidth, screenHeight)
            calculateColumnSize(screenWidth, it.density)  // Pass density to convert pixels to dp
        }
    }

    private fun determineScreenOrientation(screenWidth: Int, screenHeight: Int) {
        isLandscape = screenWidth > screenHeight
    }

    private fun calculateColumnSize(screenWidth: Int, density: Float) {
        val screenWidthDp = screenWidth / density

        columnSize = when(isLandscape) {
            true -> screenWidthDp / LANDSCAPE_COLUMNS
            false -> screenWidthDp / PORTRAIT_COLUMNS
        }
        println("ColumnSize: $columnSize dp")
    }

    fun getColumnWidthDp(columns: Int): Dp {
        return (columnSize * columns).dp
    }

    fun getColumnWidthPx(columns: Int): Float {
        return (columnSize * columns)
    }
}
