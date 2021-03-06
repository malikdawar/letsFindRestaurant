package com.example.restaurants.ui.home.widget

import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import com.example.restaurants.ui.home.drag.IDragCallback

/**
 * The DraggableLayout.kt
 * custom draggable layout widget, to moniter teh touch events
 * @return Frame Layout
 * @author Malik Dawar, malikdawar@hotmail.com
 */

class DraggableLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var dragListener: IDragCallback? = null
    private var lastTouched: Long = 0
    private val SCROLL_TIME = 200L

    fun setDrag(dragListener: IDragCallback) {
        this.dragListener = dragListener
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> lastTouched = SystemClock.uptimeMillis()
            MotionEvent.ACTION_UP -> {
                val now = SystemClock.uptimeMillis()
                if (now - lastTouched > SCROLL_TIME) {
                    dragListener?.onDrag()
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}
