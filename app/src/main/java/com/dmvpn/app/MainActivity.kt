package com.dmvpn.app

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.LinearGradient
import android.graphics.RadialGradient
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.widget.*
import android.graphics.RectF

class MainActivity : Activity() {

    private val bg = Color.rgb(4, 9, 23)
    private val panel = Color.rgb(12, 23, 45)
    private val blue = Color.rgb(30, 125, 255)
    private val cyan = Color.rgb(0, 205, 255)
    private val white = Color.WHITE
    private val muted = Color.rgb(155, 174, 205)

    private var connected = false
    private var selectedCountry = "🇺🇸  США"
    private var currentPage = "home"
    private lateinit var root: LinearLayout
    private lateinit var status: TextView

    private val countries = listOf(
        "🇺🇸  США",
        "🇩🇪  Германия",
        "🇫🇷  Франция",
        "🇳🇱  Нидерланды",
        "🇬🇧  Англия"
    )

    private fun shape(
        color: Int,
        radius: Float = 24f,
        stroke: Int = Color.rgb(30, 85, 155)
    ): GradientDrawable {
        return GradientDrawable().apply {
            setColor(color)
            cornerRadius = radius
            setStroke(1, stroke)
        }
    }

    private fun label(
        value: String,
        size: Float,
        color: Int = white,
        bold: Boolean = false
    ): TextView {
        return TextView(this).apply {
            text = value
            textSize = size
            setTextColor(color)
            gravity = Gravity.CENTER
            if (bold) typeface = Typeface.DEFAULT_BOLD
        }
    }

    private fun space(height: Int) {
        root.addView(View(this),
            LinearLayout.LayoutParams(1, height))
    }

