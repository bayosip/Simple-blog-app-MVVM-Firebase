package com.osicorp.carbon_test_app.view.fragments.list

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.Uri
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.koushikdutta.ion.Ion
import com.osicorp.carbon_test_app.R
import com.osicorp.carbon_test_app.databinding.ListItemBinding
import com.osicorp.carbon_test_app.interfaces.ListListener
import com.osicorp.carbon_test_app.model.Constants
import com.osicorp.carbon_test_app.model.GeneralUtils
import com.osicorp.carbon_test_app.model.Post
import com.squareup.picasso.Picasso
import java.util.*
import java.util.concurrent.ExecutionException

class PostAdapter(val listener: ListListener): RecyclerView.Adapter<PostAdapter.PostVH>() {


    private lateinit var context: Context
    private var blogPosts= LinkedList<Post>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostVH {
        context = parent.context
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        val binding = ListItemBinding.bind(view)
        return PostVH(binding)
    }

    fun setList(list: List<Post>){
        blogPosts.clear()
        for (p in list){
            val i = p
            blogPosts.add(i)
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: PostVH, position: Int) {
        with(holder){
            listener = this@PostAdapter.listener
            post = this@PostAdapter.blogPosts.get(position)
            binding.buttonPlayVideo.setOnClickListener(holder)
            binding.cardItem.setOnClickListener(holder)
            binding.btnEnlarge.setOnClickListener(holder)
            binding.videoViewPost.setMediaController(listener?.getMediaController())
            listener?.getMediaController()?.setAnchorView(binding.videoViewPost)
            binding.expandTextView.setText(post?.Title)
            val time =post?.TimeStamp?.time
            binding.textElaspedTime.text =
                DateFormat.format("MMM dd yyyy, HH:mm:ss", Date(time!!)).toString()
            when(post?.MediaType){
                Constants.IMAGE -> {
                    binding.mediaView.visibility = View.VISIBLE
                    binding.imageViewPost.visibility = View.VISIBLE
                    loadImg(binding.imageViewPost, post!!.Url)
                }
                Constants.VIDEO -> {
                    binding.mediaView.visibility = View.VISIBLE
                    binding.videoViewPost.visibility = View.VISIBLE
                    holder.playSelectedVideoFrom(post?.Url, 0)
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return blogPosts.size
    }

    private fun loadImg(img: ImageView, url: String?){
        Picasso.get().load(url).into(img)
    }

    private fun loadAndResizeImage(img: ImageView, url: String?) {

            img.setImageBitmap(null)
            try {
                val result: Bitmap = Ion.with(context)
                    .load(url)
                    .setTimeout(15000)
                    .withBitmap()
                    .asBitmap()
                    .get()
                img.setImageBitmap(
                    GeneralUtils.resizeBitmap(
                        result,
                        img.getMeasuredWidth()
                    )
                )
            } catch (e: ExecutionException) {
                e.printStackTrace()
                img.setImageResource(R.drawable.ic_image_24)
            } catch (e: InterruptedException) {
                e.printStackTrace()
                img.setImageResource(R.drawable.ic_image_24)
            }
    }

   inner class PostVH(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener{
        var seekTo =0
       var listener:ListListener?=null
       var post:Post?=null
        override fun onClick(v: View?) {
            with(binding){
                when(v?.id){
                    R.id.card_item, R.id.btnEnlarge -> {
                        post?.postId?.let { listener?.showSelectedPostWithId(it) }
                    }
                    R.id.buttonPlayVideo -> {
                        if (!videoViewPost.isPlaying()) {
                            buttonPlayVideo.setImageResource(R.drawable.ic_pause_24)
                            buttonPlayVideo.setVisibility(View.GONE)
                            progressVideoLoading.show()

                            playSelectedVideoFrom(post?.Url!!, seekTo)
                        } else {
                            seekTo = videoViewPost.getCurrentPosition()
                            buttonPlayVideo.visibility = View.VISIBLE
                            buttonPlayVideo.setImageResource(R.drawable.ic_play_video_24)
                            videoViewPost.pause()
                        }
                    }
                    else -> { }
                }
            }
        }

        fun playSelectedVideoFrom(url: String?, seekTo: Int) {
           with(binding!!) {
               try {
                   val uri = Uri.parse(url)
                   videoViewPost.setVideoURI(uri)
                   videoViewPost.setOnPreparedListener { mediaPlayer: MediaPlayer ->
                       progressVideoLoading.show()
                       mediaPlayer.isLooping = false
                       videoViewPost.start()
                       if (mediaPlayer.isPlaying) {
                           mediaPlayer.seekTo(seekTo)
                           progressVideoLoading.hide()
                       }
                   }
                   videoViewPost.setOnCompletionListener { mediaPlayer: MediaPlayer ->
                       mediaPlayer.reset()
                       mediaPlayer.release()
                       buttonPlayVideo.setImageResource(R.drawable.ic_play_video_24)
                       buttonPlayVideo.setVisibility(View.VISIBLE)
                   }

               } catch (ex: Exception) {
                   ex.printStackTrace()
               }
               videoViewPost.requestFocus()
           }
       }
    }

}