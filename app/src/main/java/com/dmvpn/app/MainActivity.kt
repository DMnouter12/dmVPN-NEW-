
package com.dmvpn.app

import android.app.Activity
import android.os.Bundle
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import kotlin.math.min

class MainActivity : Activity() {
    private val bg = Color.rgb(5, 3, 17)
    private val purple = Color.rgb(151, 25, 255)
    private val pink = Color.rgb(255, 27, 190)
    private val white = Color.rgb(248, 237, 255)
    private val lavender = Color.rgb(198, 158, 255)

    private var selectedServer = "США"
    private var selectedCity = "New York"
    private var currentTab = 0

    // Текущий экран нужен для плавной смены страниц
    private var activeScreen: View? = null

    private lateinit var page: LinearLayout
    private lateinit var status: TextView
    private lateinit var connect: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg
        showHome()
    }

    private fun dp(v: Int) =
        (v * resources.displayMetrics.density).toInt()

    // Плавный переход между экранами без мигания
    private fun showAnimated(root: View) {
        val container = findViewById<FrameLayout>(android.R.id.content)
        val oldScreen = activeScreen

        if (oldScreen == null || oldScreen.parent !== container) {
            setContentView(root)
            activeScreen = root
            root.alpha = 1f
            return
        }

        // Убираем оставшиеся старые экраны после предыдущих переходов
        for (i in container.childCount - 1 downTo 0) {
            val child = container.getChildAt(i)
            if (child !== oldScreen) {
                child.animate().cancel()
                container.removeView(child)
            }
        }

        oldScreen.animate().cancel()
        oldScreen.alpha = 1f

        root.alpha = 0f

        container.addView(
            root,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )

        activeScreen = root

        root.animate()
            .alpha(1f)
            .setDuration(300L)
            .start()

        oldScreen.animate()
            .alpha(0f)
            .setDuration(300L)
            .withEndAction {
                if (oldScreen.parent === container) {
                    container.removeView(oldScreen)
                }
            }
            .start()
    }

    // Анимация нажатия без изменения дизайна
    private fun animateTap(view: View, action: () -> Unit) {
        view.animate().cancel()
        view.animate()
            .scaleX(0.96f)
            .scaleY(0.96f)
            .setDuration(75L)
            .withEndAction {
                view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100L)
                    .withEndAction {
                        action()
                    }
                    .start()
            }
            .start()
    }

    private fun label(
        value: String,
        size: Float,
        color: Int = white,
        bold: Boolean = false
    ): TextView =
        TextView(this).apply {
            text = value
            textSize = size
            setTextColor(color)
            typeface = Typeface.create(
                "sans-serif",
                if (bold) Typeface.BOLD else Typeface.NORMAL
            )
            gravity = Gravity.CENTER_VERTICAL
        }

    private fun panel(): LinearLayout = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(dp(18), dp(16), dp(18), dp(16))
        background = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(Color.rgb(19, 8, 39), Color.rgb(8, 4, 22))
        ).apply {
            cornerRadius = dp(24).toFloat()
            setStroke(dp(1), Color.rgb(112, 25, 190))
        }
        elevation = dp(7).toFloat()
    }

    private fun space(parent: LinearLayout, h: Int) {
        parent.addView(View(this), LinearLayout.LayoutParams(1, dp(h)))
    }

    private fun showHome() {
        currentTab = 0

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(bg)
        }

        val scroll = ScrollView(this).apply {
            clipToPadding = false
        }

        page = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(10), dp(18), dp(18))
        }

        scroll.addView(page)
        root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))

        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        val brand = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        brand.addView(label("NIVORANT", 29f, white, true).apply {
            letterSpacing = 0.08f
            setShadowLayer(dp(14).toFloat(), 0f, 0f, purple)
        })

        brand.addView(label("Y O U R   P R I V A T E   N E T W O R K", 9f, lavender))

        header.addView(brand, LinearLayout.LayoutParams(0, dp(62), 1f))

        header.addView(label("⚙", 29f, white, true).apply {
            gravity = Gravity.CENTER
            setOnClickListener {
                animateTap(it) {
                    showInfo("НАСТРОЙКИ")
                }
            }
        }, LinearLayout.LayoutParams(dp(48), dp(55)))

        page.addView(header)

        val orbHolder = FrameLayout(this)
        orbHolder.addView(
            NeonOrbView(),
            FrameLayout.LayoutParams(dp(300), dp(300), Gravity.CENTER)
        )
        page.addView(orbHolder, LinearLayout.LayoutParams(-1, dp(300)))

        status = label("НЕ ПОДКЛЮЧЕНО", 23f, pink, true).apply {
            gravity = Gravity.CENTER
            setShadowLayer(dp(10).toFloat(), 0f, 0f, pink)
        }

        page.addView(status)

        page.addView(label("Твоё соединение пока не защищено", 14f, lavender).apply {
            gravity = Gravity.CENTER
        })

        space(page, 18)

        connect = label("▶   ВКЛЮЧИТЬ ЗАЩИТУ", 17f, Color.WHITE, true).apply {
            gravity = Gravity.CENTER
            setPadding(dp(12), dp(16), dp(12), dp(16))
            background = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(Color.rgb(112, 20, 255), Color.rgb(231, 20, 205))
            ).apply {
                cornerRadius = dp(40).toFloat()
            }
            elevation = dp(10).toFloat()

            setOnClickListener {
                animateTap(it) {
                    status.text = "НЕ ПОДКЛЮЧЕНО"
                    Toast.makeText(
                        this@MainActivity,
                        "VPN-сервер ещё не настроен. Подключение не выполнялось.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        page.addView(connect, LinearLayout.LayoutParams(-1, dp(58)))
        space(page, 22)

        val serverCard = panel()

        val serverHeader = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        serverHeader.addView(
            label("ВЫБРАННЫЙ СЕРВЕР", 14f, white, true),
            LinearLayout.LayoutParams(0, dp(32), 1f)
        )

        serverHeader.addView(label("СМЕНИТЬ  →", 13f, lavender, true).apply {
            setOnClickListener {
                animateTap(it) {
                    showServers()
                }
            }
        })

        serverCard.addView(serverHeader)

        val serverRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        serverRow.addView(
            label(flag(selectedServer), 39f),
            LinearLayout.LayoutParams(dp(58), dp(58))
        )

        val serverNames = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        serverNames.addView(label(selectedServer, 18f, white, true))
        serverNames.addView(label(selectedCity, 14f, lavender))

        serverRow.addView(serverNames, LinearLayout.LayoutParams(0, dp(58), 1f))
        serverRow.addView(label("▂ ▅ ▇", 20f, purple, true))

        serverCard.addView(serverRow)
        page.addView(serverCard)

        space(page, 18)

        val mapFrame = FrameLayout(this).apply {
            background = GradientDrawable().apply {
                setColor(Color.rgb(8, 4, 22))
                cornerRadius = dp(23).toFloat()
                setStroke(dp(1), Color.rgb(112, 25, 190))
            }
            clipToOutline = true
            addView(WorldMapView(), FrameLayout.LayoutParams(-1, dp(155)))
        }

        page.addView(mapFrame, LinearLayout.LayoutParams(-1, dp(155)))
        space(page, 18)

        val protection = panel()
        protection.addView(label("ТВОЯ ЗАЩИТА", 15f, white, true))
        space(protection, 12)

        val stats = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
        }

        listOf(
            Triple("♢", "Анонимность", "—"),
            Triple("ϟ", "Скорость", "—"),
            Triple("▢", "Безопасность", "—")
        ).forEach { item ->
            val col = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
            }

            col.addView(label(item.first, 32f, lavender, true).apply {
                gravity = Gravity.CENTER
            })

            col.addView(label(item.second, 11f, lavender).apply {
                gravity = Gravity.CENTER
            })

            col.addView(label(item.third, 14f, white, true).apply {
                gravity = Gravity.CENTER
            })

            stats.addView(col, LinearLayout.LayoutParams(0, dp(82), 1f))
        }

        protection.addView(stats)
        page.addView(protection)

        root.addView(navigation(), LinearLayout.LayoutParams(-1, dp(76)))

        showAnimated(root)
    }

    private fun flag(country: String) = when (country) {
        "США" -> "🇺🇸"
        "Англия" -> "🇬🇧"
        "Нидерланды" -> "🇳🇱"
        "Германия" -> "🇩🇪"
        "Франция" -> "🇫🇷"
        else -> "🌐"
    }

    private fun navigation(): LinearLayout {
        val nav = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            setPadding(dp(4), dp(8), dp(4), dp(8))

            background = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(Color.rgb(17, 7, 34), Color.rgb(7, 3, 18))
            ).apply {
                cornerRadii = floatArrayOf(
                    dp(22).toFloat(), dp(22).toFloat(),
                    dp(22).toFloat(), dp(22).toFloat(),
                    0f, 0f, 0f, 0f
                )
                setStroke(dp(1), Color.rgb(83, 20, 142))
            }
        }

        val tabs = listOf(
            "⌂" to "Главная",
            "◎" to "Серверы",
            "▥" to "Статистика",
            "♙" to "Профиль",
            "⚙" to "Настройки"
        )

        tabs.forEachIndexed { index, tab ->
            val active = index == currentTab
            val tabColor = if (active) pink else lavender

            val col = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER

                setOnClickListener {
                    animateTap(it) {
                        when (index) {
                            0 -> showHome()
                            1 -> showServers()
                            2 -> showInfo("СТАТИСТИКА")
                            3 -> showInfo("ПРОФИЛЬ")
                            else -> showInfo("НАСТРОЙКИ")
                        }
                    }
                }
            }

            col.addView(label(tab.first, 26f, tabColor, true).apply {
                gravity = Gravity.CENTER
                if (active) {
                    setShadowLayer(dp(9).toFloat(), 0f, 0f, pink)
                }
            })

            col.addView(label(tab.second, 10f, tabColor).apply {
                gravity = Gravity.CENTER
            })

            nav.addView(col, LinearLayout.LayoutParams(0, dp(60), 1f))
        }

        return nav
    }

    private fun showServers() {
        currentTab = 1

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(bg)
        }

        val scroll = ScrollView(this)

        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(20), dp(24), dp(20), dp(20))
            setBackgroundColor(bg)
        }

        content.addView(label("NIVORANT", 28f, white, true))
        content.addView(label("ВЫБЕРИ СЕРВЕР", 17f, lavender, true))
        space(content, 20)

        val servers = listOf(
            Triple("🇺🇸", "США", "New York"),
            Triple("🇬🇧", "Англия", "London"),
            Triple("🇳🇱", "Нидерланды", "Amsterdam"),
            Triple("🇩🇪", "Германия", "Frankfurt"),
            Triple("🇫🇷", "Франция", "Paris")
        )

        servers.forEach { item ->
            val box = panel()

            val row = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
            }

            row.addView(label(item.first, 34f))

            val names = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(dp(14), 0, 0, 0)
            }

            names.addView(label(item.second, 17f, white, true))
            names.addView(label(item.third, 13f, lavender))

            row.addView(names, LinearLayout.LayoutParams(0, dp(54), 1f))
            row.addView(label("›", 28f, purple, true))

            box.addView(row)

            box.setOnClickListener {
                animateTap(it) {
                    selectedServer = item.second
                    selectedCity = item.third
                    showHome()

                    Toast.makeText(
                        this@MainActivity,
                        "Выбран сервер: ${item.second}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            content.addView(box, LinearLayout.LayoutParams(-1, dp(82)).apply {
                bottomMargin = dp(12)
            })
        }

        scroll.addView(content)
        root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))
        root.addView(navigation(), LinearLayout.LayoutParams(-1, dp(76)))

        showAnimated(root)
    }

    private fun showInfo(title: String) {
        currentTab = when (title) {
            "СТАТИСТИКА" -> 2
            "ПРОФИЛЬ" -> 3
            else -> 4
        }

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(bg)
        }

        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(dp(24), dp(24), dp(24), dp(24))
        }

        content.addView(label("NIVORANT", 29f, white, true).apply {
            gravity = Gravity.CENTER
        })

        content.addView(label(title, 21f, lavender, true).apply {
            gravity = Gravity.CENTER
            setPadding(0, dp(20), 0, dp(20))
        })

        content.addView(
            label(
                "Этот раздел будет доступен после настройки VPN-сервиса.",
                14f,
                lavender
            ).apply {
                gravity = Gravity.CENTER
            }
        )

        root.addView(content, LinearLayout.LayoutParams(-1, 0, 1f))
        root.addView(navigation(), LinearLayout.LayoutParams(-1, dp(76)))

        showAnimated(root)
    }

    inner class NeonOrbView : View(this) {
        private val p = Paint(Paint.ANTI_ALIAS_FLAG)
        private var angle = 0f

        private val animator = ValueAnimator.ofFloat(0f, 360f).apply {
            duration = 9000L
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener {
                angle = it.animatedValue as Float
                invalidate()
            }
        }

        init {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            animator.start()
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)

            val cx = width / 2f
            val cy = height / 2f
            val r = min(width, height) * 0.29f

            p.style = Paint.Style.STROKE
            p.strokeWidth = dp(2).toFloat()
            p.color = Color.argb(45, 170, 60, 255)
            canvas.drawCircle(cx, cy, r * 1.55f, p)

            p.color = purple
            p.strokeWidth = dp(3).toFloat()
            p.setShadowLayer(dp(14).toFloat(), 0f, 0f, purple)

            canvas.drawArc(
                cx - r * 1.5f, cy - r * 1.5f,
                cx + r * 1.5f, cy + r * 1.5f,
                angle, 85f, false, p
            )

            p.color = pink

            canvas.drawArc(
                cx - r * 1.36f, cy - r * 1.36f,
                cx + r * 1.36f, cy + r * 1.36f,
                -angle * 0.7f, 48f, false, p
            )

            p.style = Paint.Style.FILL
            p.color = Color.rgb(25, 8, 48)
            p.setShadowLayer(dp(25).toFloat(), 0f, 0f, purple)
            canvas.drawCircle(cx, cy, r, p)

            p.clearShadowLayer()
            p.style = Paint.Style.STROKE
            p.strokeWidth = dp(3).toFloat()
            p.color = pink
            p.setShadowLayer(dp(12).toFloat(), 0f, 0f, pink)
            canvas.drawCircle(cx, cy, r * 0.82f, p)

            p.clearShadowLayer()
            p.style = Paint.Style.STROKE
            p.strokeWidth = dp(7).toFloat()
            p.strokeCap = Paint.Cap.ROUND
            p.color = white

            canvas.drawArc(
                cx - r * 0.34f, cy - r * 0.34f,
                cx + r * 0.34f, cy + r * 0.34f,
                135f, 270f, false, p
            )
        }

        override fun onDetachedFromWindow() {
            animator.cancel()
            super.onDetachedFromWindow()
        }
    }

    inner class WorldMapView : View(this) {
        private val p = Paint(Paint.ANTI_ALIAS_FLAG)

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)

            val w = width.toFloat()
            val h = height.toFloat()

            p.style = Paint.Style.STROKE
            p.strokeWidth = dp(1).toFloat()
            p.color = Color.argb(35, 175, 100, 255)

            for (i in 1..5) {
                canvas.drawLine(w * i / 6f, 0f, w * i / 6f, h, p)
            }

            for (i in 1..3) {
                canvas.drawLine(0f, h * i / 4f, w, h * i / 4f, p)
            }

            p.style = Paint.Style.FILL
            p.color = Color.rgb(30, 18, 57)

            val land = Path().apply {
                moveTo(w * .08f, h * .32f)
                lineTo(w * .18f, h * .18f)
                lineTo(w * .31f, h * .23f)
                li