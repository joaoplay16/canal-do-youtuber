package com.playlab.canaldoyoutuber.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.playlab.canaldoyoutuber.R
import com.playlab.canaldoyoutuber.config.YouTubeConfig

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
}
