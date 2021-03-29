package com.osicorp.carbon_test_app.view.fragments

import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.MediaController
import androidx.lifecycle.ViewModelProvider
import com.koushikdutta.ion.Ion
import com.osicorp.carbon_test_app.R
import com.osicorp.carbon_test_app.databinding.FragmentDetailsBinding
import com.osicorp.carbon_test_app.model.Constants
import com.osicorp.carbon_test_app.model.GeneralUtils
import com.osicorp.carbon_test_app.view_model.ListViewModel
import com.squareup.picasso.Picasso
import java.util.concurrent.ExecutionException

class DetailsFragment: BaseFragment(), View.OnClickListener {

    private var binding: FragmentDetailsBinding?=null
    private  lateinit var viewModel: ListViewModel
    private var mediaController: MediaController? = null
    private var fullBitmap: Bitmap? = null
    private var url: String? = null
    private var seekTo = 0
    private var mediaType: Long? = null
    private lateinit var title:String
    companion object{
        private val SEEK_INFO = "Seek"
        private val TITLE = "title"
        private val DURATION: Long = 1000
        private val BITMAP = "Bitmap"
        private val TYPE = "Type"
        private val ID = "postId"

        fun getInstance(url: String?, title: String, type: Long): DetailsFragment{
            val fragment=DetailsFragment()
            val extra = Bundle()
            extra.putString(Constants.URI_DATA, url)
            extra.putString(TITLE, title)
            extra.putLong(TYPE, type)
            fragment.arguments = extra
            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(R.layout.fragment_details, container, false)
        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mediaController = MediaController(activity)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentDetailsBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
    }


    override fun initialiseWidgets(view: View) {
        val post = requireArguments().getString(ID)?.let { viewModel.getPostWithId(it) }
        mediaType = post?.MediaType
        url = post?.Url
        with(binding!!){
            expandTextView.text = post?.Title
            if (mediaType!=Constants.TEXT){
                layoutMedia.visibility = View.VISIBLE
                when(mediaType){
                    Constants.VIDEO -> {
                        layoutFullVid.visibility = View.VISIBLE
                        videoFullscreen.setMediaController(mediaController)
                        mediaController!!.setAnchorView(videoFullscreen)
                        if (post != null) {
                            playSelectedVideoFrom(post.Url!!, 0)
                            buttonPlayVideo.visibility = View.GONE
                        }
                    }
                    Constants.IMAGE -> {
                        layoutFullImg.visibility = View.VISIBLE
                        loadImg(binding!!.imgViewFull, url)
                    }
                }
            }
            buttonPlayVideo.setOnClickListener(this@DetailsFragment)
        }
    }

    private fun loadImg(img: ImageView, url: String?){
        Picasso.get().load(url).into(img)
    }

    private fun loadAndResizeImage() {
        with(binding!!) {
            imgViewFull.setImageBitmap(null)
            try {
                val result: Bitmap = Ion.with(activity)
                    .load(url)
                    .withBitmap()
                    .asBitmap()
                    .get()
                imgViewFull.setImageBitmap(
                    GeneralUtils.resizeBitmap(
                        result,
                        imgViewFull.getMeasuredWidth()
                    )
                )
            } catch (e: ExecutionException) {
                e.printStackTrace()
                imgViewFull.setImageResource(R.drawable.ic_image_24)
            } catch (e: InterruptedException) {
                e.printStackTrace()
                imgViewFull.setImageResource(R.drawable.ic_image_24)
            }
        }
    }

    private fun playSelectedVideoFrom(url: String, seekTo: Int) {
        with(binding!!) {
            try {
                val uri = Uri.parse(url)
                videoFullscreen.setVideoURI(uri)
                videoFullscreen.setOnPreparedListener { mediaPlayer: MediaPlayer ->
                    progressVideoLoading.show()
                    mediaPlayer.isLooping = false
                    videoFullscreen.start()
                    if (mediaPlayer.isPlaying) {
                        mediaPlayer.seekTo(seekTo)
                        progressVideoLoading.hide()
                    }
                }
                videoFullscreen.setOnCompletionListener { mediaPlayer: MediaPlayer ->
                    mediaPlayer.reset()
                    mediaPlayer.release()
                    buttonPlayVideo.setImageResource(R.drawable.ic_play_video_24)
                    buttonPlayVideo.setVisibility(View.VISIBLE)
                }

            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            videoFullscreen.requestFocus()
        }
    }

    override fun onDestroy() {
        if (binding?.videoFullscreen?.isPlaying() == true)
            binding?.videoFullscreen?.stopPlayback()
        binding = null
        super.onDestroy()

    }

    override fun onClick(v: View?) {
        if(v?.id == R.id.buttonPlayVideo) {
            with(binding!!) {
                if (!videoFullscreen.isPlaying()) {
                    buttonPlayVideo.setImageResource(R.drawable.ic_pause_24)
                    buttonPlayVideo.setVisibility(View.GONE)
                    progressVideoLoading.show()

                    playSelectedVideoFrom(url!!, seekTo)
                } else {
                    seekTo = videoFullscreen.getCurrentPosition()
                    buttonPlayVideo.visibility = View.VISIBLE
                    buttonPlayVideo.setImageResource(R.drawable.ic_play_video_24)
                    videoFullscreen.pause()
                }
            }
        }
    }
}