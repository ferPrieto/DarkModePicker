package prieto.fernando.darkpicker.ui

import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.TranslateAnimation
import androidx.lifecycle.ViewModelProviders
import prieto.fernando.darkpicker.R
import prieto.fernando.darkpicker.model.Theme
import prieto.fernando.darkpicker.presentation.MainViewModel
import prieto.fernando.darkpicker.util.ThemeProvider
import prieto.fernando.darkpicker.widget.ColorSeekBar
import prieto.fernando.darkpicker.widget.ThemeMode
import javax.inject.Inject
import kotlinx.android.synthetic.main.main_activity.color_seek_bar as colorSeekBar
import kotlinx.android.synthetic.main.main_activity.fab as floatingActionButton
import kotlinx.android.synthetic.main.main_activity.theme_selected as themeSelected


class MainActivity : BaseActivity<MainViewModel>() {
    @Inject
    protected lateinit var themeProvider: ThemeProvider

    private val themes = mutableListOf<Theme>()
    private var seekBarDragged = false

    override fun onResume() {
        super.onResume()
        viewModel.inputs.applyTheme(ThemeMode.DARK)
        seekBarDragged = false
        animateFloatingButton()
    }

    private fun animateFloatingButton() {
        val animation = TranslateAnimation(0f, 0f, 500f, 0f)
        animation.duration = 500
        val viewPropertyAnimator = floatingActionButton.animate()
        viewPropertyAnimator.interpolator = AccelerateDecelerateInterpolator()
        floatingActionButton.startAnimation(animation)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(themeProvider.getSelectedStyle())

        setContentView(R.layout.main_activity)
        prepareThemeData()
        themeSelected.setTheme(themes[1])

        colorSeekBar.setOnColorChangeListener(object : ColorSeekBar.OnColorChangeListener {
            override fun onColorChangeListener(color: Int) {
                val hexadecimalColour = getHexadecimalColour(color)
                themeProvider.setSelectedColour(hexadecimalColour)
                this@MainActivity.recreate()
            }
        })
    }

    private fun getHexadecimalColour(colour: Int) = String.format("#%06X", 0xFFFFFF and colour)

    private fun prepareThemeData() {
        themes.clear()
        themes.addAll(themeProvider.getThemeList())
    }

    override val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this, vmFactory).get(MainViewModel::class.java)
    }
}
