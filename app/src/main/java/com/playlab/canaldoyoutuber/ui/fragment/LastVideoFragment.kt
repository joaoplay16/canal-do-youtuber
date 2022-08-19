package com.playlab.canaldoyoutuber.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.playlab.canaldoyoutuber.R
import com.playlab.canaldoyoutuber.data.dynamic.UtilDatabase
import com.playlab.canaldoyoutuber.data.fixed.LastVideoData
import com.playlab.canaldoyoutuber.databinding.FragmentLastVideoBinding
import com.playlab.canaldoyoutuber.model.LastVideo
import com.squareup.picasso.Picasso

/**
 * Contém toda a UI de último vídeo disponível
 * no canal YouTube do app.
 *
 * @constructor cria um objeto completo do tipo
 * [LastVideoFragment].
 */

class LastVideoFragment : Fragment() {
    private lateinit var binding: FragmentLastVideoBinding

    companion object {
        /**
         * Constante com o identificador único do
         * fragmento [LastVideoFragment] para que
         * ele seja encontrado na pilha de fragmentos
         * e assim não seja necessária a construção
         * de mais de um objeto deste fragmento em
         * memória enquanto o aplicativo estiver em
         * execução.
         */
        const val KEY = "LastVideoFragment_key"
    }
    /**
     * [lastVideo] sempre inicia com algum dado válido
     * de "último vídeo" liberado, mesmo que nenhum
     * vídeo tenha sido ainda enviado ao app.
     */
    private var lastVideo: LastVideo = LastVideoData.getInitialVideo()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * Para garantir que o banco de dados local
         * será acessado apenas na primeira vez que
         * o fragmento é carregado.
         *
         * Sendo assim o usuário poderá mudar de tabs
         * (itens de menu principal) que não haverá novos
         * carregamentos somente devido à mudança de
         * tab. Isso se o objeto de fragmento for
         * retido em memória.
         */
        UtilDatabase
            .getInstance( context = requireActivity().applicationContext )
            .getLastVideo{
                setUiModel( lVideo = it )
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle? ): View? {

        binding = FragmentLastVideoBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setUiModel(lVideo = lastVideo)
    }

    /**
     * Responsável pela configuração dos dados de
     * "último vídeo disponível" em tela.
     *
     * Como é possível que a invocação deste método
     * ocorra fora da Thread Principal, então é
     * importante sempre ter o código de atualização
     * de vídeo dentro de runOnUiThread().
     *
     * Outro ponto importante é garantir que não
     * haverá NullPointerException caso os dados
     * cheguem em método quando a UI não mais está no
     * foreground (primeiro plano). Assim o operador
     * not null (?.) é utilizado com frequência.
     *
     * @param lVideo último vídeo liberado em canal.
     */
    private fun setUiModel( lVideo: LastVideo? ){
        if( lVideo != null ) {
            requireActivity().runOnUiThread{
                lastVideo = lVideo
                try {
                    Picasso
                        .get()
                        .load(lVideo.thumbUrl)
                        .into(binding.ivLastVideoThumb)
                    binding.ivLastVideoThumb.contentDescription = lVideo.title
                } catch (e: Exception) {}

                binding.tvLastVideoTitle.text = lVideo.title
                descriptionStatus(description = lVideo.description)
            }
        }
    }
    /**
     * Garante que o componente visual de apresentação
     * de descrição do vídeo somente estará em tela
     * caso exista descrição (alguns vídeos não têm).
     *
     * @param description descrição do vídeo.
     */
    private fun descriptionStatus( description: String ){
        if( description.isNotEmpty() ){
            binding.tvLastVideoDesc.text = description
            binding.tvLastVideoDesc.visibility = View.VISIBLE
        }
        else{
            binding.tvLastVideoDesc.visibility = View.GONE
        }
    }

    /**
     * Configura os listeners de alguns componentes
     * visuais em tela.
     */
    private fun setListeners() {
        binding.llLastVideoContainer.setOnClickListener {
            openVideoOnYouTube()
        }
    }
    /**
     * Invoca o aplicativo do YouTube para que o usuário
     * tenha acesso ao último vídeo liberado no canal.
     *
     * Caso o dado de URI presente no objeto [lastVideo] seja
     * inválido para a abertura do app nativo do YouTube
     * ou abertura da versão dele em app de navegador Web,
     * então uma mensagem de falha é apresentada.
     */
    private fun openVideoOnYouTube() {
        val intent = Intent(
            Intent.ACTION_VIEW,
            lastVideo.webUri()
        )
        /**
         * É utópico, mas pode ocorrer de não haver instalado
         * no aparelho do usuário o aplicativo do YouTube e
         * nem mesmo um navegador Web.
         *
         * Sendo assim, ao invés de gerar uma exceção, nós
         * avisamos ao usuário a necessidade de instalar o
         * aplicativo adequado.
         */
        if (intent.resolveActivity(requireActivity().packageManager) == null) {
            Toast
                .makeText(
                    activity,
                    String.format(
                        getString(R.string.last_video_toast_alert),
                        lastVideo.title
                    ),
                    Toast.LENGTH_LONG
                )
                .show()
            return
        }
        requireActivity().startActivity(intent)
    }
}