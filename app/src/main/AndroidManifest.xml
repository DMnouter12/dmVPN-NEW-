
package com.dmvpn.app

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.*

class MainActivity : Activity() {

    private val bg = Color.rgb(9, 8, 18)
    private val card = Color.rgb(33, 26, 50)
    private val card2 = Color.rgb(25, 20, 39)
    private val purple = Color.rgb(155, 92, 255)
    private val lilac = Color.rgb(190, 150, 255)
    private val muted = Color.rgb(169, 155, 191)
    private val white = Color.WHITE

    private val countries = listOf(
        Triple("🇺🇸", "United States", "New York"),
        Triple("🇬🇧", "United Kingdom", "London"),
        Triple("🇳🇱", "Netherlands", "Amsterdam"),
        Triple("🇩🇪", "Germany", "Frankfurt"),
        Triple("🇫🇷", "France", "Paris")
    )

    private var selectedCountry = 0
    private var page = "home"
    private lateinit var pageContent: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg
        render()
    }

    private fun dp(v: Int) =
        (v * resources.displayMetrics.density).toInt()

    private fun shape(
        color: Int,
        radius: Int = 22,
        stroke: Int = 0
    ) = GradientDrawable().apply {
        setColor(color)
        cornerRadius = dp(radius).toFloat()
        if (stroke != 0) setStroke(dp(1), stroke)
    }

    private fun gradient(
        top: Int,
        bottom: Int,
        radius: Int = 26
    ) = GradientDrawable(
        GradientDrawable.Orientation.TL_BR,
        intArrayOf(top, bottom)
    ).apply {
        cornerRadius = dp(radius).toFloat()
        setStroke(dp(1), Color.argb(45, 190, 150, 255))
    }

    private fun label(
        value: String,
        size: Float,
        color: Int = white,
        bold: Boolean = false,
        gravity: Int = Gravity.START
    ) = TextView(this).apply {
        text = value
        textSize = size
        setTextColor(color)
        this.gravity = gravity
        if (bold) {
            typeface = Typeface.create("sans-serif", Typeface.BOLD)
        }
    }

    private fun gap(height: Int) =
        View(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                1, dp(height)
            )
        }

    private fun column() =
        LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

    private fun row() =
        LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

    private fun cardView(padding: Int = 18) =
        column().apply {
            background = shape(
                card, 22, Color.rgb(55, 42, 78)
            )
            setPadding(
                dp(padding), dp(padding),
                dp(padding), dp(padding)
            )
        }

    private fun primaryButton(
        text: String,
        action: () -> Unit
    ) = label(
        text, 15f, white, true, Gravity.CENTER
    ).apply {
        background = gradient(
            Color.rgb(166, 105, 255),
            Color.rgb(112, 55, 210),
            18
        )
        setPadding(
            dp(18), dp(17), dp(18), dp(17)
        )
        elevation = dp(7).toFloat()
        setOnClickListener { action() }
    }

    private fun secondaryButton(
        text: String,
        action: () -> Unit
    ) = label(
        text, 14f, lilac, true, Gravity.CENTER
    ).apply {
        background = shape(
            card2, 16, Color.rgb(65, 49, 91)
        )
        setPadding(
            dp(16), dp(15), dp(16), dp(15)
        )
        setOnClickListener { action() }
    }

    private fun render() {
        val outer = column().apply {
            setBackgroundColor(bg)
            setPadding(
                dp(20), dp(12), dp(20), dp(8)
            )
        }

        val header = row()
        val brand = column()

        brand.addView(
            label("NIVORANT", 25f, white, true)
        )
        brand.addView(
            label("YOUR PRIVATE NETWORK", 9f, muted, true)
        )

        header.addView(
            brand,
            LinearLayout.LayoutParams(0, -2, 1f)
        )
        header.addView(
            label("●  PROTECTION", 10f, lilac, true)
        )

        outer.addView(header)
        outer.addView(gap(18))

        val title = when (page) {
            "home" -> "Welcome back"
            "servers" -> "Choose server"
            "stats" -> "Statistics"
            "settings" -> "Settings"
            else -> "About NIVORANT"
        }

        outer.addView(label(title, 23f, white, true))
        outer.addView(gap(12))

        val scroll = ScrollView(this)

        pageContent = column().apply {
            setPadding(0, dp(2), 0, dp(18))
        }

        scroll.addView(pageContent)

        outer.addView(
            scroll,
            LinearLayout.LayoutParams(-1, 0, 1f)
        )

        val nav = row().apply {
            background = shape(
                card2, 20, Color.rgb(55, 42, 78)
            )
            setPadding(
                dp(5), dp(8), dp(5), dp(8)
            )
        }

        addNavItem(nav, "⌂", "Home", "home")
        addNavItem(nav, "◎", "Servers", "servers")
        addNavItem(nav, "▥", "Stats", "stats")
        addNavItem(nav, "⚙", "Settings", "settings")

        outer.addView(
            nav,
            LinearLayout.LayoutParams(-1, dp(66))
        )

        setContentView(outer)

        when (page) {
            "home" -> home()
            "servers" -> servers()
            "stats" -> stats()
            "settings" -> settings()
            else -> about()
        }

        pageContent.alpha = 0f
        pageContent.animate()
            .alpha(1f)
            .setDuration(220)
            .setInterpolator(DecelerateInterpolator())
            .start()
    }

    private fun addNavItem(
        nav: LinearLayout,
        symbol: String,
        title: String,
        target: String
    ) {
        val active = page == target

        val item = column().apply {
            gravity = Gravity.CENTER
            setPadding(
                dp(8), dp(2), dp(8), dp(2)
            )

            if (active) {
                background = shape(
                    Color.rgb(54, 38, 79), 14
                )
            }

            setOnClickListener {
                page = target
                render()
            }
        }

        item.addView(
            label(
                symbol, 21f,
                if (active) lilac else muted,
                true, Gravity.CENTER
            )
        )

        item.addView(
            label(
                title, 10f,
                if (active) white else muted,
                active, Gravity.CENTER
            )
        )

        nav.addView(
            item,
            LinearLayout.LayoutParams(0, -1, 1f)
        )
    }

    private fun home() {
        pageContent.addView(
            label(
                "Your privacy matters. Browse with more peace of mind.",
                13f, muted
            )
        )

        pageContent.addView(gap(20))

        val hero = column().apply {
            gravity = Gravity.CENTER
            background = gradient(
                Color.rgb(43, 29, 67),
                Color.rgb(20, 16, 32), 28
            )
            setPadding(
                dp(20), dp(24), dp(20), dp(24)
            )
        }

        val orb = label(
            "✧", 54f, lilac, true, Gravity.CENTER
        ).apply {
            background = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(Color.rgb(48, 31, 75))
                setStroke(dp(2), purple)
            }

            layoutParams = LinearLayout.LayoutParams(
                dp(112), dp(112)
            ).apply {
                gravity = Gravity.CENTER
            }

            elevation = dp(12).toFloat()
        }

        hero.addView(orb)
        hero.addView(gap(16))

        hero.addView(
            label(
                "●  NOT CONNECTED",
                13f, lilac, true, Gravity.CENTER
            )
        )

        hero.addView(gap(7))

        hero.addView(
            label(
                "Your connection is not protected",
                12f, muted, false, Gravity.CENTER
            )
        )

        hero.addView(gap(20))

        hero.addView(
            primaryButton("CONNECT  →") {
                Toast.makeText(
                    this,
                    "VPN server is not configured yet. No connection was made.",
                    Toast.LENGTH_LONG
                ).show()
            },
            LinearLayout.LayoutParams(-1, dp(56))
        )

        pageContent.addView(hero)
        pageContent.addView(gap(16))

        val serverCard = cardView()
        val serverHead = row()

        serverHead.addView(
            label("SELECTED SERVER", 10f, muted, true),
            LinearLayout.LayoutParams(0, -2, 1f)
        )

        serverHead.addView(
            label("CHANGE  ›", 11f, lilac, true).apply {
                setOnClickListener {
                    page = "servers"
                    render()
                }
            }
        )

        serverCard.addView(serverHead)
        serverCard.addView(gap(14))

        val country = countries[selectedCountry]
        val serverRow = row()

        serverRow.addView(label(country.first, 30f))

        val info = column().apply {
            setPadding(dp(12), 0, 0, 0)
        }

        info.addView(
            label(country.second, 16f, white, true)
        )

        info.addView(
            label(
                country.third + " • " + country.second,
                11f, muted
            )
        )

        serverRow.addView(
            info,
            LinearLayout.LayoutParams(0, -2, 1f)
        )

        serverRow.addView(label("›", 25f, muted))
        serverCard.addView(serverRow)

        pageContent.addView(serverCard)
        pageContent.addView(gap(14))

        val metrics = row()

        metrics.addView(
            metric("↓", "0 MB", "DOWNLOAD"),
            LinearLayout.LayoutParams(0, -2, 1f)
        )

        metrics.addView(gap(10))

        metrics.addView(
            metric("↑", "0 MB", "UPLOAD"),
            LinearLayout.LayoutParams(0, -2, 1f)
        )

        pageContent.addView(metrics)
        pageContent.addView(gap(12))

        pageContent.addView(
            label(
                "VPN ENGINE NOT CONFIGURED",
                10f, Color.rgb(135, 121, 154),
                true, Gravity.CENTER
            )
        )
    }

    private fun metric(
        icon: String,
        value: String,
        caption: String
    ): LinearLayout {
        val box = cardView(15)

        box.addView(label(icon, 19f, lilac, true))
        box.addView(gap(5))
        box.addView(label(value, 20f, white, true))
        box.addView(label(caption, 9f, muted, true))

        return box
    }

    private fun servers() {
        pageContent.addView(
            label(
                "Choose a location for your future VPN connection.",
                13f, muted
            )
        )

        pageContent.addView(gap(16))

        countries.forEachIndexed { index, country ->

            val item = row().apply {
                background = shape(
                    if (index == selectedCountry)
                        Color.rgb(48, 34, 69)
                    else card,
                    18,
                    if (index == selectedCountry)
                        purple
                    else Color.rgb(55, 42, 78)
                )

                setPadding(
                    dp(16), dp(15), dp(16), dp(15)
                )

                setOnClickListener {
                    selectedCountry = index

                    Toast.makeText(
                        this@MainActivity,
                        country.second + " selected",
                        Toast.LENGTH_SHORT
                    ).show()

                    render()
                }
            }

            item.addView(label(country.first, 29f))

            val details = column().apply {
                setPadding(dp(13), 0, 0, 0)
            }

            details.addView(
                label(country.second, 15f, white, true)
            )

            details.addView(
                label(country.third, 11f, muted)
            )

            item.addView(
                details,
                LinearLayout.LayoutParams(0, -2, 1f)
            )

            item.addView(
                label(
                    if (index == selectedCountry) "✓" else "›",
                    19f, lilac, true
                )
            )

            pageContent.addView(item)
            pageContent.addView(gap(10))
        }

        pageContent.addView(gap(8))

        pageContent.addView(
            label(
                "Server locations are display options only until VPN servers are configured.",
                11f, muted
            )
        )
    }

    private fun stats() {
        pageContent.addView(
            label("YOUR SESSION", 10f, muted, true)
        )

        pageContent.addView(gap(12))

        val session = cardView(20)

        session.addView(
            label(
                "00:00:00", 32f, white, true,
                Gravity.CENTER
            )
        )

        session.addView(
            label(
                "Connection time", 11f, muted,
                false, Gravity.CENTER
            )
        )

        pageContent.addView(session)
        pageContent.addView(gap(12))

        val metrics = row()

        metrics.addView(
            metric("↓", "0 MB", "DOWNLOADED"),
            LinearLayout.LayoutParams(0, -2, 1f)
        )

        metrics.addView(gap(10))

        metrics.addView(
            metric("↑", "0 MB", "UPLOADED"),
            LinearLayout.LayoutParams(0, -2, 1f)
        )

        pageContent.addView(metrics)
        pageContent.addView(gap(16))

        pageContent.addView(
            label(
                "No real VPN session yet. Statistics will become available after the VPN engine is connected.",
                12f, muted
            )
        )
    }

    private fun settings() {
        pageContent.addView(
            label("PERSONALIZE YOUR APP", 10f, muted, true)
        )

        pageContent.addView(gap(12))

        settingRow("App theme", "Dark • NIVORANT purple")
        settingRow(
            "Auto-connect",
            "Unavailable until VPN engine is added"
        )
        settingRow(
            "Notifications",
            "Connection status alerts"
        )
        settingRow("Protocol", "Not configured")

        pageContent.addView(gap(16))

        pageContent.addView(
            secondaryButton("ABOUT NIVORANT  →") {
                page = "about"
                render()
            }
        )

        pageContent.addView(gap(16))

        pageContent.addView(
            label(
                "Version 1.0 • Interface preview",
                11f, muted, false, Gravity.CENTER
            )
        )
    }

    private fun settingRow(
        title: String,
        subtitle: String
    ) {
        val box = cardView(16)

        box.addView(
            label(title, 14f, white, true)
        )

        box.addView(gap(4))

        box.addView(
            label(subtitle, 11f, muted)
        )

        pageContent.addView(box)
        pageContent.addView(gap(9))
    }

    private fun about() {
        val box = cardView(22).apply {
            gravity = Gravity.CENTER
        }

        box.addView(
            label(
                "NIVORANT", 27f, lilac, true,
                Gravity.CENTER
            )
        )

        box.addView(gap(8))

        box.addView(
            label(
                "YOUR PRIVATE NETWORK",
                10f, muted, true, Gravity.CENTER
            )
        )

        box.addView(gap(22))

        box.addView(
            label(
                "A modern privacy-focused VPN app interface.",
                14f, white, false, Gravity.CENTER
            )
        )

        box.addView(gap(16))

        box.addView(
            label(
                "This build contains the app design and navigation. A real VPN connection still requires a VPN service, server infrastructure and secure configuration.",
                12f, muted, false, Gravity.CENTER
            )
        )

        pageContent.addView(box)
        pageContent.addView(gap(16))

        pageContent.addView(
            secondaryButton("BACK TO HOME") {
                page = "home"
                render()
            }
        )
    }
}
