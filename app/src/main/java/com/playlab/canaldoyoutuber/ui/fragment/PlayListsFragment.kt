package com.playlab.canaldoyoutuber.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.playlab.canaldoyoutuber.R
import com.playlab.canaldoyoutuber.data.dynamic.UtilDatabase
import com.playlab.canaldoyoutuber.data.fixed.PlayListsData
import com.playlab.canaldoyoutuber.databinding.FragmentPlayListsBinding
import com.playlab.canaldoyoutuber.model.PlayList
import com.playlab.canaldoyoutuber.network.UtilNetwork
import com.playlab.canaldoyoutuber.ui.adapter.ListItemAdapter

/**
 * Contém toda a UI de PlayLists do canal YouTube
 * do app.
 *
 * @constructor cria um objeto completo do tipo
 * [PlayListsFragment].
 */
class PlayListsFragment : Fragment() {

    private lateinit var binding: FragmentPlayListsBinding

    companion object {
        /**
         * Constante com o identificador único do
         * fragmento [PlayListsFragment] para que
         * ele seja encontrado na pilha de fragmentos
         * e assim não seja necessária a construção
         * de mais de um objeto deste fragmento em
         * memória enquanto o aplicativo estiver em
         * execução.
         */
        const val KEY = "PlayListsFragment_key"
    }

    /**
     * [playLists] sempre inicia com alguma lista
     * mutável válida de PlayLists, mesmo que nenhuma
     * PlayList tenha sido ainda enviada ao app e a
     * lista esteja vazia.
     */
    private val playLists = mutableListOf<PlayList>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayListsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()

        initPlayListList()

