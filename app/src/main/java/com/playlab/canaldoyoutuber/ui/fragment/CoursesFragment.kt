package com.playlab.canaldoyoutuber.ui.fragment

import android.os.Bundle
import android.view.View
import com.playlab.canaldoyoutuber.R
import com.playlab.canaldoyoutuber.data.fixed.CoursesData
import com.playlab.canaldoyoutuber.model.Course
import com.playlab.canaldoyoutuber.ui.adapter.ListItemAdapter

/**
 * Contém a lista de cursos vinculados ou apenas
 * indicados pelo canal YouTube do app.
 *
 * @constructor cria um objeto completo do tipo
 * [CoursesFragment].
 */

class CoursesFragment : FrameworkListFragment() {
    companion object {
        /**
         * Constante com o identificador único do
         * fragmento [CoursesFragment] para que
         * ele seja encontrado na pilha de fragmentos
         * e assim não seja necessária a construção
         * de mais de um objeto deste fragmento em
         * memória enquanto o aplicativo estiver em
         * execução.
         */
        const val KEY = "CoursesFragment_key"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUiModel(
            titleText = getString(R.string.courses_content_title)
        )
        val adapter = ListItemAdapter(
            context = requireActivity(),
            items = CoursesData.getCourses(),
            callExternalAppCallback = { item ->
                callExternalApp(
                    webUri = item.getWebUri(),
                    appUri = item.getAppUri(),
                    failMessage = String.format(
                        getString(R.string.course_toast_alert),
                        (item as Course).title
                    )
                )
            }
        )
        initList(adapter = adapter)
    }
}