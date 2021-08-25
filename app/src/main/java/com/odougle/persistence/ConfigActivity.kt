package com.odougle.persistence

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.odougle.persistence.databinding.ActivityConfigBinding
import com.odougle.persistence.databinding.ActivityMainBinding

class ConfigActivity : AppCompatActivity() {

    private val binding: ActivityConfigBinding by lazy {
        ActivityConfigBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}