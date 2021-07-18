package com.jbapp.activities

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

import com.jbapp.R
import kotlinx.android.synthetic.main.activity_make_up_category.*
import kotlinx.android.synthetic.main.activity_mehndi.ivBackButton

class MakeUpCategoryActivity: AppCompatActivity(), View.OnTouchListener,
    GestureDetector.OnGestureListener, View.OnDragListener, GestureDetector.OnDoubleTapListener
    //ScaleGestureDetector.OnScaleGestureListener
{

    lateinit var mGestureDetector: GestureDetector
    var TAG = "TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_up_category)
        ivBackButton.setOnClickListener {
            onBackPressed()
        }

        mGestureDetector = GestureDetector(this, this)

        //iv_rose.setOnTouchListener(this)
     //   iv_offer_de.setOnTouchListener(this)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {

//        val action = event?.action
//        Log.e(TAG, "Action $action")
//        when (action) {
//            MotionEvent.ACTION_DOWN -> {
//                Log.e(TAG, "Action was DOWN")
//                  return true
//            }
//            MotionEvent.ACTION_MOVE -> {
//                Log.e(TAG, "Action was MOVE")
//                Log.e(TAG, "On Touch (x,y) (" + event.x + " , " + event.y + ")")
//                return true
//            }
//            MotionEvent.ACTION_UP -> {
//                Log.e(TAG, "Action was UP")
//                return true
//            }
//            MotionEvent.ACTION_CANCEL -> {
//                Log.e(TAG, "Action was CANCEL")
//                return true
//            }
//            MotionEvent.ACTION_OUTSIDE -> {
//                Log.e(
//                    TAG, "Movement occurred outside bounds " +
//                            "of current screen element"
//                )
//                return true
//            }
//              else -> return super.onTouchEvent(event)
//        }
        mGestureDetector.onTouchEvent(event)
        if (v != null) {
            if (v.id == R.id.iv_rose)
                return true

            if (v.id == R.id.iv_offer_de)
                return false
        }
        return true
    }

    /*
    * Gesture Detector
    * */
    override fun onDown(e: MotionEvent?): Boolean {
        Log.e(TAG, "onDown : Called")
        return false
    }

    override fun onShowPress(e: MotionEvent?) {
        Log.e(TAG, "onShowPress : Called")

    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        Log.e(TAG, "onSingleTapUp : Called")
        return false
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        Log.e(TAG, "onScroll : Called")
        return false
    }


    override fun onLongPress(event: MotionEvent?) {
        Log.e(TAG, "onLongPress : Called")
        val dragBuilder: View.DragShadowBuilder = View.DragShadowBuilder(iv_rose)
//        if (Build.VERSION.SDK_INT  >= Build.VERSION_CODES.N)
//        iv_rose.startDragAndDrop(null, dragBuilder, null, 0)
//        else
        iv_rose.startDrag(null, dragBuilder, null, 0)

        dragBuilder.view.setOnDragListener(this)
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        Log.e(TAG, "onFling : Called")
        return false
    }

    /*
      * onDrag
      * */
    override fun onDrag(v: View?, dragEvent: DragEvent?): Boolean {

        when (dragEvent?.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                Log.e(TAG, "onDrag: drag started.")
                return true
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                Log.e(TAG, "onDrag: drag entered.")
                return true
            }
            DragEvent.ACTION_DRAG_LOCATION -> {
                Log.e(
                    TAG,
                    "onDrag: current point: ( " + dragEvent.x
                        .toString() + " , " + dragEvent.y.toString() + " )"
                )
                return true
            }
            DragEvent.ACTION_DRAG_EXITED -> {
                Log.e(TAG, "onDrag: exited.")
                return true
            }
            DragEvent.ACTION_DROP -> {
                Log.e(TAG, "onDrag: dropped.")
                return true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                Log.e(TAG, "onDrag: ended.")
                return true
            }
            else -> Log.e(TAG, "Unknown action type received by OnStartDragListener.")
        }
        return false
    }

    /*
    OnDoubleTap
     */
    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        Log.e(TAG, "onSingleTapConfirmed : Called")
        return false
    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        Log.e(TAG, "onDoubleTap : Called")
        return false
    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        Log.e(TAG, "onDoubleTapEvent : Called")
        return false
    }

//    /*
//    ScaleGestureDetector
//     */
//    override fun onScale(detector: ScaleGestureDetector?): Boolean {
//        TODO("Not yet implemented")
//    }
//
//    override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
//        TODO("Not yet implemented")
//    }
//
//    override fun onScaleEnd(detector: ScaleGestureDetector?) {
//        TODO("Not yet implemented")
//    }
}