    private fun line() {
        val v = View(this)
        v.setBackgroundColor(Color.rgb(27, 48, 78))
        val p = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 1)
        p.setMargins(0, 15, 0, 15)
        root.addView(v, p)
    }

    private fun panelBox(): LinearLayout {
        return LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(18, 16, 18, 16)
            background = shape(panel)
        }
    }

    private fun addPanel(
        title: String,
        subtitle: String
    ) {
        val box = panelBox()
        box.addView(label(title, 17f, white, true))
        val sub = label(subtitle, 13f, muted)
        val p = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        p.topMargin = 8
        box.addView(sub, p)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        params.bottomMargin = 12
        root.addView(box, params)
    }

    private fun actionButton(
        value: String,
        onClick: () -> Unit
    ): TextView {
        return TextView(this).apply {
            text = value
            textSize = 15f
            gravity = Gravity.CENTER
            setTextColor(white)
            typeface = Typeface.DEFAULT_BOLD
            background = shape(Color.rgb(15, 65, 135), 20f)
            setPadding(16, 17, 16, 17)
            setOnClickListener { onClick() }
        }
    }

    private fun addButton(
        value: String,
        onClick: () -> Unit
    ) {
        val b = actionButton(value, onClick)
        val p = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        p.bottomMargin = 11
        root.addView(b, p)
    }

    private fun header(title: String) {
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        val back = actionButton("‹", { showPage("home") })
        row.addView(back, LinearLayout.LayoutParams(52, 48))

        val name = label(title, 22f, white, true)
        row.addView(name, LinearLayout.LayoutParams(
            0, 55, 1f))

        root.addView(row)
        space(12)
    }

    private fun base(title: String) {
        currentPage = title
        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(20, 18, 20, 18)
            background = GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                intArrayOf(
                    Color.rgb(3, 8, 22),
                    Color.rgb(9, 24, 52),
                    Color.rgb(3, 8, 20)
                )
            )
        }

        val scroll = ScrollView(this)
        scroll.isFillViewport = true
        scroll.addView(root)
        setContentView(scroll)
    }

    private fun showPage(page: String) {
        when (page) {
            "home" -> showHome()
            "servers" -> showServers()
            "stats" -> showStats()
            "settings" -> showSettings()
            "pro" -> showPro()
            "menu" -> showMenu()
            "about" -> showAbout()
            "help" -> showHelp()
        }
    }

    private fun showHome() {
        base("home")

        val brand = label("♛", 34f, cyan, true)
        root.addView(brand)
        root.addView(label("NIVORANT", 32f, white, true))
        root.addView(label("V P N", 13f, cyan, true))
        space(12)

        val globe = panelBox()
        globe.addView(label("🌐", 65f, cyan))
        globe.addView(label(
            "ГЛОБАЛЬНАЯ ЗАЩИТА",
            12f, cyan, true))
        globe.addView(label(
            "Твой цифровой мир",
            15f, muted))
        val gp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 145)
        root.addView(globe, gp)

        space(22)

        val connect = TextView(this).apply {
            text = if (connected) "Ⅱ" else "⏻"
            textSize = 52f
            gravity = Gravity.CENTER
            setTextColor(white)
            typeface = Typeface.DEFAULT_BOLD
            background = GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                intArrayOf(
                    Color.rgb(15, 95, 220),
                    Color.rgb(0, 190, 255),
                    Color.rgb(15, 75, 190)
                )
            ).apply {
                shape = GradientDrawable.OVAL
                setStroke(4, Color.rgb(75, 175, 255))
            }
            elevation = 18f
            setOnClickListener {
                connected = !connected
                showHome()
                if (connected) {
                    Toast.makeText(
                        this@MainActivity,
                        "Демо: VPN-сервер ещё не подключён",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        val cp = LinearLayout.LayoutParams(174, 174)
        cp.gravity = Gravity.CENTER
        root.addView(connect, cp)

        space(12)

        status = label(
            if (connected) "ДЕМО-РЕЖИМ" else "НЕ ПОДКЛЮЧЕНО",
            15f,
            if (connected) cyan else muted,
            true
        )
        root.addView(status)
        space(18)

        val server = panelBox()
        server.addView(label("ВЫБРАННЫЙ СЕРВЕР", 12f, cyan, true))
        server.addView(label(selectedCountry, 22f, white, true))
        server.addView(label(
            "Состояние: серверы ещё не подключены",
            12f, muted))
        server.setOnClickListener { showServers() }
        root.addView(server, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 112))

        space(14)
        addPanel("🛡  Шифрование", "Защищённое соединение WireGuard — в разработке")
        addPanel("⚡  Скорость", "Показатели появятся после подключения сервера")
        addPanel("🌍  Серверы", "Выбирай расположение сервера")

        addButton("🌐  ВЫБРАТЬ СЕРВЕРЫ") {
            showServers()
        }
        addButton("📊  СТАТИСТИКА") {
            showStats()
        }
        addButton("⚙  НАСТРОЙКИ") {
            showSettings()
        }
        addButton("👑  NIVORANT PRO") {
            showPro()
        }
        addButton("⋮  ЕЩЁ") {
            showMenu()
        }

        footer()
    }

    private fun showServers() {
        base("servers")
        header("ВЫБОР СЕРВЕРА")

        root.addView(label(
            "ВЫБЕРИ СТРАНУ",
            13f, cyan, true))
        space(14)

        for (country in countries) {
            val selected = country == selectedCountry
            val b = actionButton(
                (if (selected) "✓  " else "　") + country +
                    "\n" + if (selected) "Выбрано" else "Выбрать"
            ) {
                selectedCountry = country
                showServers()
            }

            val p = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 76)
            p.bottomMargin = 12
            root.addView(b, p)
        }

        line()
        addPanel("Информация",
            "Реальный пинг и состояние серверов появятся после настройки инфраструктуры.")

        addButton("←  НА ГЛАВНУЮ") {
            showHome()
        }
        footer()
    }

    private fun showStats() {
        base("stats")
        header("СТАТИСТИКА")

        addPanel("⏱  Время подключения", "Появится после реального подключения")
        addPanel("⬇  Получено данных", "—")
        addPanel("⬆  Отправлено данных", "—")
        addPanel("⚡  Скорость соединения", "—")
        addPanel("🌍  Текущий сервер", selectedCountry)

        addButton("←  НА ГЛАВНУЮ") {
            showHome()
        }
        footer()
    }

    private fun showSettings() {
        base("settings")
        header("НАСТРОЙКИ")

        addPanel("⚙  Основные настройки",
            "Управление приложением NIVORANT VPN")

        settingSwitch("Автоподключение")
        settingSwitch("Уведомления")
        settingSwitch("Запуск при включении телефона")
        settingSwitch("Вибрация при нажатии")

        addPanel("🔐  Безопасность",
            "Настройки VPN-защиты будут доступны после интеграции WireGuard.")

        addButton("←  НА ГЛАВНУЮ") {
            showHome()
        }
        footer()
    }

    private fun settingSwitch(title: String) {
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(14, 8, 14, 8)
            background = shape(panel)
        }

        val t = label(title, 14f, white)
        t.gravity = Gravity.CENTER_VERTICAL
        row.addView(t, LinearLayout.LayoutParams(0, 55, 1f))

        val sw = Switch(this)
        sw.buttonTintList = android.content.res.ColorStateList.valueOf(cyan)
        row.addView(sw)

        val p = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        p.bottomMargin = 10
        root.addView(row, p)
    }

    private fun showPro() {
        base("pro")
        header("NIVORANT PRO")

        space(20)
        root.addView(label("♛", 60f, cyan, true))
        root.addView(label("NIVORANT PRO", 29f, white, true))
        root.addView(label(
            "ПРЕМИАЛЬНАЯ ЗАЩИТА",
            13f, cyan, true))
        space(24)

        addPanel("⚡  Больше возможностей",
            "Дополнительные функции и расширенные настройки.")
        addPanel("🌍  Выбор серверов",
            "Доступ к доступным локациям.")
        addPanel("🚫  Без рекламы",
            "Планируемая возможность PRO.")
        addPanel("🛡  Приоритет",
            "Дополнительные возможности обслуживания.")

        addButton("ПОДПИСКА PRO — СКОРО") {
            Toast.makeText(
                this,
                "Покупки пока не настроены",
                Toast.LENGTH_SHORT
            ).show()
        }

        addPanel("Информация",
            "Платёжная система и подписки будут добавлены отдельно.")
        addButton("←  НА ГЛАВНУЮ") {
            showHome()
        }
        footer()
    }

    private fun showMenu() {
        base("menu")
        header("МЕНЮ")

        root.addView(label("♛", 60f, cyan, true))
        root.addView(label("NIVORANT VPN", 25f, white, true))
        root.addView(label("ТВОЯ ЗАЩИТА В СЕТИ", 12f, cyan, true))
        space(25)

        addButton("📊   Статистика") { showStats() }
        addButton("⚙   Настройки") { showSettings() }
        addButton("🌍   Выбор серверов") { showServers() }
        addButton("👑   Купить PRO") { showPro() }
        addButton("ℹ   О приложении") { showAbout() }
        addButton("❔   Помощь") { showHelp() }
        addButton("⌂   Главная") { showHome() }

        footer()
    }

    private fun showAbout() {
        base("about")
        header("О ПРИЛОЖЕНИИ")

        root.addView(label("♛", 65f, cyan, true))
        root.addView(label("NIVORANT VPN", 27f, white, true))
        space(15)

        addPanel("Версия", "1.0.0")
        addPanel("Разработчик", "NIVORANT")
        addPanel("Платформа", "Android")
        addPanel("Технология", "WireGuard — интеграция в разработке")

        addButton("←  НАЗАД") { showMenu() }
        footer()
    }

    private fun showHelp() {
        base("help")
        header("ПОМОЩЬ")

        addPanel("Не подключается VPN?",
            "Настоящие серверы ещё находятся в разработке.")
        addPanel("Как выбрать страну?",
            "Открой раздел серверов и нажми на нужную страну.")
        addPanel("Что такое PRO?",
            "Премиальные функции, которые будут добавлены позже.")

        addButton("←  НАЗАД") { showMenu() }
        footer()
    }

    private fun footer() {
        line()
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
        }

        val items = listOf(
            "⌂\nГлавная" to "home",
            "🌍\nСерверы" to "servers",
            "📊\nСтатистика" to "stats",
            "⚙\nНастройки" to "settings"
        )

        for ((name, page) in items) {
            val item = label(
                name, 12f,
                if (currentPage == page) cyan else muted,
                true
            )
            item.setOnClickListener { showPage(page) }
            row.addView(item, LinearLayout.LayoutParams(0, 60, 1f))
        }

        root.addView(row)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg
        showHome()
    }

    override fun onBackPressed() {
        showHome()
    }
}
