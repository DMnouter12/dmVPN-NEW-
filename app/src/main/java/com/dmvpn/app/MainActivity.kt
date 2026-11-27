
package com.dmvpn.app

import android.app.Activity
import android.os.Bundle
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.view.animation.LinearInterpolator
import android.animation.ValueAnimator
import android.widget.*
import android.graphics.Typeface

class MainActivity : Activity() {

    private val bg = Color.rgb(8, 7, 16)
    private val panel = Color.rgb(20, 17, 32)
    private val purple = Color.rgb(151, 75, 255)
    private val pink = Color.rgb(220, 74, 255)
    private val white = Color.rgb(245, 240, 255)
    private val muted = Color.rgb(145, 135, 165)

    private lateinit var root: LinearLayout
    private lateinit var content: FrameLayout
    private var selectedServer = 0
    private var currentPage = "Главная"

    private val servers = listOf(
        Triple("США", "New York", "US"),
        Triple("Великобритания", "London", "UK"),
        Triple("Нидерланды", "Amsterdam", "NL"),
        Triple("Германия", "Frankfurt", "DE"),
        Triple("Франция", "Paris", "FR")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg
        window.decorView.systemUiVisibility = 0
        showApp()
    }

    private fun showApp() {
        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(bg)
        }

        val header = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(22, 18, 22, 12)
        }

        val brand = TextView(this).apply {
            text = "NIVORANT"
            textSize = 23f
            letterSpacing = 0.16f
            setTextColor(white)
            typeface = Typeface.create("sans-serif-black", Typeface.BOLD)
        }

        val subtitle = TextView(this).apply {
            text = "YOUR PRIVATE NETWORK"
            textSize = 9f
            letterSpacing = 0.22f
            setTextColor(muted)
        }

        header.addView(brand)
        header.addView(subtitle)
        root.addView(header)

        content = FrameLayout(this)
        root.addView(content, LinearLayout.LayoutParams(
            -1, 0, 1f
        ))

        val nav = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            setPadding(5, 9, 5, 13)
            setBackgroundColor(Color.rgb(13, 11, 22))
        }

        val tabs = listOf(
            "Главная" to "⌂",
            "Серверы" to "◎",
            "Статистика" to "◷",
            "Профиль" to "♙",
            "Настройки" to "⚙"
        )

        tabs.forEach { item ->
            val button = TextView(this).apply {
                text = "${item.second}\n${item.first}"
                gravity = Gravity.CENTER
                textSize = 10f
                setTextColor(if (item.first == currentPage) pink else muted)
                setPadding(3, 5, 3, 5)
                setOnClickListener { openPage(item.first) }
            }
            nav.addView(button, LinearLayout.LayoutParams(0, -2, 1f))
        }

        root.addView(nav)
        setContentView(root)
        openPage("Главная")
    }

    private fun openPage(page: String) {
        currentPage = page
        content.removeAllViews()

        val scroll = ScrollView(this).apply {
            isFillViewport = true
            clipToPadding = false
        }

        val column = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(20, 10, 20, 28)
        }

        when (page) {
            "Главная" -> homePage(column)
            "Серверы" -> serversPage(column)
            "Статистика" -> statsPage(column)
            "Профиль" -> profilePage(column)
            "Настройки" -> settingsPage(column)
        }

        scroll.addView(column)
        content.addView(scroll)
    }

    private fun homePage(box: LinearLayout) {
        title(box, "Добро пожаловать")
        text(box, "Твоя приватность начинается здесь", muted, 13f)

        val orb = GlowOrbView()
        box.addView(orb, LinearLayout.LayoutParams(-1, 245).apply {
            topMargin = 10
        })

        text(box, "НЕ ПОДКЛЮЧЕНО", pink, 12f, true, Gravity.CENTER)
        text(box, "Твоё соединение пока не защищено", muted, 12f, false, Gravity.CENTER)

        val connect = button("ВКЛЮЧИТЬ ЗАЩИТУ")
        box.addView(connect, LinearLayout.LayoutParams(-1, 54).apply {
            topMargin = 22
        })
        connect.setOnClickListener {
            Toast.makeText(
                this,
                "VPN-сервер ещё не настроен. Подключение не выполнялось.",
                Toast.LENGTH_LONG
            ).show()
        }

        section(box, "ВЫБРАННЫЙ СЕРВЕР")
        val server = servers[selectedServer]
        val serverCard = card()
        serverCard.addView(textView(
            "🌐   ${server.first}  ·  ${server.second}",
            white, 15f, true
        ))
        serverCard.addView(textView(
            "Нажми, чтобы выбрать другой сервер",
            muted, 11f
        ))
        serverCard.setOnClickListener { openPage("Серверы") }
        box.addView(serverCard)

        section(box, "ТВОЯ ЗАЩИТА")
        val info = card()
        info.addView(textView("◆  Современный интерфейс", white, 13f))
        info.addView(textView("◆  Выбор расположения сервера", white, 13f))
        info.addView(textView("◆  Настройки соединения", white, 13f))
        box.addView(info)
    }

    private fun serversPage(box: LinearLayout) {
        title(box, "Серверы")
        text(box, "Выбери расположение для соединения", muted, 13f)
        box.addView(MapView(), LinearLayout.LayoutParams(-1, 190).apply {
            topMargin = 18
            bottomMargin = 12
        })

        servers.forEachIndexed { index, server ->
            val item = card()
            item.orientation = LinearLayout.HORIZONTAL
            item.gravity = Gravity.CENTER_VERTICAL

            val details = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
            }
            details.addView(textView(server.first, white, 15f, true))
            details.addView(textView(server.second, muted, 11f))

            item.addView(details, LinearLayout.LayoutParams(0, -2, 1f))
            item.addView(textView(
                if (index == selectedServer) "ВЫБРАНО ✓" else "ВЫБРАТЬ",
                if (index == selectedServer) pink else muted,
                10f, true
            ))

            item.setOnClickListener {
                selectedServer = index
                openPage("Серверы")
            }
            box.addView(item)
        }
    }

    private fun statsPage(box: LinearLayout) {
        title(box, "Статистика")
        text(box, "Информация о твоём соединении", muted, 13f)

        section(box, "ТЕКУЩИЙ СТАТУС")
        val status = card()
        status.addView(textView("●  VPN не подключён", pink, 16f, true))
        status.addView(textView(
            "Передача данных через VPN пока не выполняется.",
            muted, 12f
        ))
        box.addView(status)

        section(box, "ПОКАЗАТЕЛИ")
        statCard(box, "Время соединения", "—")
        statCard(box, "Получено данных", "—")
        statCard(box, "Отправлено данных", "—")
        text(box, "Показатели появятся после настройки настоящего VPN-соединения.",
            muted, 11f)
    }

    private fun profilePage(box: LinearLayout) {
        title(box, "Профиль")
        text(box, "Твоё пространство NIVORANT", muted, 13f)

        val profile = card()
        profile.addView(textView("◉", pink, 42f, true))
        profile.addView(textView("Гость NIVORANT", white, 18f, true))
        profile.addView(textView("Личный профиль", muted, 12f))
        box.addView(profile)

        section(box, "АККАУНТ")
        option(box, "Информация о приложении", "Версия 1.0")
        option(box, "Статус аккаунта", "Гостевой режим")
    }

    private fun settingsPage(box: LinearLayout) {
        title(box, "Настройки")
        text(box, "Управляй параметрами приложения", muted, 13f)

        section(box, "ПОДКЛЮЧЕНИЕ")
        option(box, "Протокол", "WireGuard · будет настроен")
        option(box, "Автоподключение", "Выключено")
        option(box, "Kill Switch", "Выключено")

        section(box, "ПРИЛОЖЕНИЕ")
        option(box, "Анимации", "Включены")
        option(box, "Тема", "Тёмная · Neon")
        option(box, "О приложении", "NIVORANT VPN 1.0")

        text(box,
            "Некоторые функции станут доступны после подключения VPN-движка и серверов.",
            muted, 11f)
    }

    private fun title(box: LinearLayout, value: String) {
        box.addView(textView(value, white, 26f, true))
        box.addView(View(this).apply {
            setBackgroundColor(purple)
        }, LinearLayout.LayoutParams(42, 3).apply {
            topMargin = 9
            bottomMargin = 10
        })
    }

    private fun section(box: LinearLayout, value: String) {
        box.addView(textView(value, muted, 10f, true).apply {
            letterSpacing = 0.15f
            setPadding(2, 22, 2, 9)
        })
    }

    private fun text(
        box: LinearLayout,
        value: String,
        color: Int,
        size: Float,
        bold: Boolean = false,
        gravity: Int = Gravity.START
    ) {
        box.addView(textView(value, color, size, bold).apply {
            this.gravity = gravity
        })
    }

    private fun textView(
        value: String,
        color: Int,
        size: Float,
        bold: Boolean = false
    ): TextView {
        return TextView(this).apply {
            text = value
            textSize = size
            setTextColor(color)
            if (bold) typeface = Typeface.DEFAULT_BOLD
            setPadding(0, 4, 0, 4)
        }
    }

    private fun card(): LinearLayout {
        return LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(17, 14, 17, 14)
            background = rounded(panel, 18, Color.rgb(48, 36, 69))
            val params = LinearLayout.LayoutParams(-1, -2)
            params.bottomMargin = 10
            layoutParams = params
        }
    }

    private fun button(value: String): TextView {
        return TextView(this).apply {
            text = value
            gravity = Gravity.CENTER
            textSize = 13f
            letterSpacing = 0.08f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.WHITE)
            background = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(purple, pink)
            ).apply {
                cornerRadius = 18f
            }
        }
    }

    private fun rounded(
        color: Int,
        radius: Int,
        stroke: Int
    ): GradientDrawable {
        return GradientDrawable().apply {
            setColor(color)
            cornerRadius = radius.toFloat()
            setStroke(1, stroke)
        }
    }

    private fun option(box: LinearLayout, name: String, value: String) {
        val item = card()
        item.orientation = LinearLayout.HORIZONTAL
        item.gravity = Gravity.CENTER_VERTICAL
        item.addView(textView(name, white, 13f), LinearLayout.LayoutParams(0, -2, 1f))
        item.addView(textView(value, muted, 11f))
        box.addView(item)
    }

    private fun statCard(box: LinearLayout, name: String, value: String) {
        val item = card()
        item.orientation = LinearLayout.HORIZONTAL
        item.addView(textView(name, white, 13f), LinearLayout.LayoutParams(0, -2, 1f))
        item.addView(textView(value, pink, 15f, true))
        box.addView(item)
    }

    inner class GlowOrbView : View(this) {
        private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        private var angle = 0f
        private var pulse = 0f

        private val animator = ValueAnimator.ofFloat(0f, 360f).apply {
            duration = 9000
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener {
                angle = it.animatedValue as Float
                pulse = (Math.sin(Math.toRadians(angle.toDouble() * 2)) + 1) / 2f
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
            val radius = 76f

            paint.style = Paint.Style.FILL
            paint.color = Color.argb(35, 151, 75, 255)
            canvas.drawCircle(cx, cy, radius + 28 + pulse * 8, paint)

            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 2.5f
            paint.color = purple
            canvas.drawCircle(cx, cy, radius, paint)

            paint.color = Color.argb(150, 220, 74, 255)
            paint.strokeWidth = 1.5f
            canvas.save()
            canvas.rotate(angle, cx, cy)
            canvas.drawOval(
                cx - 100, cy - 35, cx + 100, cy + 35, paint
            )
            canvas.restore()

            paint.style = Paint.Style.FILL
            paint.color = white
            paint.textAlign = Paint.Align.CENTER
            paint.typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
            paint.textSize = 39f
            canvas.drawText("N", cx, cy + 13f, paint)

            paint.textSize = 10f
            paint.color = pink
            paint.letterSpacing = 0.1f
            canvas.drawText("PRIVATE NETWORK", cx, cy + 112f, paint)
        }

        override fun onDetachedFromWindow() {
            animator.cancel()
            super.onDetachedFromWindow()
        }
    }

    inner class MapView : View(this) {
        private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        private var phase = 0f

        private val animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 1800
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {
                phase = it.animatedValue as Float
                invalidate()
            }
        }

        init {
            animator.start()
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            val w = width.toFloat()
            val h = height.toFloat()

            paint.color = Color.rgb(17, 14, 29)
            paint.style = Paint.Style.FILL
            canvas.drawRoundRect(0f, 0f, w, h, 22f, 22f, paint)

            paint.color = Color.rgb(45, 32, 65)
            paint.strokeWidth = 1f
            paint.style = Paint.Style.STROKE
            for (i in 1..5) {
                canvas.drawLine(w * i / 6f, 10f, w * i / 6f, h - 10f, paint)
                canvas.drawLine(10f, h * i / 6f, w - 10f, h * i / 6f, paint)
            }

            paint.style = Paint.Style.FILL
            paint.color = Color.rgb(53, 38, 77)

            val land = Path().apply {
                moveTo(w * .08f, h * .25f)
                lineTo(w * .20f, h * .12f)
                lineTo(w * .34f, h * .20f)
                lineTo(w * .37f, h * .42f)
                lineTo(w * .28f, h * .55f)
                lineTo(w * .23f, h * .84f)
                lineTo(w * .12f, h * .70f)
                close()
                moveTo(w * .43f, h * .16f)
                lineTo(w * .56f, h * .12f)
                lineTo(w * .63f, h * .27f)
                lineTo(w * .58f, h * .43f)
                lineTo(w * .48f, h * .38f)
                close()
                moveTo(w * .66f, h * .22f)
                lineTo(w * .88f, h * .18f)
                lineTo(w * .94f, h * .40f)
                lineTo(w * .83f, h * .55f)
                lineTo(w * .69f, h * .44f)
                close()
            }
            canvas.drawPath(land, paint)

            val nodes = listOf(
                Pair(w * .23f, h * .38f),
                Pair(w * .51f, h * .29f),
                Pair(w * .72f, h * .35f),
                Pair(w * .80f, h * .43f),
                Pair(w * .59f, h * .48f)
            )

            paint.color = Color.argb(65, 220, 74, 255)
            paint.strokeWidth = 1.5f
            for (i in 0 until nodes.size - 1) {
                canvas.drawLine(
                    nodes[i].first, nodes[i].second,
                    nodes[i + 1].first, nodes[i + 1].second, paint
                )
            }

            nodes.forEach { point ->
                paint.style = Paint.Style.FILL
                paint.color = Color.argb((45 + phase * 90).toInt(), 220, 74, 255)
                canvas.drawCircle(point.first, point.second, 9f + phase * 7f, paint)
                paint.color = pink
                canvas.drawCircle(point.first, point.second, 3.5f, paint)
            }
        }

        override fun onDetachedFromWindow() {
            animator.cancel()
            super.onDetachedFromWindow()
        }
    }
}
