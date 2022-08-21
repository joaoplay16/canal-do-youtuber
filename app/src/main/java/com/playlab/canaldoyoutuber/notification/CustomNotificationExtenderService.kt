package com.playlab.canaldoyoutuber.notification
import com.onesignal.NotificationExtenderService
import com.onesignal.OSNotificationReceivedResult
import com.playlab.canaldoyoutuber.config.OneSignalConfig
import org.json.JSONObject

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
}