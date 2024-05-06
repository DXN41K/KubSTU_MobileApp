package com.example.kubstu_app.View

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.marginStart
import androidx.viewpager2.widget.ViewPager2
import com.example.kubstu_app.ViewModel.Adapters.OnBoardingAdapter
import com.example.kubstu_app.Model.OnBoardingItem
import com.example.kubstu_app.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class LogInActivity : AppCompatActivity() {

    private lateinit var onBoardingAdapter: OnBoardingAdapter
    private lateinit var layoutOnBoardingIndicators: LinearLayout
    private lateinit var LogInBtn: Button

    companion object{
        val dip = 40f
        var px by Delegates.notNull<Float>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val displayMetrics = resources.displayMetrics
        val height = displayMetrics.heightPixels
        val peek = (height * 0.25).toInt()

        val marg = (peek - 160) / 2

        val Logo: View = findViewById(R.id.LogoLayer)
        val layoutParams = Logo.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.setMargins(0, marg, 0, 0)
        Logo.layoutParams = layoutParams



        val bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.btmSht)).apply {
            this.peekHeight = peek
            px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip,  resources.displayMetrics)
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        val bottomSheetBehavior2 = BottomSheetBehavior.from(findViewById(R.id.btmSht2)).apply {

            val displayMetrics = resources.displayMetrics
            val height = displayMetrics.heightPixels
            this.peekHeight = (height * 0.75).toInt()
            this.isHideable = true
            this.isDraggable = false
            this.state = BottomSheetBehavior.STATE_HIDDEN
        }

        val button = findViewById<Button>(R.id.BtnOnBoardingAction)
        button.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                val state1 = (slideOffset <= 0.12)
                val alpha1 = (1 - 2.0 * slideOffset).toFloat()
                val state2 = (slideOffset >= 0.95)
                val alpha2 = ((slideOffset - 0.5) * 2.0).toFloat()

                findViewById<CardView>(R.id.bottomsheetnegr).radius = (px - px * slideOffset)
                findViewById<ViewPager2>(R.id.ViewPagerOnBoarding).isEnabled = state1
                findViewById<TextView>(R.id.textView2).isEnabled = state1
                findViewById<TextView>(R.id.createAccountLink).isEnabled = state1
                findViewById<TextView>(R.id.textView2).alpha = alpha1
                findViewById<TextView>(R.id.createAccountLink).alpha = alpha1
                button.alpha = alpha1
                setAlphaForImageViews(alpha1)
                findViewById<ImageView>(R.id.LogoImg).alpha = alpha2
                findViewById<TextView>(R.id.LogoText).alpha = alpha2

                if ((bottomSheetBehavior2.state == BottomSheetBehavior.STATE_HIDDEN) && (slideOffset >= 0.95)) {
                    GlobalScope.launch {
                    bottomSheetBehavior2.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                } else if ((bottomSheetBehavior2.state == BottomSheetBehavior.STATE_COLLAPSED) && (slideOffset <= 0.95)){
                    GlobalScope.launch {
                    bottomSheetBehavior2.state = BottomSheetBehavior.STATE_HIDDEN
                    }
                }
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }
        })



        layoutOnBoardingIndicators = findViewById(R.id.LayoutOnBoardingIndicator)

        setupOnBoardingItems()

        val onBoardingViewPager: ViewPager2 = findViewById(R.id.ViewPagerOnBoarding)
        onBoardingViewPager.adapter = onBoardingAdapter

        setupOnBoardingIndicators()
        setCurrentOnBoardingIndicator(0);

        onBoardingViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentOnBoardingIndicator(position)
            }
        })

        LogInBtn = findViewById(R.id.LogInBtn)
        LogInBtn.setOnClickListener {

            val resultIntent = Intent()
            resultIntent.putExtra("key", "value")
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }


    //region PageViewFuncs
    private fun setupOnBoardingItems() {

        val first_item: OnBoardingItem = OnBoardingItem(
            R.drawable.img1,
            "Расписание занятий \nи экзаменов"
        )

        val second_item: OnBoardingItem = OnBoardingItem(
            R.drawable.img2,
            "Электронная\nзачетная книжка"
        )

        val third_item: OnBoardingItem = OnBoardingItem(
            R.drawable.img3,
            "Оплата\nзаказов в столовых\nи буфетах"
        )

        val onBoardingItems: List<OnBoardingItem> = arrayListOf(first_item, second_item, third_item)

        onBoardingAdapter = OnBoardingAdapter(onBoardingItems)
    }

    private fun setupOnBoardingIndicators() {
        val indicators = arrayOfNulls<ImageView>(onBoardingAdapter.itemCount)

        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)

        layoutParams.marginStart = 5
        layoutParams.marginEnd = 5
        for (i in 0 until indicators.size) {
            val imageView = ImageView(applicationContext)

            imageView.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.onboarding_indicator_inactive
                )
            )
            imageView.layoutParams = layoutParams

            layoutOnBoardingIndicators.addView(imageView)
        }

        layoutOnBoardingIndicators.gravity = Gravity.CENTER // Установка гравитации по центру
    }

    private fun setCurrentOnBoardingIndicator(index: Int) {
        val childCount = layoutOnBoardingIndicators.childCount
        for (i in 0 until childCount) {
            var imageView: ImageView = layoutOnBoardingIndicators.getChildAt(i) as ImageView
            if (i == index) imageView.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.onboarding_indicator_active
                )
            )
            else imageView.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.onboarding_indicator_inactive
                )
            )
        }
    }

    private fun setAlphaForImageViews(alpha: Float) {
        for (i in 0 until layoutOnBoardingIndicators.childCount) {
            val child = layoutOnBoardingIndicators.getChildAt(i)
            if (child is ImageView) {
                child.alpha = alpha
            }
        }
    }
    //endregion
}