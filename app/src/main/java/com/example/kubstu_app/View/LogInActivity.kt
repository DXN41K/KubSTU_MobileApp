package com.example.kubstu_app.View

import android.app.ActionBar.LayoutParams
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.sax.TextElementListener
import android.text.Editable
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.kubstu_app.ViewModel.Adapters.OnBoardingAdapter
import com.example.kubstu_app.Model.OnBoardingItem
import com.example.kubstu_app.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class LogInActivity : AppCompatActivity() {

    //добавление позже инициализируемых переменных
    private lateinit var onBoardingAdapter: OnBoardingAdapter
    private lateinit var layoutOnBoardingIndicators: LinearLayout
    private lateinit var GroupBtn: Button
    private lateinit var LogInBtn: Button
    private lateinit var nameET: EditText
    private lateinit var surnameET: EditText
    private lateinit var groupET: EditText
    private lateinit var stage1: ConstraintLayout
    private lateinit var stage2: ConstraintLayout
    private lateinit var stg1Params: ConstraintLayout.LayoutParams
    private lateinit var stg2Params: ConstraintLayout.LayoutParams

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var ChooseGroupBtn: Button

    //статические поля
    companion object{
        val dip = 40f
        var px by Delegates.notNull<Float>()
        val REQUEST_CODE = 8
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        ChooseGroupBtn = findViewById(R.id.ChooseGroupBtn)
        ChooseGroupBtn.setOnClickListener {
            val intent = Intent(this, ChooseGroupActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }

        sharedPreferences = getSharedPreferences("Users_preferences", Context.MODE_PRIVATE)

        //Инициализация переменных
        stage1 = findViewById(R.id.stage1)
        stage2 = findViewById(R.id.stage2)

        stg1Params = stage1.layoutParams as ConstraintLayout.LayoutParams
        stg2Params = stage2.layoutParams as ConstraintLayout.LayoutParams

        nameET = findViewById(R.id.NameTE)
        surnameET = findViewById(R.id.SurNameTE)
        groupET = findViewById(R.id.GroupTE)


        //добавление слушателей на кнопки
        LogInBtn = findViewById(R.id.LogInBtn)
        GroupBtn = findViewById(R.id.GroupBtn)

        LogInBtn.setOnClickListener {

            //добавить прочтение фамилии и имени
            if (nameET.text.isEmpty()) {
                val textIL = findViewById<TextInputLayout>(R.id.NameTB)
                textIL.error = "Введите имя"
                return@setOnClickListener
            }

            if (surnameET.text.isEmpty()) {
                val textIL = findViewById<TextInputLayout>(R.id.SurNameTB)
                textIL.error = "Введите фамилию"
                return@setOnClickListener
            }

            val editor = sharedPreferences.edit()
            editor.putString("Users_name", "${nameET.text}")
            editor.putString("Users_surname", "${surnameET.text}")
            editor.apply()

            val mainIntent = Intent(this@LogInActivity, MainActivity::class.java)
            startActivity(mainIntent)
            finish()
        }

        GroupBtn.setOnClickListener {

            //добавить прочтение группы

            if (groupET.text.isEmpty()) {
                val textIL = findViewById<TextInputLayout>(R.id.GroupTB)
                textIL.error = "Введите группу"
                return@setOnClickListener
            }

            stage1.alpha = 0f

            //костыль убирающий 2 накладывающихся на друг друга слоя
            stg1Params.topMargin = -2000
            stage1.layoutParams = stg1Params
            stg2Params.topMargin = 0
            stage2.layoutParams = stg2Params

            stage2.alpha = 1f

            LogInBtn.isEnabled = true
            GroupBtn.isEnabled = false

            groupET.isEnabled = false

            nameET.isEnabled = true
            surnameET.isEnabled = true
        }

        //рассчет пропорций расположения элементов
        val displayMetrics = resources.displayMetrics
        val height = displayMetrics.heightPixels
        val peek = (height * 0.25).toInt()

        val marg = (peek - 160) / 2

        val Logo: View = findViewById(R.id.LogoLayer)
        val layoutParams = Logo.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.setMargins(0, marg, 0, 0)
        Logo.layoutParams = layoutParams


        //объявление объекта поведения первой нижней панели
        val bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.btmSht)).apply {
            this.peekHeight = peek
            px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip,  resources.displayMetrics)
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        //объявление объекта поведения второй нижней панели
        val bottomSheetBehavior2 = BottomSheetBehavior.from(findViewById(R.id.btmSht2)).apply {

            val displayMetrics = resources.displayMetrics
            val height = displayMetrics.heightPixels
            this.peekHeight = (height * 0.6).toInt()
            this.isHideable = true
            this.isDraggable = false
            this.state = BottomSheetBehavior.STATE_HIDDEN
        }

        //добавление слушателя кнопки выдвижения панелей
        val button = findViewById<Button>(R.id.BtnOnBoardingAction)
        button.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        //настройка поведения первой панели
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                val state1 = (slideOffset <= 0.12)
                val alpha1 = (1 - 2.0 * slideOffset).toFloat()
                val state2 = (slideOffset >= 0.9)
                val alpha2 = ((slideOffset - 0.5) * 2.0).toFloat()

                findViewById<CardView>(R.id.bottomsheetnegr).radius = (px - px * slideOffset)
                findViewById<ViewPager2>(R.id.ViewPagerOnBoarding).isEnabled = state1
                button.alpha = alpha1
                //setAlphaForImageViews(alpha1)
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

        //настройка поведения второй панели
        bottomSheetBehavior2.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                runOnUiThread{

                    when (newState) {
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            GroupBtn.isEnabled = true
                            stg1Params.topMargin = 0
                            stage1.layoutParams = stg1Params
                            stg2Params.topMargin = -2000
                        }

                        BottomSheetBehavior.STATE_HIDDEN -> {
                            stage2.alpha = 0f
                            //костыль убирающий 2 накладывающихся на друг друга слоя
                            stg1Params.topMargin = 0
                            stage1.layoutParams = stg1Params
                            stg2Params.topMargin = -2000
                            stage2.layoutParams = stg2Params
                            stage1.alpha = 1f
                            LogInBtn.isEnabled = false
                            GroupBtn.isEnabled = false

                            groupET.isEnabled = false
                            nameET.isEnabled = false
                            surnameET.isEnabled = false
                        }
                    }
                }
            }
        })


        //инициализация ViewPager
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
    }


    //region PageViewFuncs

    //инициализация ViewPager
    private fun setupOnBoardingItems() {

        val first_item: OnBoardingItem = OnBoardingItem(
            R.drawable.logo_volume,
            "Открой для себя\nКубанский Политех"
        )


        val onBoardingItems: List<OnBoardingItem> = arrayListOf(first_item)

        onBoardingAdapter = OnBoardingAdapter(onBoardingItems)
    }

    //установка индикаторов
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

    //установка индикаторов при пролистывании
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

    //альфаканал для индикаторов ViewPager
    private fun setAlphaForImageViews(alpha: Float) {
        for (i in 0 until layoutOnBoardingIndicators.childCount) {
            val child = layoutOnBoardingIndicators.getChildAt(i)
            if (child is ImageView) {
                child.alpha = alpha
            }
        }
    }
    //endregion

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            val result = data?.getStringExtra("result")

            val textIL = findViewById<TextInputLayout>(R.id.GroupTB)
            if (result == null) return
            else {
                val editor = sharedPreferences.edit()
                editor.putString("Users_group", "${result}")
                editor.apply()
                textIL.hint = ""
                textIL.editText?.setText(result)
            }
        }
    }
}