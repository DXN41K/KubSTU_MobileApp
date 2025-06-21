package com.example.kubstu_app.presentation.activity

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
import com.example.kubstu_app.presentation.viewstate.OnBoardingViewState
import com.example.kubstu_app.data.remote.response.LoginResponse
import com.example.kubstu_app.R
import com.example.kubstu_app.data.model.SemesterModel
import com.example.kubstu_app.data.model.SpecsModel
import com.example.kubstu_app.data.remote.response.SpecsResponse
import com.example.kubstu_app.data.remote.retrofit.IAPI
import com.example.kubstu_app.data.remote.retrofit.RetrofitClient
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.properties.Delegates

class LogInActivity : AppCompatActivity() {
    //статические поля
    companion object{
        val dip = 40f
        var px by Delegates.notNull<Float>()
    }

    private lateinit var onBoardingAdapter: OnBoardingAdapter
    private lateinit var layoutOnBoardingIndicators: LinearLayout
    private lateinit var LogInBtn: Button
    private lateinit var LoginET: EditText
    private lateinit var PasswordET: EditText
    private lateinit var stage2: ConstraintLayout
    private lateinit var _user: LoginResponse

    private lateinit var sharedPreferences: SharedPreferences

    lateinit var list: Map<String, SemesterModel>
    lateinit var specific: SpecsResponse
    lateinit var specific_text: SpecsModel
    val tips_kontr = listOf(2,3,4,9,10,11,12)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val api = RetrofitClient.getClient()
        val lessonAPI = api.create(IAPI::class.java)

        sharedPreferences = getSharedPreferences("Users_preferences", Context.MODE_PRIVATE)

        stage2 = findViewById(R.id.stage2)
        LoginET = findViewById(R.id.LoginET)
        PasswordET = findViewById(R.id.PasswordET)

        LogInBtn = findViewById(R.id.LogInBtn)
        LogInBtn.setOnClickListener {

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
                    Log.i("jojo", "1")

                    _user = lessonAPI.login(login, password)

                    Log.i("jojo", "2")

                    list = lessonAPI.getGrades(_user.access_token)
                    specific = lessonAPI.getSpecs()
                    specific_text = specific.data!!.first { it.id == (_user.spec_id ?: 0) }


                    var count = 0
                    var sum = 0


                    if (!list.isNullOrEmpty()) {
                        for ((_, item) in list) {
                            if (!item.e.isNullOrEmpty()) {
                                for (i in item.e) {
                                    if (!tips_kontr.contains(i.tipkontr_id)) continue
                                    if (i.oc_f != 0 && i.oc_f != 8) {
                                        sum += i.oc_f
                                        count++
                                    }
                                }
                            }
                            if (!item.z.isNullOrEmpty()) {
                                for (i in item.z) {
                                    if (!tips_kontr.contains(i.tipkontr_id)) continue
                                    if (i.tipkontr_id != 2 && (i.oc_f != 0 && i.oc_f != 8)) {
                                        sum += i.oc_f
                                        count++
                                    }
                                }
                            }
                        }
                    }

                    var roundedResult = "-"
                    if (count > 0) {
                        var mid = sum.toDouble() / count.toDouble()
                        roundedResult = String.format(Locale.US, "%.1f", mid)
                    }

                    Log.i("jojo", "3")


                    Log.i(
                        "Login", "id: ${_user.id} \n" +
                                "${_user.fam} \n" +
                                "${_user.imja} \n" +
                                "${_user.otch} \n" +
                                "gr: ${_user.gr} \n" +
                                "formaob_id: ${_user.formaob_id} \n" +
                                "inst_id: ${_user.inst_id} \n" +
                                "kurs: ${_user.kurs} \n" +
                                "spec_id: ${_user.spec_id} \n" +
                                "AT: ${_user.access_token} \n" +
                                "RT: ${_user.refresh_token} \n" +
                                "average_grade: ${_user.refresh_token} \n" +
                                "specific_name: ${_user.refresh_token} \n" +
                                "specific_code: ${_user.refresh_token} \n"
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
                        putString("average_grade", roundedResult)
                        putString("specific_code", specific_text.code)
                        putString("specific_name", specific_text.name)
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

        // Установка ссылок
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

        val first_item: OnBoardingViewState = OnBoardingViewState(
            R.drawable.logo_volume,
            "Открой для себя\nКубанский Политех"
        )


        val onBoardingViewStates: List<OnBoardingViewState> = arrayListOf(first_item)

        onBoardingAdapter = OnBoardingAdapter(onBoardingViewStates)
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