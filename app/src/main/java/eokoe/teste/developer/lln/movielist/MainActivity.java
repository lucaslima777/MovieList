package eokoe.teste.developer.lln.movielist;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import eokoe.teste.developer.lln.movielist.util.HttpHandler;
import eokoe.teste.developer.lln.movielist.util.UtilsNoInternet;

public class MainActivity extends AppCompatActivity {

    private Context context = this;
    private List<FilmEntity> dataList = new ArrayList<>();
    private RecyclerView.Adapter adapter;

    private String TAG = MainActivity.class.getSimpleName();
    private String apiURL;
    private String apiKey;
    private String apiLanguage;
    private String apiPage;
    private String urlImage;
    private String apiUrlSearch;
    private String apiQuery;
    private String urlSearch;
    private String url;
    private ProgressDialog pDialog;

    private int numMax = 20;

    ArrayList<String> titleArray = new ArrayList<String>(10);
    private String film_title[] = new String[numMax];

    ArrayList<Float> voteArray = new ArrayList<Float>(10);
    private Float film_vote[] = new Float[numMax];

    ArrayList<Double> popularityArray = new ArrayList<Double>(10);
    private Double film_popularity[] = new Double[numMax];

    ArrayList<String> dateArray = new ArrayList<String>(10);
    private String film_date[] = new String[numMax];

    ArrayList<String> overviewArray = new ArrayList<String>(10);
    private String film_overview[] = new String[numMax];

    ArrayList<String> posterArray = new ArrayList<String>(10);
    private String film_poster[] = new String[numMax];


    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private int numPage = 1;

    private NetworkChangeReceiver receiver;
    private ProgressBar progressBar;
    private ImageView wifi_img;
    private AlertDialog alertDialog;
    private boolean isConnected;

