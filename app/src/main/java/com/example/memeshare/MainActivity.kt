package com.example.memeshare

import MySingleton
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {
    var currentImage : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        loadMeme()

        val nextBtn = findViewById<Button>(R.id.nextBtn)
        val shareBtn = findViewById<Button>(R.id.shareBtn)

        shareBtn.setOnClickListener {
            shareMeme()
        }
        nextBtn.setOnClickListener {
            nextMeme()
        }

    }

    private fun loadMeme(){
        val meme = findViewById<ImageView>(R.id.memeView)
        val url = "https://meme-api.com/gimme"
        val pgBar = findViewById<ProgressBar>(R.id.pgBar)

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                currentImage = response.getString("url")
                Glide.with(this).load(currentImage).listener(object: RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        pgBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        pgBar.visibility = View.GONE
                        return false
                    }

                }).into(meme)
            },
            { _ ->
                Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
            }
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    private fun shareMeme(){
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Hey checkout this cool memes ")
        val chooser = Intent.createChooser(intent, "Share memes through...")
        startActivity(chooser)

    }
    private fun nextMeme(){
        loadMeme()
    }
}