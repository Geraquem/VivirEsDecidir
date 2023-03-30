package com.mmfsin.quepreferirias.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mmfsin.quepreferirias.databinding.ActivityMainBinding
import com.mmfsin.quepreferirias.domain.models.DataDTO

class MainActivity : AppCompatActivity(), MainView {

    private lateinit var binding: ActivityMainBinding

    private lateinit var dataList: List<DataDTO>
    private var position = 0

    private val presenter by lazy { MainPresenter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUI()
        presenter.getData()
    }

    private fun setUI() {
        binding.apply {
//            showWelcomeDialog()
            loadingScreen.root.visibility = View.VISIBLE
        }
    }

    override fun firebaseReady(dataList: List<DataDTO>) {
        this.dataList = dataList
        setSingleData(dataList[position])
    }

    override fun setSingleData(data: DataDTO) {
        binding.apply {
            tvTextTop.text = data.textA
            tvTextBottom.text = data.textB
            loadingScreen.root.visibility = View.GONE
        }
    }
}