    private int total_results;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);

        apiURL = context.getResources().getString(R.string.apiServe);
        apiKey = context.getResources().getString(R.string.apiKey);
        apiLanguage = context.getResources().getString(R.string.apiLanguage);
        apiPage = context.getResources().getString(R.string.apiPage);
        apiUrlSearch = context.getResources().getString(R.string.apiSearch);
        apiQuery = context.getResources().getString(R.string.apiQuery);
        url = apiURL + apiKey + apiLanguage + apiPage;
        urlImage = context.getResources().getString(R.string.urlImage);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchDialog();
            }
        });

        AlertDialogCreate();

        recyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = getAdapter();
        recyclerView.setAdapter(adapter);

        if (isConnected == true) {
            new getFilm().execute();
            adapter.notifyDataSetChanged();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back:
                pageNumber(1);
                break;
            case R.id.next:
                pageNumber(2);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void pageNumber(int type) {
        if (type == 1 && numPage != 1) {
            numPage--;
            setUrl(apiURL + apiKey + apiLanguage + "&page=" + numPage);
            dataList.clear();
            new getFilm().execute();
        } else if (type == 2) {
            numPage++;
            setUrl(apiURL + apiKey + apiLanguage + "&page=" + numPage);
            dataList.clear();
            new getFilm().execute();
        }
    }


    private void prepareDataList() {
        for (int i = 0; i < getTotal_results(); i++) {
            dataList.add(new FilmEntity(i + 1, film_title[i], film_vote[i], film_popularity[i], film_date[i]));
        }
    }

    private RecyclerView.Adapter getAdapter() {
        final LayoutInflater inflater = LayoutInflater.from(this);
        RecyclerView.Adapter adapter = new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = inflater.inflate(R.layout.card_recycler, parent, false);
                return new MyViewHolder(view);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                MyViewHolder myHolder = (MyViewHolder) holder;
                myHolder.bindData(dataList.get(position));
            }

            @Override
            public int getItemCount() {
                return dataList.size();
            }
        };
        return adapter;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle;
        TextView textViewVote;
        RatingBar ratingBar;
        TextView textViewPopularity;
        TextView textViewDate;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewTitle = (TextView) itemView.findViewById(R.id.title);
            this.textViewVote = itemView.findViewById(R.id.vote_average);
            this.ratingBar = itemView.findViewById(R.id.rating);
            this.textViewPopularity = itemView.findViewById(R.id.popularity);
            this.textViewDate = itemView.findViewById(R.id.date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    newActivity(film_title[pos], film_overview[pos], film_poster[pos]);
                }
            });
        }

        public void bindData(FilmEntity filmEntity) {
            textViewTitle.setText(filmEntity.getTitle());
            textViewVote.setText("Média de Votos: " + filmEntity.getVote());
            ratingBar.setRating(Float.valueOf(String.valueOf(filmEntity.getVote())) / 2);
            textViewPopularity.setText("Popularidade: " + filmEntity.getPopularity());
            textViewDate.setText("" + filmEntity.getDate());
        }
    }


    public void newActivity(String title, String overview, String poster) {
        Intent intent = new Intent(MainActivity.this, InfoFilm.class);
        intent.putExtra("TITLE", title);
        intent.putExtra("OVERVIEW", overview);
        intent.putExtra("POSTER", urlImage + poster);
        startActivity(intent);
    }


    public class getFilm extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Aguarde...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            String jsonStr;
            if (getUrlSearch() == null) {
                jsonStr = sh.makeServiceCall(getUrl());
            } else {
                jsonStr = sh.makeServiceCall(apiUrlSearch + apiKey + apiLanguage + apiQuery + getUrlSearch().replace(" ", "%20") + apiPage);
            }

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray jObjquery = jsonObj.getJSONArray("results");
                    int jObjquerySearch;

                    if (getUrlSearch() == null){

                        setTotal_results(jObjquery.length());
                        Log.e(TAG, "TAMANHO " + jObjquery.length());

                    } else {
                        jObjquerySearch = jsonObj.getInt("total_results");

                        if (jObjquerySearch > numMax){
                            setTotal_results(numMax);
                        }else {
                            setTotal_results(jObjquerySearch);
                        }
                        Log.e(TAG, "TAMANHO " + jObjquerySearch);
                    }


                    int l;
                    for (l = 0; l < getTotal_results(); l++) {

                        JSONObject position = jObjquery.getJSONObject(l);
                        String title = position.getString("title");
                        float voteAverage = BigDecimal.valueOf(position.getDouble("vote_average")).floatValue();
                        Double popularity = position.getDouble("popularity");
                        String date = position.getString("release_date");
                        String overview = position.getString("overview");
                        String poster = position.getString("poster_path");

                        titleArray.add(l, title);
                        film_title[l] = titleArray.get(l);

                        voteArray.add(l, voteAverage);
                        film_vote[l] = voteArray.get(l);

                        popularityArray.add(l, popularity);
                        film_popularity[l] = popularityArray.get(l);

                        dateArray.add(l, date);
                        film_date[l] = dateArray.get(l);

                        overviewArray.add(l, overview);
                        film_overview[l] = overviewArray.get(l);

                        posterArray.add(l, poster);
                        film_poster[l] = posterArray.get(l);

                    }


                } catch (final JSONException e) {
                    Log.e(TAG, "Json error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Server Error.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Server Error.",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            prepareDataList();
            adapter.notifyDataSetChanged();
            pDialog.dismiss();
        }
    }

    public void searchDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Search").setCancelable(false);
        LayoutInflater inflater = getLayoutInflater();
        final View dialoglayout = inflater.inflate(R.layout.activity_dialog, null);
        final EditText editText;
        editText = dialoglayout.findViewById(R.id.editText);


        builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String film = editText.getText().toString();
                setUrlSearch(film);
                dataList.clear();
                new getFilm().execute();
            }
        });


        builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setView(dialoglayout);
        builder.show();
    }

    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            isNetworkAvailable(context);
        }


        private boolean isNetworkAvailable(Context context) {
            ConnectivityManager connectivity = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            if (!isConnected) {
                                alertDialog.dismiss();
                                Log.i("ANDROID", "Network " + " connected");
                                isConnected = true;
                                progressBar.setVisibility(View.INVISIBLE);
                                wifi_img.setVisibility(View.VISIBLE);
                                dataList.clear();
                                new getFilm().execute();
                                adapter.notifyDataSetChanged();
                            }
                            return true;
                        }
                    }
                }
            }
            alertDialog.show();
            isConnected = false;
            Log.i("ANDROID", "Network " + " not connected");
            return false;
        }
    }

    public void AlertDialogCreate() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_internet, null);
        dialogBuilder.setView(dialogView);

        final Button wifi_btn = dialogView.findViewById(R.id.wifi_on_app);
        Button mobile_btn = dialogView.findViewById(R.id.mobile_on);
        progressBar = dialogView.findViewById(R.id.progress_bar);
        wifi_img = dialogView.findViewById(R.id.wifi);

        wifi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilsNoInternet.turnOnWifi(MainActivity.this);
                wifi_img.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        mobile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilsNoInternet.turnOn3g(MainActivity.this);
            }
        });

        wifi_btn.setTextColor(Color.WHITE);
        mobile_btn.setTextColor(Color.WHITE);

        Drawable wifi = ContextCompat.getDrawable(this, R.drawable.ic_wifi_white);
        Drawable mobile = ContextCompat.getDrawable(this, R.drawable.ic_4g_white);

        wifi.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        mobile.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wifi_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(wifi, null, null, null);
            mobile_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(mobile, null, null, null);
        } else {
            wifi_btn.setCompoundDrawablesWithIntrinsicBounds(wifi, null, null, null);
            mobile_btn.setCompoundDrawablesWithIntrinsicBounds(mobile, null, null, null);
        }

        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlSearch() {
        return urlSearch;
    }

    public void setUrlSearch(String urlSearch) {
        this.urlSearch = urlSearch;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

}
