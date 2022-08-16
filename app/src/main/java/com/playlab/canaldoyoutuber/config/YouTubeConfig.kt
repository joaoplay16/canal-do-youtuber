package com.playlab.canaldoyoutuber.config

/**
 * Classe que contém os principais dados estáticos de
 * configuração de acesso à YouTube Data API. E também
 * dados para acesso a recursos alternativos externos
 * do YouTube.
 *
 * As classes internas ([Key], [Channel], [ApiV3] e [Notification])
 * e também os rótulos de todos os companion object.
 * Estes estão presentes em código somente para
 * facilitar a leitura dele. Ou seja, em termos de
 * regras de sintaxe esses não são obrigatórios.
 */
abstract class YouTubeConfig {
}
abstract class Channel {
    companion object {
        /**
         * Constante com o identificador único do
         * canal. Com esse ID é possível
         * carregar da YouTube Data API os dados do
         * canal correto.
         */
        const val CHANNEL_ID = "UCG3gFuIkRF3PpNkRk3Wp6dw"
    }
}
/**
 * Constante com a URL do canal.
 */
const val CHANNEL_URL = "https:"//""www.youtube.com/channel/"CHANNEL_ID
