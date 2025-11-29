package com.honeycomb.disciplineapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView

class BlockActivity : Activity() {

    companion object {
        const val EXTRA_PACKAGE_NAME = "extra_pkg"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make fullscreen and keep screen on
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        // Minimal UI programmatically
        val tv = TextView(this).apply {
            text = "App blocked"
            textSize = 24f
            setPadding(50, 200, 50, 20)
        }
        val btn = Button(this).apply {
            text = "Return to Home"
            setOnClickListener {
                // Return to home (so blocked app is no longer foreground)
                val home = Intent(Intent.ACTION_MAIN).apply {
                    addCategory(Intent.CATEGORY_HOME)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(home)
                finish()
            }
        }

        val layout = android.widget.LinearLayout(this).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            addView(tv)
            addView(btn)
        }
        setContentView(layout)
    }
}