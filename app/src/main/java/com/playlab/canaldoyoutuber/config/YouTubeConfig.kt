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

    abstract class Channel {
        companion object {
            /**
             * Constante com o identificador único do
             * canal. Com esse ID é possível
             * carregar da YouTube Data API os dados do
             * canal correto.
             */
            const val CHANNEL_ID = "UCG3gFuIkRF3PpNkRk3Wp6dw"

            /**
             * Constante com a URL do canal.
             */
            const val CHANNEL_URL = "https://www.youtube.com/channel/$CHANNEL_ID"

            const val VIDEO_URL_TEMPLATE = "https://www." +
                    "youtube.com/watch?v=%s"

            const val VIDEO_THUMB_URL_TEMPLATE = "https://i." +
                    "ytimg.com/vi/%s/hqdefault.jpg"

            const val PLAYLIST_URL_TEMPLATE = "https://www.youtube." +
                    "com/playlist?list=%s"

        }
    }

    abstract class Notification {
        companion object {
            /**
             * Constantes com definições para acesso aos
             * dados de novo "último vídeo" liberado no
             * canal YouTube do app. Dados presentes em
             * URL de vídeo na notificação push OneSignal.
             */
            const val ALTERNATIVE_URL = "https://youtu.be/"
            const val VIDEO_PARAM = "v"
            const val EMPTY = ""
        }
    }

    abstract class Key {
        companion object {
            /**
             * Constante com a chave de API do Google
             * para que seja possível realizar consultas
             * à YouTube Data API.
             */
            const val GOOGLE_DEV = "AIzaSyCkWpwvwi_MAJmvgB5ZbYRazTuNF0U12Zc"
        }

    }

    abstract class ApiV3 {
        companion object {
            /**
             * Constante com a URL base para acesso à
             * YouTube Data API.
             */
            const val BASE_URL = "https://www.googleapis.com/"

            /**
             * Constantes com os caminhos URL para
             * acesso aos dados corretos (vídeo e
             * PlayLists).
             */
            const val VIDEO_PATH = "youtube/v3/search"
            const val PLAYLISTS_PATH = "youtube/v3/playlists"

            /**
             * Constantes com as definições de
             * parâmetros que devem estar junto a URL
             * final de acesso a dados via YouTube Data
             * API.
             */
            const val PART_PARAM = "snippet"
            const val MAX_RESULTS_VIDEO_PARAM = "1"
            const val MAX_RESULTS_PLAYLISTS_PARAM = "500"
            const val ORDER_PARAM = "date"
        }
    }
}