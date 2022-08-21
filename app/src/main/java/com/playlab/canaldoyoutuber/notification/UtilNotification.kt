package com.playlab.canaldoyoutuber.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.playlab.canaldoyoutuber.R
import com.playlab.canaldoyoutuber.config.YouTubeConfig.Channel.Companion.CHANNEL_ID

/**
 * Classe utilitária que permite o fácil acesso à
 * geração de notificações push no app.
 *
 * Assim é possível obter de maneira imediata e
 * não verbosa uma notificação push quando um
 * novo "último vídeo" liberado chega ao aplicativo.
 *
 * @property context contexto do aplicativo.
 * @constructor cria um objeto completo do tipo
 * [UtilNotification].
 */

class UtilNotification private constructor(
    private val context: Context
) {
    companion object {
        /**
         * Propriedade responsável por conter a única
         * instância de [UtilNotification] disponível
         * durante toda a execução do aplicativo.
         */
        private var instance: UtilNotification? = null

        /**
         * Método que aplica, junto à propriedade
         * [instance], o padrão Singleton em classe.
         * Garantindo que somente uma instância de
         * [UtilNotification] estará disponível durante
         * toda a execução do app. Ajudando a
         * diminuir a possibilidade de vazamento
         * de memória.
         *
         * @param context contexto do aplicativo.
         * @return instância única de [UtilNotification].
         */
        fun getInstance(context: Context): UtilNotification {
            if (instance == null) {
                instance = UtilNotification(context = context)
            }
            return instance!!
        }
    }

    /**
     * Cria uma Notification Channel para aparelhos
     * Android com o Android Oreo (API 26) ou superior.
     *
     * Notification Channel é algo necessário nessas
     * versões do Android para a notificação ser gerada.
     */
    @RequiresApi( Build.VERSION_CODES.O )
    private fun createNotificationChannel(){
        val name = context.getString(
            R.string.notification_verbose_name
        )
        val description = context.getString(
            R.string.notification_verbose_description
        )
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(
            CHANNEL_ID,
            name,
            importance
        )
            .apply {
                this.description = description
            }
        val notificationManager = context.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager?

        notificationManager?.createNotificationChannel( channel )
    }
}