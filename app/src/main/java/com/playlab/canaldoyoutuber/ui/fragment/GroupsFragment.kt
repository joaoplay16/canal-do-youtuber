package com.playlab.canaldoyoutuber.ui.fragment

import android.os.Bundle
import android.view.View
import com.playlab.canaldoyoutuber.R
import com.playlab.canaldoyoutuber.data.fixed.GroupsData
import com.playlab.canaldoyoutuber.model.Group
import com.playlab.canaldoyoutuber.ui.adapter.ListItemAdapter

/**
 * Contém a lista de grupos do canal YouTube do
 * app.
 *
 * @constructor cria um objeto completo do tipo
 * [GroupsFragment].
 */
class GroupsFragment : FrameworkListFragment() {
    companion object {
        /**
         * Constante com o identificador único do
         * fragmento [GroupsFragment] para que
         * ele seja encontrado na pilha de fragmentos
         * e assim não seja necessária a construção
         * de mais de um objeto deste fragmento em
         * memória enquanto o aplicativo estiver em
         * execução.
         */
        const val KEY = "GroupsFragment_key"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUiModel(
            titleText = getString(R.string.groups_content_title),
            subTitleText = getString(R.string.groups_desc)
        )
        val adapter = ListItemAdapter(
            context = requireActivity(),
            items = GroupsData.getGroups(),
            callExternalAppCallback = { item ->
                callExternalApp(
                    webUri = item.getWebUri(),
                    failMessage = String.format(
                        getString(R.string.groups_toast_alert),
                        (item as Group).place,
                        item.name
                    )
                )
            }
        )
        initList(adapter = adapter)
    }
}