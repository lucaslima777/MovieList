package eokoe.teste.developer.lln.movielist

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_info_film.*;


class InfoFilm : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_film)


        val title: String?
        val overview: String?
        val urlPoster: String?
        if (savedInstanceState == null) {
            val extras = intent.extras
            if (extras == null) {
                title = null
                overview = null
                urlPoster = null
            } else {
                title = extras.getString("TITLE")
                overview = extras.getString("OVERVIEW")
                urlPoster = extras.getString("POSTER")
            }
        } else {
            title = savedInstanceState.getSerializable("TITLE") as String
            overview = savedInstanceState.getSerializable("OVERVIEW") as String
            urlPoster = savedInstanceState.getSerializable("POSTER") as String


        }

        info_title.setText(title);
        overview_textview.setText(overview)

        Picasso.get()
                .load(urlPoster)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(poster)

        fab.setOnClickListener {
            onBackPressed()
        }

    }


}
