package com.example.kubstu_app.View

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.kubstu_app.ViewModel.Adapters.OnBoardingAdapter
import com.example.kubstu_app.Model.OnBoardingItem
import com.example.kubstu_app.Model.student.LoginResponse
import com.example.kubstu_app.R
import com.example.kubstu_app.ViewModel.Retrofit.IAPI
import com.example.kubstu_app.ViewModel.Retrofit.retrofitClient
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates

class LogInActivity : AppCompatActivity() {

    //добавление позже инициализируемых переменных
    private lateinit var onBoardingAdapter: OnBoardingAdapter
    private lateinit var layoutOnBoardingIndicators: LinearLayout
    private lateinit var LogInBtn: Button
    private lateinit var LoginET: EditText
    private lateinit var PasswordET: EditText
    private lateinit var stage2: ConstraintLayout
    private lateinit var _user: LoginResponse

    private lateinit var sharedPreferences: SharedPreferences

    //статические поля
    companion object{
        val dip = 40f
        var px by Delegates.notNull<Float>()
        val REQUEST_CODE = 8
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val SignUpLink = findViewById<TextView>(R.id.SignupTV)
        val url1 = "https://student.kubstu.ru/site/signup-search-student"
        SignUpLink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url1)
            startActivity(intent)
        }

        val ResetPassLink = findViewById<TextView>(R.id.ResetPassTV)
        val url2 = "https://student.kubstu.ru/site/reset-password-request"
        ResetPassLink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url2)
            startActivity(intent)
        }

        retrofitClient.Initialize(this)
        val api = retrofitClient.getClient()
        val lessonAPI = api.create(IAPI::class.java)

        sharedPreferences = getSharedPreferences("Users_preferences", Context.MODE_PRIVATE)

        //Инициализация переменных
        stage2 = findViewById(R.id.stage2)
        LoginET = findViewById(R.id.LoginET)
        PasswordET = findViewById(R.id.PasswordET)

        //добавление слушателей на кнопки
        LogInBtn = findViewById(R.id.LogInBtn)

        LogInBtn.setOnClickListener {

            //добавить прочтение Login и Password
            val textIL = findViewById<TextInputLayout>(R.id.LoginTB)
            val textIL2 = findViewById<TextInputLayout>(R.id.PasswordTB)

            textIL.error = null
            textIL2.error = null

            if (LoginET.text.isEmpty()) {
                textIL.error = "Введите имя пользователя"
                return@setOnClickListener
            }
            if (PasswordET.text.isEmpty()) {
                textIL2.error = "Введите пароль"
                return@setOnClickListener
            }

            val password = PasswordET.text.toString()
            val login = LoginET.text.toString()

            lifecycleScope.launch(Dispatchers.IO) {

                try {
                    _user = lessonAPI.login(login, password)
                    Log.i(
                        "Login", "id: ${_user.id} \n" +
                                "${_user.fam} \n" +
                                "${_user.imja} \n" +
                                "${_user.otch} \n" +
                                "gr: ${_user.gr} \n" +
                                "formaob_id: ${_user.formaob_id} \n" +
                                "inst_id: ${_user.inst_id} \n" +
                                "kurs: ${_user.kurs} \n" +
                                "AT: ${_user.access_token} \n" +
                                "RT: ${_user.refresh_token} \n"
                    )
                    sharedPreferences.edit().apply {
                        putString("access_token", _user.access_token)
                        putString("refresh_token", _user.refresh_token)
                        putString("fam", _user.fam)
                        putString("imja", _user.imja)
                        putString("otch", _user.otch)
                        putString("gr", _user.gr)
                        putInt("kurs", _user.kurs)
                        putInt("spec_id", _user.spec_id)
                        putInt("inst_id", _user.inst_id)
                        putInt("formaob_id", _user.formaob_id)
                        apply()
                    }

                    withContext(Dispatchers.Main) {
                        val mainIntent = Intent(this@LogInActivity, MainActivity::class.java)
                        startActivity(mainIntent)
                        finish()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        textIL.error = " "
                        textIL2.error = "Не удалось войти. Попробуйте позже."
                    }
                }


            }
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
                px = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    dip,
                    resources.displayMetrics
                )
                this.state = BottomSheetBehavior.STATE_COLLAPSED
            }

            //объявление объекта поведения второй нижней панели
            val bottomSheetBehavior2 = BottomSheetBehavior.from(findViewById(R.id.btmSht2)).apply {

                val displayMetrics = resources.displayMetrics
                val height = displayMetrics.heightPixels
                this.peekHeight = (height * 0.75).toInt()
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
                    val alpha2 = ((slideOffset - 0.5) * 2.0).toFloat()

                    findViewById<CardView>(R.id.bottomsheetnegr).radius = (px - px * slideOffset)
                    findViewById<ViewPager2>(R.id.ViewPagerOnBoarding).isEnabled = state1
                    button.alpha = alpha1
                    //setAlphaForImageViews(alpha1)
                    findViewById<ImageView>(R.id.LogoImg).alpha = alpha2
                    findViewById<TextView>(R.id.LogoText).alpha = alpha2

                    if ((bottomSheetBehavior2.state == BottomSheetBehavior.STATE_HIDDEN) && (slideOffset >= 0.95)) {
                        lifecycleScope.launch {
                            bottomSheetBehavior2.state = BottomSheetBehavior.STATE_COLLAPSED
                        }
                    } else if ((bottomSheetBehavior2.state == BottomSheetBehavior.STATE_COLLAPSED) && (slideOffset <= 0.95)) {
                        lifecycleScope.launch {
                            bottomSheetBehavior2.state = BottomSheetBehavior.STATE_HIDDEN
                        }
                    }
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {

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

}