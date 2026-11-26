
package com.dmvpn.app

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.widget.*

class MainActivity : Activity() {
    private val bg = Color.rgb(10, 9, 18)
    private val panel = Color.rgb(25, 21, 39)
    private val purple = Color.rgb(155, 92, 255)
    private val white = Color.WHITE
    private var selectedCountry = "USA"
    private var connected = false
    private lateinit var root: LinearLayout
    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showHome()
    }

    private fun text(value: String, size: Float, color: Int = white): TextView {
        return TextView(this).apply {
            text = value
            textSize = size
            setTextColor(color)
            gravity = Gravity.CENTER
        }
    }

    private fun button(value: String, action: () -> Unit): Button {
        return Button(this).apply {
            text = value
            textSize = 15f
            setTextColor(white)
            setBackgroundColor(purple)
            setOnClickListener { action() }
        }
    }

    private fun makeRoot(title: String) {
        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(22, 28, 22, 18)
            setBackgroundColor(bg)
        }

        val scroll = ScrollView(this)
        scroll.addView(root)
        setContentView(scroll)

        root.addView(text("NIVORANT", 30f, purple))
        root.addView(text("VPN  •  YOUR PRIVATE CONNECTION", 11f, Color.LTGRAY))
        val space = View(this)
        root.addView(space, LinearLayout.LayoutParams(1, 28))
        root.addView(text(title, 23f))
    }

    private fun showHome() {
        makeRoot("Welcome back")
        root.addView(text("Your privacy matters.", 14f, Color.LTGRAY))
        val space = View(this)
        root.addView(space, LinearLayout.LayoutParams(1, 38))

        root.addView(text("●", 58f, purple))
        root.addView(text(if (connected) "CONNECTED (DEMO)" else "NOT CONNECTED", 18f))
        root.addView(text("Selected location: $selectedCountry", 14f, Color.LTGRAY))

        val connect = button(
            if (connected) "DISCONNECT" else "CONNECT"
        ) {
            connected = !connected
            showHome()
            Toast.makeText(
                this,
                "Demo only: VPN server is not configured yet",
                Toast.LENGTH_LONG
            ).show()
        }

        root.addView(connect, LinearLayout.LayoutParams(-1, 58).apply {
            topMargin = 28
            bottomMargin = 15
        })

        root.addView(button("Choose server") { showServers() })
        root.addView(button("Statistics") { showStats() })
        root.addView(button("Settings") { showSettings() })
        root.addView(button("About NIVORANT") { showAbout() })
        root.addView(text("Secure interface • VPN engine not configured", 11f, Color.GRAY))
    }

    private fun showServers() {
        makeRoot("Choose location")
        val countries = listOf(
            "🇺🇸  USA",
            "🇩🇪  Germany",
            "🇫🇷  France",
            "🇳🇱  Netherlands",
            "🇬🇧  England"
        )

        countries.forEach { country ->
            root.addView(button(country) {
                selectedCountry = country.substringAfter("  ")
                showServers()
                Toast.makeText(this, "Selected: $selectedCountry", Toast.LENGTH_SHORT).show()
            }, LinearLayout.LayoutParams(-1, 58).apply {
                topMargin = 7
                bottomMargin = 7
            })
        }

        root.addView(button("Back to home") { showHome() })
    }

    private fun showStats() {
        makeRoot("Statistics")
        root.addView(text("Session traffic", 17f))
        root.addView(text("Download: 0 MB", 20f, purple))
        root.addView(text("Upload: 0 MB", 20f, purple))
        root.addView(text("No real VPN session yet.", 13f, Color.LTGRAY))
        root.addView(button("Back") { showHome() })
    }

    private fun showSettings() {
        makeRoot("Settings")
        root.addView(text("NIVORANT VPN", 20f, purple))
        root.addView(text("Version 1.0", 14f, Color.LTGRAY))
        root.addView(text("Application settings will be expanded in future updates.", 14f))
        root.addView(button("Back") { showHome() })
    }

    private fun showAbout() {
        makeRoot("About")
        root.addView(text("NIVORANT VPN", 24f, purple))
        root.addView(text("A privacy-focused VPN application.", 15f))
        root.addView(text("The current build contains the app interface only. A real VPN connection requires a configured VPN engine and server.", 14f, Color.LTGRAY))
        root.addView(button("Back") { showHome() })
    }
}
