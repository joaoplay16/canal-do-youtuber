package com.playlab.canaldoyoutuber

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.onesignal.OneSignal
import com.playlab.canaldoyoutuber.config.OneSignalConfig
import com.playlab.canaldoyoutuber.network.worker.CatchChannelDataWorker
import java.util.concurrent.TimeUnit

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
        backgroundWork()
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
        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(OneSignalConfig.App.ID);
    }

    /**
     * Inicializa o WorkManager para execuções
     * não precisas, mas intervaladas, em
     * background.
     */
    private fun backgroundWork(){

        val request = PeriodicWorkRequestBuilder<CatchChannelDataWorker>(
            CatchChannelDataWorker.REPEAT_INTERVAL,
            TimeUnit.HOURS
        ).build()

        /**
         * Configuração de WorkManager que
         * garante que mesmo com uma
         * "re-invocação" de enfileiramento de
         * "work" não haverá work repetido em
         * lista de execução do WorkManager.
         */
        WorkManager
            .getInstance( this )
            .enqueueUniquePeriodicWork(
                CatchChannelDataWorker.NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
    }

}