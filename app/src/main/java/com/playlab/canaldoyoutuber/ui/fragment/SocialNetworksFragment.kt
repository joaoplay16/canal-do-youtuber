package com.playlab.canaldoyoutuber.ui.fragment

import android.os.Bundle
import android.view.View
import com.playlab.canaldoyoutuber.R
import com.playlab.canaldoyoutuber.data.fixed.SocialNetworksData
import com.playlab.canaldoyoutuber.model.SocialNetwork
import com.playlab.canaldoyoutuber.ui.adapter.ListItemAdapter

/**
 * Contém a lista de redes do canal YouTube do
 * app.
 *
 * @constructor cria um objeto completo do tipo
 * [SocialNetworksFragment].
 */

class SocialNetworksFragment : FrameworkListFragment() {
    companion object {
        /**
         * Constante com o identificador único do
         * fragmento [SocialNetworksFragment] para que
         * ele seja encontrado na pilha de fragmentos
         * e assim não seja necessária a construção
         * de mais de um objeto deste fragmento em
         * memória enquanto o aplicativo estiver em
         * execução.
         */
        const val KEY = "SocialNetworksFragment_key"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUiModel(
            titleText = getString(
                R.string.social_networks_content_title
            )
        )
        val adapter = ListItemAdapter(
            context = requireActivity(),
            items = SocialNetworksData.getNetworks(),
            callExternalAppCallback = { item ->
                callExternalApp(
                    webUri = item.getWebUri(),
                    appUri = item.getAppUri(),
                    failMessage = String.format(
                        getString(
                            R.string.social_network_toast_alert
                        ),
                        (item as SocialNetwork).network
                    )
                )
            }
        )
        initList(adapter = adapter)
    }
}