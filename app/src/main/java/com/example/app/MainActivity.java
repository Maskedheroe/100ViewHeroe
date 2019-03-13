package com.example.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.bookview.BookPageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final BookPageView bookView = (BookPageView) findViewById(R.id.view_book_page);
    }
}
