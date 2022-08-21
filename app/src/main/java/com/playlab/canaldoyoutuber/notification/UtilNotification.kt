package com.playlab.canaldoyoutuber.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.playlab.canaldoyoutuber.R
import com.playlab.canaldoyoutuber.ui.MainActivity

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
         * Constante que contém o identificador único
         * de Notification Channel da notificação push.
         */
        const val CHANNEL_ID = "new_channel_video"

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

    /**
     * Configura e retorna uma [PendingIntent] que
     * acionará a [MainActivity] do aplicativo caso
     * a notificação push do app seja acionada pelo
     * usuário.
     *
     * @return [PendingIntent] configurada para
     * abertura de app.
     */
    private fun getPendingIntent() : PendingIntent {

        val intent = Intent(
            context,
            MainActivity::class.java
        )
        .apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT,
        )

        return pendingIntent
    }

}