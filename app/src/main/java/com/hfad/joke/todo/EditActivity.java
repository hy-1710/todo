package com.hfad.joke.todo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_todo);
        Intent intent=getIntent();
        final String id= intent.getStringExtra("id");
        final EditText title=(EditText)findViewById(R.id.title);
        final EditText description=(EditText)findViewById(R.id.description);
//        FloatingActionButton fl_btn = (FloatingActionButton) findViewById(R.id.floating_btn);
//        fl_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    SQLiteOpenHelper sql_hlp = new Database(EditActivity.this);
//                    SQLiteDatabase db = sql_hlp.getWritableDatabase();
//                    String  edit_title=title.getText().toString();
//                    String edit_desc= description.getText().toString();
//
//
//                }
//                catch (SQLiteException e)
//                {
//                    e.printStackTrace();
//                }
//        });
        FloatingActionButton save=(FloatingActionButton)findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {

                    SQLiteOpenHelper sql_hlp= new Database(EditActivity.this);
                    SQLiteDatabase db= sql_hlp.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("TITLE",title.getText().toString());
                    values.put("DESCRIPTION",description.getText().toString());
                    db.update("TASK",values,"_ID=?", new  String[] {id});
                }
                catch (SQLiteException e)
                {
                    //Log.i(TAG, "onClick: error occured");
                    e.printStackTrace();
                }

                Toast.makeText(EditActivity.this,"update Successfully",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        try {
            SQLiteOpenHelper sql_hlp = new Database(this);
            SQLiteDatabase db = sql_hlp.getReadableDatabase();

            Cursor cursor= db.query("TASK",
                    new String[] {"_ID","TITLE","DESCRIPTION"},
                    "_ID=?",
                    new String[]{id},
                    null, null, null

            );
            while (cursor.moveToNext()){
                String titleText= cursor.getString(1);
                String descriptionText= cursor.getString(2);

                title.setText(titleText);
                description.setText(descriptionText);


            }
            cursor.close();
            db.close();
        }
        catch (SQLiteException e)
        {
            e.printStackTrace();
        }


    }
}
