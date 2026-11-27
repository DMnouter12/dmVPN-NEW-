package com.dmvpn.app

import android.app.Activity
import android.os.Bundle
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.view.animation.*
import android.widget.*
import android.content.res.ColorStateList
import android.graphics.Typeface
import kotlin.math.*

class MainActivity : Activity() {

    private val bg = Color.rgb(5, 3, 17)
    private val purple = Color.rgb(151, 25, 255)
    private val pink = Color.rgb(255, 27, 190)
    private val white = Color.rgb(248, 237, 255)
    private val lavender = Color.rgb(198, 158, 255)
    private var connected = false
    private var selectedServer = "США"
    private var selectedCity = "New York"
    private lateinit var content: LinearLayout
    private lateinit var statusText: TextView
    private lateinit var statusDescription: TextView
    private lateinit var connectButton: TextView
    private lateinit var orb: NeonOrbView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg
        window.decorView.systemUiVisibility = 0
        showHome()
    }

    private fun dp(v: Int): Int =
        (v * resources.displayMetrics.density).toInt()

    private fun gradient(
        colors: IntArray,
        radius: Float,
        strokeColor: Int = Color.TRANSPARENT,
        strokeWidth: Int = 0
    ): GradientDrawable {
        return GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            colors
        ).apply {
            cornerRadius = radius
            if (strokeWidth > 0) setStroke(strokeWidth, strokeColor)
        }
    }

    private fun text(
        value: String,
        size: Float,
        color: Int = white,
        bold: Boolean = false
    ): TextView {
        return TextView(this).apply {
            text = value
            textSize = size
            setTextColor(color)
            typeface = if (bold) Typeface.create("sans-serif", Typeface.BOLD)
            else Typeface.create("sans-serif", Typeface.NORMAL)
            gravity = Gravity.CENTER_VERTICAL
            includeFontPadding = true
        }
    }

    private fun card(): LinearLayout {
        return LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(16), dp(18), dp(16))
            background = GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                intArrayOf(
                    Color.rgb(16, 7, 34),
                    Color.rgb(9, 4, 23)
                )
            ).apply {
                cornerRadius = dp(25).toFloat()
                setStroke(dp(1), Color.rgb(116, 20, 205))
            }
            elevation = dp(8).toFloat()
        }
    }

    private fun addSpace(parent: LinearLayout, height: Int) {
        parent.addView(View(this), LinearLayout.LayoutParams(1, dp(height)))
    }

    private fun showHome() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(bg)
        }

        val scroll = ScrollView(this).apply {
            clipToPadding = false
            isFillViewport = true
            setPadding(0, 0, 0, dp(12))
        }

        content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(8), dp(18), dp(10))
        }

        scroll.addView(content)
        root.addView(
            scroll,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        buildHeader()
        buildOrb()
        buildStatus()
        buildConnectButton()
        buildServerCard()
        buildMap()
        buildProtectionCard()

        root.addView(buildNavigation())
        setContentView(root)
    }

    private fun buildHeader() {
        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        val brand = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        val logo = text("NIVORANT", 30f, white, true)
        logo.letterSpacing = 0.08f
        logo.setShadowLayer(dp(15).toFloat(), 0f, 0f, purple)

        val subtitle = text(
            "Y O U R   P R I V A T E   N E T W O R K",
            10f,
            lavender
        )

        brand.addView(logo)
        brand.addView(subtitle)

        val settings = text("⚙", 31f, white, true).apply {
            gravity = Gravity.CENTER
            setShadowLayer(dp(12).toFloat(), 0f, 0f, purple)
            setOnClickListener { showSimplePage("НАСТРОЙКИ") }
        }

        header.addView(
            brand,
            LinearLayout.LayoutParams(0, dp(65), 1f)
        )
        header.addView(settings, LinearLayout.LayoutParams(dp(48), dp(55)))

        content.addView(header)
    }

    private fun buildOrb() {
        orb = NeonOrbView()
        val holder = FrameLayout(this).apply {
            addView(
                orb,
                FrameLayout.LayoutParams(
                    dp(300),
                    dp(300),
                    Gravity.CENTER
                )
            )
        }

        content.addView(
            holder,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(310)
            )
        )
    }

    private fun buildStatus() {
        statusText = text(
            "НЕ ПОДКЛЮЧЕНО",
            24f,
            pink,
            true
        ).apply {
            gravity = Gravity.CENTER
            setShadowLayer(dp(12).toFloat(), 0f, 0f, pink)
            letterSpacing = 0.03f
        }

        statusDescription = text(
            "Твоё соединение пока не защищено",
            15f,
            lavender
        ).apply {
            gravity = Gravity.CENTER
        }

        content.addView(statusText)
        content.addView(statusDescription)
        addSpace(content, 18)
    }

    private fun buildConnectButton() {
        connectButton = text(
            "▶   ВКЛЮЧИТЬ ЗАЩИТУ",
            18f,
            Color.WHITE,
            true
        ).apply {
            gravity = Gravity.CENTER
            background = gradient(
                intArrayOf(
                    Color.rgb(112, 20, 255),
                    Color.rgb(231, 20, 205)
                ),
                dp(45).toFloat()
            )
            elevation = dp(12).toFloat()
            setShadowLayer(dp(12).toFloat(), 0f, 0f, purple)
            setPadding(dp(12), dp(17), dp(12), dp(17))
            setOnClickListener {
                connected = false
                statusText.text = "НЕ ПОДКЛЮЧЕНО"
                statusDescription.text =
                    "VPN-сервер ещё не настроен"
                connectButton.text = "▶   ВКЛЮЧИТЬ ЗАЩИТУ"
                Toast.makeText(
                    this@MainActivity,
                    "VPN-сервер ещё не настроен. Подключение не выполнялось.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        content.addView(
            connectButton,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(58)
            )
        )
        addSpace(content, 24)
    }

    private fun buildServerCard() {
        val box = card()

        val top = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        top.addView(
            text("ВЫБРАННЫЙ СЕРВЕР", 15f, white, true),
            LinearLayout.LayoutParams(0, dp(34), 1f)
        )

        top.addView(
            text("СМЕНИТЬ  →", 15f, lavender, true).apply {
                setOnClickListener { showServers() }
            }
        )

        box.addView(top)

        val server = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(0, dp(12), 0, 0)
        }

        server.addView(
            text(
                flagFor(selectedServer),
                42f
            ),
            LinearLayout.LayoutParams(dp(65), dp(60))
        )

        val names = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }
        names.addView(text(selectedServer, 19f, white, true))
        names.addView(text(selectedCity, 16f, lavender))

        server.addView(
            names,
            LinearLayout.LayoutParams(0, dp(60), 1f)
        )

        server.addView(
            text("▂ ▅ ▇", 21f, purple, true).apply {
                setShadowLayer(dp(10).toFloat(), 0f, 0f, purple)
            }
        )

        box.addView(server)
        content.addView(box)
        addSpace(content, 18)
    }

    private fun flagFor(country: String): String {
        return when (country) {
            "США" -> "🇺🇸"
            "Англия" -> "🇬🇧"
            "Нидерланды" -> "🇳🇱"
            "Германия" -> "🇩🇪"
            "Франция" -> "🇫🇷"
            else -> "🌐"
        }
    }

    private fun buildMap() {
        val map = WorldMapView()
        val frame = FrameLayout(this).apply {
            background = GradientDrawable().apply {
                cornerRadius = dp(24).toFloat()
                setColor(Color.rgb(8, 4, 22))
                setStroke(dp(1), purple)
            }
            clipToOutline = true
            addView(
                map,
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    dp(155)
                )
            )
        }

        content.addView(
            frame,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(155)
            )
        )
        addSpace(content, 18)
    }

    private fun buildProtectionCard() {
        val box = card()
        box.addView(
            text("ТВОЯ ЗАЩИТА", 16f, white, true)
        )
        addSpace(box, 12)

        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
        }

        val items = listOf(
            Triple("♢", "Анонимность", "—"),
            Triple("ϟ", "Скорость", "—"),
            Triple("▢", "Безопасность", "—")
        )

        items.forEachIndexed { index, item ->
            val column = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
            }

            column.addView(
                text(item.first, 38f, lavender, true).apply {
                    gravity = Gravity.CENTER
                    setShadowLayer(dp(10).toFloat(), 0f, 0f, purple)
                }
            )
            column.addView(
                text(item.second, 12f, lavender).apply {
                    gravity = Gravity.CENTER
                }
            )
            column.addView(
                text(item.third, 15f, white, true).apply {
                    gravity = Gravity.CENTER
                }
            )

            row.addView(
                column,
                LinearLayout.LayoutParams(0, dp(92), 1f)
            )

            if (index < 2) {
                val line = View(this).apply {
                    setBackgroundColor(Color.rgb(64, 31, 99))
                }
                row.addView(
                    line,
                    LinearLayout.LayoutParams(dp(1), dp(70))
                )
            }
        }

        box.addView(row)
        content.addView(box)
    }

    private fun buildNavigation(): LinearLayout {
        val nav = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            setPadding(dp(5), dp(10), dp(5), dp(8))
            background = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(
                    Color.rgb(14, 6, 30),
                    Color.rgb(7, 3, 18)
                )
            ).apply {
                cornerRadii = floatArrayOf(
                    dp(25).toFloat(), dp(25).toFloat(),
                    dp(25).toFloat(), dp(25).toFloat(),
                    0f, 0f, 0f, 0f
                )
                setStroke(dp(1), Color.rgb(83, 20, 142))
            }
        }

        val tabs = listOf(
            Pair("⌂", "Главная"),
            Pair("◎", "Серверы"),
            Pair("▥", "Статистика"),
            Pair("♙", "Профиль"),
            Pair("⚙", "Настройки")
        )

        tabs.forEachIndexed { index, tab ->
            val active = index == 0
            val item = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                setPadding(0, dp(4), 0, dp(2))
                setOnClickListener {
                    when (index) {
                        0 -> showHome()
                        1 -> showServers()
                        2 -> showSimplePage("СТАТИСТИКА")
                        3 -> showSimplePage("ПРОФИЛЬ")
                        4 -> showSimplePage("НАСТРОЙКИ")
                    }
                }
            }

            item.addView(
                text(
                    tab.first,
                    29f,
                    if (active) pink else lavender,
                    true
                ).apply {
                    gravity = Gravity.CENTER
                    if (active) {
                        setShadowLayer(dp(12).toFloat(), 0f, 0f, pink)
                    }
                }
            )

            item.addView(
                text(
                    tab.second,
                    11f,
                    if (active) pink else lavender,
                    active
                ).apply {
                    gravity = Gravity.CENTER
                }
            )

            if (active) {
                item.addView(
                    View(this).apply {
                        background = gradient(
                            intArrayOf(pink, purple),
                            dp(10).toFloat()
                        )
                    },
                    LinearLayout.LayoutParams(dp(48), dp(3)).apply {
                        topMargin = dp(6)
                    }
                )
            } else {
                item.addView(View(this), LinearLayout.LayoutParams(1, dp(9)))
            }

            nav.addView(
                item,
                LinearLayout.LayoutParams(0, dp(78), 1f)
            )
        }

        return nav
    }

    private fun showServers() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(20), dp(24), dp(20), dp(20))
            setBackgroundColor(bg)
        }

        root.addView(text("NIVORANT", 28f, white, true))
        root.addView(text("ВЫБЕРИ СЕРВЕР", 18f, lavender, true))
        addSpace(root, 20)

        val servers = listOf(
            Triple("🇺🇸", "США", "New York"),
            Triple("🇬🇧", "Англия", "London"),
            Triple("🇳🇱", "Нидерланды", "Amsterdam"),
            Triple("🇩🇪", "Германия", "Frankfurt"),
            Triple("🇫🇷", "Франция", "Paris")
        )

        servers.forEach { server ->
            val item = card()
            val row = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
            }
            row.addView(text(server.first, 35f))
            val names = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(dp(15), 0, 0, 0)
            }
            names.addView(text(server.second, 18f, white, true))
            names.addView(text(server.third, 14f, lavender))
            row.addView(names, LinearLayout.LayoutParams(0, dp(55), 1f))
            row.addView(text("›", 30f, purple, true))
            item.addView(row)

            item.setOnClickListener {
                selectedServer = server.second
                selectedCity = server.third
                Toast.makeText(
                    this,
                    "Выбран сервер: ${server.second}",
                    Toast.LENGTH_SHORT
                ).show()
                showHome()
            }

            root.addView(
                item,
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    dp(85)
                ).apply { bottomMargin = dp(12) }
            )
        }

        root.addView(
            text("←  НА ГЛАВНУЮ", 16f, lavender, true).apply {
                setPadding(0, dp(15), 0, dp(15))
                setOnClickListener { showHome() }
            }
        )

        setContentView(root)
    }

    private fun showSimplePage(title: String) {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(dp(24), dp(24), dp(24), dp(24))
            setBackgroundColor(bg)
        }

        root.addView(text("NIVORANT", 30f, white, true).apply {
            gravity = Gravity.CENTER
        })

        root.addView(text(title, 22f, lavender, true).apply {
            gravity = Gravity.CENTER
            setPadding(0, dp(20), 0, dp(20))
        })

        root.addView(text(
            "Функция будет доступна после настройки VPN-сервиса.",
            15f,
            lavender
        ).apply {
            gravity = Gravity.CENTER
        })

        root.addView(
            text("←  НА ГЛАВНУЮ", 16f, pink, true).apply {
                gravity = Gravity.CENTER
                setPadding(0, dp(35), 0, dp(20))
                setOnClickListener { showHome() }
            }
        )

        setContentView(root)
    }

    inner class NeonOrbView : View(this) {
        private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        private var angle = 0f
        private val animator = ValueAnimator.ofFloat(0f, 360f).apply {
            duration = 9000
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener {
                angle = it.animatedValue as Float
                invalidate()
            }
        }

        init {
            animator.start()
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)

            val cx = width / 2f
            val cy = height / 2f
            val radius = min(width, height) * 0.39f

            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 2f
            paint.color = Color.argb(35, 165, 45, 255)
            canvas.drawCircle(cx, cy, radius * 1.48f, paint)

            paint.strokeWidth = 2.5f
            paint.color = purple
            paint.setShadowLayer(18f, 0f, 0f, purple)
            canvas.drawArc(
                cx - radius * 1.5f,
                cy - radius * 1.5f,
                cx + radius * 1.5f,
                cy + radius * 1.5f,
                angle,
                75f,
                false,
                paint
            )

            paint.color = pink
            canvas.drawArc(
                cx - radius * 1.35f,
                cy - radius * 1.35f,
                cx + radius * 1.35f,
                cy + radius * 1.35f,
                -angle * 0.7f,
                42f,
                false,
                paint
            )

            paint.strokeWidth = 15f
            paint.color = purple
            paint.setShadowLayer(28f, 0f, 0f, purple)
            canvas.drawCircle(cx, cy, radius, paint)

            paint.strokeWidth = 3f
  