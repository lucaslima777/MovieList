# MovieList
Most Popular Movie List


|Item|status
|---|---|
|Movie List|`yes`
|Details of the Movie|`yes`
|API|`yes`
|Unitary Tests|`no`
|Connection Error|`yes`
|Search Movie|`yes`
|Java|`yes`
|Kotlin|`yes`



# List / Detail

<img src="https://github.com/lucaslima777/MovieList/blob/master/gif/main.gif?raw=true" title="main" />

# Search

<img src="https://github.com/lucaslima777/MovieList/blob/master/gif/search.gif?raw=true" title="search" />



# ProGuard - Obfuscate


## configuration

```gradle
    release {
            debuggable false
            minifyEnabled true
            shrinkResources true
            useProguard true
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }

        debug {
            debuggable true
            minifyEnabled true
            shrinkResources true
            useProguard true
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
  ```
  
  
* **ProGuard disabled**

```java
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
```

  
* **ProGuard activated**

```java
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_main);
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        this.f4873N = new C0857b(this);
        registerReceiver(this.f4873N, intentFilter);
        this.f4889t = this.f4885p.getResources().getString(R.string.apiServe);
        this.f4890u = this.f4885p.getResources().getString(R.string.apiKey);
        this.f4891v = this.f4885p.getResources().getString(R.string.apiLanguage);
        this.f4892w = this.f4885p.getResources().getString(R.string.apiPage);
        this.f4894y = this.f4885p.getResources().getString(R.string.apiSearch);
        this.f4895z = this.f4885p.getResources().getString(R.string.apiQuery);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.f4889t);
        stringBuilder.append(this.f4890u);
        stringBuilder.append(this.f4891v);
        stringBuilder.append(this.f4892w);
        this.f4861B = stringBuilder.toString();
        this.f4893x = this.f4885p.getResources().getString(R.string.urlImage);
        this.f4870K = (FloatingActionButton) findViewById(R.id.fab);
        this.f4870K.setOnClickListener(new C08511(this));
        m6802l();
        this.f4871L = (RecyclerView) findViewById(R.id.main_recycler_view);
        this.f4871L.setLayoutManager(new LinearLayoutManager(this));
        this.f4887r = m6790q();
        this.f4871L.setAdapter(this.f4887r);
        if (this.f4877R) {
            new C0860c(this).execute(new Void[0]);
            this.f4887r.m1965c();
        }
    }
```
  
  
# Developed By

* Lucas Lima 
 * :email: e-mail: lucaslimatorre@gmail.com
 
 [![LinkedIn](https://img.shields.io/badge/LinkedIn-LucasLima-blue.svg)](https://www.linkedin.com/in/lucas-lima-torre/)
 

![GitHub issue age](https://img.shields.io/badge/build-android%20studio-brightgreen.svg)


# License

    Copyright 2018 Lucas Lima

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

    
