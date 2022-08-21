package com.playlab.canaldoyoutuber.notification
import android.net.UrlQuerySanitizer
import com.onesignal.NotificationExtenderService
import com.onesignal.OSNotificationReceivedResult
import com.playlab.canaldoyoutuber.config.OneSignalConfig
import com.playlab.canaldoyoutuber.config.YouTubeConfig
import com.playlab.canaldoyoutuber.model.LastVideo
import org.json.JSONObject
import java.net.URI

/**
 * Serviço responsável por interceptar a notificação
 * OneSignal enviada ao aparelho e assim trabalhar
 * a configuração personalizada dessa notificação
 * push.
 *
 * Este serviço deve também estar configurado no
 * AndroidManifest.xml do app como a seguir:
 *
 * <service
 *      android:name=".notification.CustomNotificationExtenderService"
 *      android:exported="false"
 *      android:permission="android.permission.BIND_JOB_SERVICE">
 *          <intent-filter>
 *              <action
 *                  android:name="com.onesignal.NotificationExtender" />
 *          </intent-filter>
 * </service>
 *
 * @constructor cria um objeto completo do tipo
 * [CustomNotificationExtenderService].
 */

class CustomNotificationExtenderService: NotificationExtenderService() {

    /**
     * Processa a notificação OneSignal que foi
     * enviada ao aparelho.
     *
     * @param notification notificação OneSignal.
     * @return um [Boolean] que indica se o
     * comportamento comum de geração de notificação
     * da OneSignal API deve (false) ou não (true)
     * continuar.
     */
    override fun onNotificationProcessing(
        notification: OSNotificationReceivedResult?
    ): Boolean {

        /* TODO */
        return true
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
            || json.isNull (OneSignalConfig.Notification.TITLE) ){
            return null
        }

        val url = json.getString(OneSignalConfig.Notification.VIDEO)
        val title = json.getString(OneSignalConfig.Notification.TITLE)
        var lastVideo: LastVideo? = null

        if (!url.isEmpty() && !title.isEmpty()){
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
                    YouTubeConfig.Notification.ALTERNATIVE_URL)) {

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
}