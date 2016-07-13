package com.hfad.joke.todo;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewTodoActivity extends AppCompatActivity {


    public static  final String TAG="NewToDoActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_todo);

        TextInputLayout inputLayoutTitle = (TextInputLayout) findViewById(R.id.input_layout_title);
        TextInputLayout inputLayoutDesc = (TextInputLayout) findViewById(R.id.input_layout_desc);
        final EditText title=(EditText)findViewById(R.id.title);
        final EditText description=(EditText)findViewById(R.id.description);
        FloatingActionButton save=(FloatingActionButton)findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {
                    SQLiteOpenHelper sql_hlp= new Database(NewTodoActivity.this);
                    SQLiteDatabase db= sql_hlp.getWritableDatabase();
                    Database.insertTask(db,title.getText().toString(),description.getText().toString());
                    ;

                }
                catch (SQLiteException e)
                {
                    Log.i(TAG, "onClick: error occured");
                    e.printStackTrace();
                }

                Toast.makeText(NewTodoActivity.this,"Saved Successfully",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