        if( playLists.isNotEmpty() ){
            setUiModel( pLists = playLists )
        }else{
            /**
             * Todo o algoritmo abaixo é necessário aqui,
             * pois na primeira abertura do aplicativo,
             * quando acessando o fragmento [PlayListsFragment],
             * é possível que a inserção de dados de PlayList
             * no banco de dados local (partindo dos algoritmos
             * em [InitialDataCallback]) não seja rápida o
             * suficiente para os dados já serem apresentados
             * neste fragmento quando o usuário estiver
             * acessando-o pela primeira vez.
             */
            playLists.addAll(PlayListsData.getInitialPlayLists())

            uiDataStatus(status = UiFragLoadDataStatus.LOADING)

            UtilDatabase
                .getInstance( context = requireActivity().applicationContext )
                .getAllPlayLists {
                    val auxPlayList = if( it.isNullOrEmpty() ) {
                        playLists
                    }else {
                        it
                    }
                    setUiModel( pLists = auxPlayList )
                }
        }
    }

    /**
     * Invoca o aplicativo do YouTube para que o usuário
     * tenha acesso a PlayList do canal que foi acionada.
     *
     * Caso o dado de URI presente no objeto [playList] seja
     * inválido para a abertura do app nativo do YouTube
     * ou abertura da versão dele em app de navegador Web,
     * então uma mensagem de falha é apresentada.
     *
     * @param playList objeto [PlayList] do item de lista
     * acionado pelo usuário.
     */
    private fun callYouTubePlayListCallback(
        playList: PlayList
    ) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            playList.getAppUri()
        )
        /**
         * * É utópico, mas pode ocorrer de não haver
         * instalado no aparelho do usuário o aplicativo
         * do YouTube e nem mesmo um navegador Web.
         *
         * Sendo assim, ao invés de gerar uma exceção,
         * nós avisamos ao usuário a necessidade de
         * instalar o aplicativo adequado.
         */
        if (intent.resolveActivity(requireActivity().packageManager) == null) {
            Toast
                .makeText(
                    activity,
                    String.format(
                        getString(R.string.playlist_toast_alert),
                        playList.title
                    ),
                    Toast.LENGTH_LONG
                )
                .show()
            return
        }
        requireActivity().startActivity(intent)
    }

    /**
     * Inicializa por completo o framework de lista
     * [RecyclerView] que deve conter a listagem das
     * PlayLists do canal.
     */
    private fun initPlayListList() {
        val layoutManager = LinearLayoutManager(activity)

        with(binding.rvPlayLists) {
            this.layoutManager = layoutManager
            setHasFixedSize(true)
            this.adapter = ListItemAdapter(
                context = requireActivity(),
                items = playLists,
                callExternalAppCallback = { item ->
                    callYouTubePlayListCallback(
                        playList = item as PlayList
                    )
                }
            )
        }
    }

    /**
     * Configura toda a UI, layout, do fragmento de
     * acordo com o estado atual dos dados que devem
     * ser apresentados em tela.
     *
     * Como é possível ter a invocação deste método
     * fora da Thread Principal, então é importante
     * sempre ter o código de atualização de UI
     * dentro de runOnUiThread().
     *
     * Outro ponto importante é garantir que não
     * haverá NullPointerException caso os dados
     * cheguem em método quando a UI não mais está no
     * foreground (primeiro plano). Assim o operador
     * not null (?.) é utilizado com frequência.
     *
     * @param status estado atual dos dados que
     * devem estar em tela.
     */
    private fun uiDataStatus(status: UiFragLoadDataStatus) {
        requireActivity().runOnUiThread {
            var rvPlayLists = View.GONE

            var rlNoDataMessageContainer = View.VISIBLE
            var tvNoDataStatus = View.GONE
            binding.noDataMb.pbLoadContent.hide()

            when (status) {
                UiFragLoadDataStatus.LOADING -> {
                    binding.noDataMb.pbLoadContent.show()
                }
                UiFragLoadDataStatus.NO_MAIN_CONTENT -> {
                    binding.noDataMb.tvNoData.text = getString(R.string.no_playlists_yet)
                    tvNoDataStatus = View.VISIBLE
                }
                else -> {
                    rlNoDataMessageContainer = View.GONE
                    rvPlayLists = View.VISIBLE
                }
            }
            binding.rvPlayLists.visibility = rvPlayLists
            binding.noDataMb.rlNoDataMessageContainer
                .visibility = rlNoDataMessageContainer
            binding.noDataMb.tvNoData.visibility = tvNoDataStatus
        }
    }

    /**
     * Responsável principalmente pela configuração
     * da lista de PlayLists em tela.
     *
     * Como é possível que a invocação deste método
     * ocorra fora da Thread Principal, então é
     * importante sempre ter o código de atualização
     * de lista dentro de runOnUiThread().
     *
     * Outro ponto importante é garantir que não
     * haverá NullPointerException caso os dados
     * cheguem em método quando a UI não mais está no
     * foreground (primeiro plano). Assim o operador
     * not null (?.) é utilizado com frequência.
     *
     * @param pLists lista não mutável de PlayLists.
     *
     */
    private fun setUiModel( pLists: List<PlayList>? ){
        if( !pLists.isNullOrEmpty() ){
            requireActivity().runOnUiThread {
                uiDataStatus(
                    status = UiFragLoadDataStatus.LOADED
                )
                if (!pLists.equals( playLists )) {
                    playLists.clear()
                    playLists.addAll( pLists )
                }
                binding.rvPlayLists.adapter?.notifyDataSetChanged()
            }
        }
        else{
            uiDataStatus(
                status = UiFragLoadDataStatus.NO_MAIN_CONTENT
            )
        }
    }

    /**
     * Configura o estado atual do componente visual
     * SwipeRefresh.
     *
     * @param status estado atual do swipe.
     */
    private fun swipeRefreshStatus( status : Boolean ){
        activity?.runOnUiThread {
            binding.srlUpdateContent.isRefreshing = status
        }
    }

    /**
     * Solicita dados de último vídeo da fonte remota,
     * YouTube Data API.
     */
    private fun retrieveData(){
        UtilNetwork
            .getInstance( context = requireActivity() )
            .retrievePlayLists (
                callbackSuccess = {
                    swipeRefreshStatus( status = false )
                    setUiModel( it )
                },
                callbackFailure = {
                    swipeRefreshStatus( status = false )
                }
            )
    }

    /**
     * Configura o listener de swipe do componente
     * visual SwipeRefresh.
     */
    private fun setListener(){
        binding.srlUpdateContent.setOnRefreshListener {
            swipeRefreshStatus( status = true )
            retrieveData()
        }
    }

}