package com.playlab.canaldoyoutuber.notification

import android.content.Context
import android.net.UrlQuerySanitizer
import android.util.Log
import com.onesignal.OSNotificationReceivedEvent
import com.onesignal.OneSignal.OSRemoteNotificationReceivedHandler
import com.playlab.canaldoyoutuber.config.OneSignalConfig
import com.playlab.canaldoyoutuber.config.YouTubeConfig
import com.playlab.canaldoyoutuber.data.dynamic.UtilDatabase
import com.playlab.canaldoyoutuber.model.LastVideo
import org.json.JSONObject
import java.net.URI

class NotificationServiceExtension : OSRemoteNotificationReceivedHandler {
    override fun remoteNotificationReceived(
        context: Context,
        notificationReceivedEvent: OSNotificationReceivedEvent
    ) {
        val notification = notificationReceivedEvent.notification

        val data = notification.additionalData
        Log.i("OneSignal", "Received Notification Data: $data")

        val lastVideo = getLastVideoFromJson(
            json = data
        )
        if( lastVideo != null ){
            ifNewLastVideoThenSaveAndNotify( context, lastVideo = lastVideo )
        }

        notificationReceivedEvent.complete(null)
    }

    /**
     * Retorna o dado de descrição do "último vídeo" se
     * ele estiver presente no JSON de dados que chegou
     * junto a notificação OneSignal.
     *
     * @param json dados JSON obtidos de notificação.
     * @return dado de descrição do vídeo.
     */
    private fun getDescriptionFromJson(json: JSONObject) =
        if (!json.isNull(OneSignalConfig.Notification.DESCRIPTION)) {
            json.getString(OneSignalConfig.Notification.DESCRIPTION)
        } else {
            OneSignalConfig.Notification.EMPTY
        }

    /**
     * Gera um objeto [LastVideo] completo de acordo com
     * os dados JSON recebidos de notificação OneSignal.
     *
     * @param json dados JSON obtidos de notificação.
     * @return objeto [LastVideo] completo.
     */
    private fun getLastVideoFromJson(
        json: JSONObject?
    ): LastVideo? {
        /**
         * Padrão Cláusula de guarda. Se as premissas não
         * estiverem presentes, então nem mesmo continue
         * com a execução.
         */
        if (json == null
            || json.isNull(OneSignalConfig.Notification.VIDEO)
            || json.isNull(OneSignalConfig.Notification.TITLE)
        ) {
            return null
        }

        val url = json.getString(OneSignalConfig.Notification.VIDEO)
        val title = json.getString(OneSignalConfig.Notification.TITLE)
        var lastVideo: LastVideo? = null

        if (!url.isEmpty() && !title.isEmpty()) {
            val urlQuery = UrlQuerySanitizer(url)

            if (!urlQuery.getValue(YouTubeConfig.Notification.VIDEO_PARAM)
                    .isNullOrEmpty()
            ) {
                lastVideo = LastVideo(
                    uid = urlQuery.getValue(
                        YouTubeConfig.Notification.VIDEO_PARAM
                    ),
                    title = title,
                    description = getDescriptionFromJson(json)
                )
                    .apply {
                        thumbUrl = YouTubeConfig.Notification.EMPTY
                    }
            } else if (url.contains(
                    YouTubeConfig.Notification.ALTERNATIVE_URL
                )
            ) {

                val uri = URI(url)
                val path: String = uri.getPath()
                val uid = path
                    .substring(
                        path.lastIndexOf('/') + 1
                    )

                lastVideo = LastVideo(
                    uid = uid,
                    title = title,
                    description = getDescriptionFromJson(json)
                )
                    .apply {
                        thumbUrl = YouTubeConfig.Notification.EMPTY
                    }
            }
        }
        return lastVideo
    }

    /**
     * Salva em banco de dados e notifica o usuário
     * caso os dados que chegaram ao app sejam de um
     * novo "último vídeo" liberado no canal YouTube
     * do aplicativo.
     *
     * @param lastVideo último vídeo liberado em canal e
     * que chegou ao aplicativo.
     */
    private fun ifNewLastVideoThenSaveAndNotify(context: Context, lastVideo: LastVideo) {

        UtilDatabase
            .getInstance(context = context)
            .getLastVideo {

                if (it == null
                    || !it.uid.equals(lastVideo.uid)
                    || !it.title.equals(lastVideo.title)
                    || !it.description.equals(lastVideo.description)
                ) {

                    UtilDatabase
                        .getInstance(context = context)
                        .saveLastVideo(lastVideo = lastVideo)

                    UtilNotification
                        .getInstance(context = context)
                        .createBigPictureNotification(
                            lastVideo = lastVideo
                        )
                }
            }
    }
}