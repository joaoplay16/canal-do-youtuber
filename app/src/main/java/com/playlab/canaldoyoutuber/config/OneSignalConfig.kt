package com.playlab.canaldoyoutuber.config

/**
 * Classe que contém todos os dados estáticos de
 * configuração de notificações push no sistema
 * via API OneSignal.
 *
 * As classes internas ([Firebase], [App], ...)
 * e também os rótulos de todos os companion object.
 * Estes estão presentes em código somente para
 * facilitar a leitura dele. Ou seja, em termos de
 * regras de sintaxe esses não são obrigatórios.
 */
abstract class OneSignalConfig {

    abstract class Firebase {
        companion object CloudMessage {
            /**
             * Constantes com dados de configuração
             * do Firebase Cloud Message. Dados que
             * devem ser colocados no dashboard do
             * OneSignal.
             */
            const val SERVER_KEY = "AAAAc06clkc:APA91bGAYgq5hiPHtp59B6CHJfVmm" +
                    "7xFUoKxXRUu_UVRf7iC-6U1dXImYHteotoaCSy2hcMaeyW87hMoPNR29v" +
                    "-8E_CVTQF2m0_xByskiIxuDcmKIDnOW7MODQtNz8o0TShPx2t5fwVR"
            const val SENDER_ID = "495240123975"
        }
    }

    abstract class App {
        companion object {
            /**
             * Constante com dado de configuração
             * do OneSignal em aplicativo. Esse dado
             * entra na variável onesignal_app_id do
             * Gradle Nível de Aplicativo, ou
             * build.gradle (Module: app).
             */
            const val ID = "40e0af51-6308-4300-8515-8ae4593f24f2"
        }
    }
}
