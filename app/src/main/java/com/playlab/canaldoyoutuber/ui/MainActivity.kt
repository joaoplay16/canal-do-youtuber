package com.playlab.canaldoyoutuber.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.playlab.canaldoyoutuber.R
import com.playlab.canaldoyoutuber.config.YouTubeConfig
import com.playlab.canaldoyoutuber.data.fixed.MenuItemsData
import com.playlab.canaldoyoutuber.databinding.ActivityMainBinding
import com.playlab.canaldoyoutuber.ui.adapter.MenuAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomMenu()
    }

    /**
     * Invoca o aplicativo do YouTube para que o usuário
     * tenha acesso direto ao canal.
     *
     * Esse listener de clique está vinculado aos
     * componentes visuais do topo do aplicativo.
     *
     * Caso o aplicativo nativo do YouTube e nem mesmo um
     * navegador Web esteja instalado no aparelho (algo
     * utópico), então uma mensagem de falha é apresentada
     * ao usuário.
     *
     * @param view componente visual que teve o evento de
     * toque (clique) disparado.
     */
    fun openYouTubeChannel( view: View){
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse( YouTubeConfig.Channel.CHANNEL_URL )
        )
        if( intent.resolveActivity( packageManager ) != null ){
            startActivity( intent )
        }
        else{
            Toast
                .makeText(
                    this,
                    getString( R.string.channel_toast_alert ),
                    Toast.LENGTH_LONG
                )
                .show()
        }
    }

    /**
     * Inicializa o menu principal do aplicativo, o
     * BottomMenu.
     */
    private fun initBottomMenu(){
        val layoutManager = GridLayoutManager(
            this,
            MenuAdapter.NUMBER_COLUMNS,
            RecyclerView.HORIZONTAL,
            false
        )
        with(binding.rvMenu) {
            this.layoutManager = layoutManager
            setHasFixedSize(true)
            adapter = MenuAdapter(
                context = this@MainActivity,
                items = MenuItemsData.getItems(res = resources),
                changeFragmentCallback = { item ->
                    { /* TODO */ }
                }
            )
        }
    }

}
