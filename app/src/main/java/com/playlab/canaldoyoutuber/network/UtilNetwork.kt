package com.playlab.canaldoyoutuber.network

import android.content.Context
import com.playlab.canaldoyoutuber.config.YouTubeConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Classe utilitária que permite fácil acesso à
 * API de comunicação remota do aplicativo.
 *
 * Assim é possível obter de maneira imediata e
 * não verbosa uma nova comunicação remota.
 *
 * @property context contexto do aplicativo.
 * @constructor cria um objeto completo do tipo
 * [UtilNetwork].
 */
class UtilNetwork private constructor(
    private val context: Context
) {
    companion object {
        /**
         * Propriedade responsável por conter a única
         * instância de [UtilNetwork] disponível
         * durante toda a execução do aplicativo.
         */
        private var instance: UtilNetwork? = null

        /**
         * Método que aplica, junto à propriedade
         * [instance], o padrão Singleton em classe.
         * Garantindo que somente uma instância de
         * [UtilNetwork] estará disponível durante
         * toda a execução do app. Ajudando a
         * diminuir a possibilidade de vazamento
         * de memória.
         *
         * @param context contexto do aplicativo.
         * @return instância única de [UtilNetwork].
         */
        fun getInstance(context: Context): UtilNetwork {
            if (instance == null) {
                instance = UtilNetwork(context = context)
            }
            return instance!!
        }
    }

    /**
     * Retorna um objeto de serviço completo para comunicação
     * remota com o servidor de dados do YouTube.
     *
     * @return serviço Retrofit para comunicação remota.
     */
    private fun getYouTubeService()
            = Retrofit.Builder()
        .baseUrl( YouTubeConfig.ApiV3.BASE_URL )
        .addConverterFactory( GsonConverterFactory.create() )
        .build()
        .create( YouTubeService::class.java )

}