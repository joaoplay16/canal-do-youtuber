package com.playlab.canaldoyoutuber

import android.app.Application
import com.onesignal.OneSignal

/**
 * Classe de sistema e de objeto único enquanto
 * o aplicativo estiver em execução.
 *
 * Com essa característica está é a classe
 * responsável por iniciar as entidades de
 * WorkManager (trabalho em background) e
 * OneSignal (notificação push). Entidades essas
 * que precisam ser invocadas logo no início da
 * execução do aplicativo ainda na primeira vez
 * que ele é acessado pelo usuário.
 *
 * Note que para essa classe ser invocada pelo
 * sistema ela precisa estar configurada no
 * AndroidManifest.xml como:
 *
 * <application
 *
android:name=".CustomApplication"
 *
...>
 *
 * @constructor cria um objeto completo do tipo
 * [CustomApplication].
 */
class CustomApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        oneSignalInit()
    }
    /**
    * Inicializa o OneSignal e registra o
    * usuário do app para que ele já consiga
    * receber as notificações push do canal
    * do aplicativo.
    *
    * Com a configuração a seguir é preciso
    * que também tenha definido no aplicativo
    * um serviço do tipo
    * [NotificationExtenderService] para que as
    * notificações push sejam interceptadas
    * e trabalhadas de maneira personalizada.
    */
    private fun oneSignalInit() {
        OneSignal.startInit( this )
            .inFocusDisplaying(
                OneSignal.OSInFocusDisplayOption.Notification
            )
            .unsubscribeWhenNotificationsAreDisabled( true )
            .init()
    }
}