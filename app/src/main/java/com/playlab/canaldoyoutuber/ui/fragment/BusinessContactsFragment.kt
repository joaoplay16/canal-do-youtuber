package com.playlab.canaldoyoutuber.ui.fragment

import android.os.Bundle
import android.view.View
import com.playlab.canaldoyoutuber.R
import com.playlab.canaldoyoutuber.data.fixed.BusinessContactData
import com.playlab.canaldoyoutuber.model.BusinessContact
import com.playlab.canaldoyoutuber.ui.adapter.ListItemAdapter

/**
 * Contém a lista de contatos comerciais do canal
 * YouTube do app.
 *
 * @constructor cria um objeto completo do tipo
 * [BusinessContactsFragment].
 */

class BusinessContactsFragment : FrameworkListFragment() {
    companion object {
        /**
         * Constante com o identificador único do
         * fragmento [BusinessContactsFragment] para que
         * ele seja encontrado na pilha de fragmentos
         * e assim não seja necessária a construção
         * de mais de um objeto deste fragmento em
         * memória enquanto o aplicativo estiver em
         * execução.
         */
        const val KEY = "BusinessContactsFragment_key"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUiModel(
            titleText = getString(R.string.business_contacts_content_title),
            subTitleText = getString( R.string.business_contacts_desc )
        )
        val adapter = ListItemAdapter(
            context = requireActivity(),
            items = BusinessContactData.getBusinessContacts(),
            callExternalAppCallback = { item ->
                callExternalApp(
                    webUri = item.getWebUri(),
                    appUri = item.getAppUri(),
                    failMessage = String.format(
                        getString(R.string.business_contact_toast_alert),
                        (item as BusinessContact).place,
                        item.contact
                    )
                )
            }
        )
        initList(adapter = adapter)
    }
}