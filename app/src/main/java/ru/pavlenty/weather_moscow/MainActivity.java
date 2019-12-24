package ru.pavlenty.weather_moscow;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private Button btn;
    private ImageView im;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        im = findViewById(R.id.im);
        btn = findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // вызов AsyncTask
                // создание отдельного потока
                MyDownload mytask = new MyDownload();
                mytask.execute();
            }
        });
    }

    private class MyDownload extends AsyncTask<Void, Void, Bitmap> {

        HttpURLConnection httpurl;

        @Override
        protected Bitmap doInBackground(Void... voids) {

            try {
                URL url = new URL("http://api.weatherstack.com/current?access_key=c88c008b493b902d7d6c70b4da8fe193&query=Moscow");
                httpurl = (HttpURLConnection) url.openConnection();
                httpurl.setRequestMethod("GET");
                httpurl.connect();

                InputStream inputStream = httpurl.getInputStream();

                Scanner scan = new Scanner(inputStream);
                StringBuffer buffer = new StringBuffer();
                while(scan.hasNextLine()) {
                    buffer.append(scan.nextLine());
                }


                String result = buffer.toString();

                Gson g = new Gson();
                Example ex = g.fromJson(result,Example.class);
                URL url_img = new URL( "https://avatars2.githubusercontent.com/u/24296446?s=400&v=4");
                URLConnection conn = url.openConnection();
                InputStream in = conn.getInputStream();
                Bitmap image = BitmapFactory.decodeStream(in);
                return image;

            } catch (java.io.IOException e) {
                Log.e("RRRRRRR",e.getMessage());
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Bitmap image) {
            super.onPostExecute(image);
            // метод, который выполняется после doInBackground
            im.setImageBitmap(image);
         //   Log.e("RRRRRRRRR",s);
        }
    }
}
