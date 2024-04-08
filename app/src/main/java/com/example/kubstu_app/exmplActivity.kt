package com.example.kubstu_app

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior

class exmplActivity : AppCompatActivity() {

    private lateinit var onBoardingAdapter: OnBoardingAdapter
    private lateinit var layoutOnBoardingIndicators: LinearLayout
    private lateinit var LogInBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exmpl)

        val bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.btmSht)).apply {
            peekHeight = 750
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        val bottomSheetBehavior2 = BottomSheetBehavior.from(findViewById(R.id.btmSht2)).apply {

            this.isHideable = true
            this.isDraggable = false
            this.state = BottomSheetBehavior.STATE_HIDDEN
        }

        val button = findViewById<Button>(R.id.BtnOnBoardingAction)
        button.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                val state1 = (slideOffset <= 0.12)
                val alpha1 = (1 - 2.0 * slideOffset).toFloat()
                val state2 = (slideOffset >= 0.95)
                val alpha2 = ((slideOffset - 0.5) * 2.0).toFloat()

                findViewById<ViewPager2>(R.id.ViewPagerOnBoarding).isEnabled = state1
                findViewById<TextView>(R.id.textView2).isEnabled = state1
                findViewById<TextView>(R.id.createAccountLink).isEnabled = state1
                findViewById<TextView>(R.id.textView2).alpha = alpha1
                findViewById<TextView>(R.id.createAccountLink).alpha = alpha1
                button.alpha = alpha1
                setAlphaForImageViews(alpha1)
                findViewById<ImageView>(R.id.LogoImg).alpha = alpha2
                findViewById<TextView>(R.id.LogoText).alpha = alpha2

                if (slideOffset >= 0.97) {
                    bottomSheetBehavior2.state = BottomSheetBehavior.STATE_COLLAPSED
                }
                else {
                    bottomSheetBehavior2.state = BottomSheetBehavior.STATE_HIDDEN

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

        onBoardingViewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
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

        val first_item :OnBoardingItem = OnBoardingItem(R.drawable.img1,
                                                  "Расписание занятий \nи экзаменов")

        val second_item :OnBoardingItem = OnBoardingItem(R.drawable.img2,
                                                  "Электронная\nзачетная книжка")

        val third_item :OnBoardingItem = OnBoardingItem(R.drawable.img3,
                                                  "Оплата\nзаказов в столовых\nи буфетах")

        val onBoardingItems: List<OnBoardingItem> = arrayListOf(first_item, second_item, third_item)

        onBoardingAdapter = OnBoardingAdapter(onBoardingItems)
    }

    private fun setupOnBoardingIndicators() {
        val indicators = arrayOfNulls<ImageView>(onBoardingAdapter.itemCount)

        val layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)

        for (i in 0 until indicators.size) {
            val imageView = ImageView(applicationContext)

            imageView.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.onboarding_indicator_inactive))
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
                ContextCompat.getDrawable(applicationContext, R.drawable.onboarding_indicator_active)
            )
            else  imageView.setImageDrawable(
                ContextCompat.getDrawable(applicationContext, R.drawable.onboarding_indicator_inactive)
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