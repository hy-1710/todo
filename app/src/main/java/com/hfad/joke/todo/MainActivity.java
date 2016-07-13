package com.hfad.joke.todo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private ArrayList<Task> taskList;

    private RecyclerView recyclerview;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();
        taskList = new ArrayList<>();
        adapter = new TaskAdapter(this, taskList);



        recyclerview = (RecyclerView) findViewById(R.id.recycler_view);
        FloatingActionButton fl_btn = (FloatingActionButton) findViewById(R.id.floating_btn);
        fl_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewTodoActivity.class);
                startActivity(intent);
            }
        });

        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(adapter);



    }

    @Override
    protected void onResume() {
        super.onResume();

        preparedTask();
    }


    private void preparedTask() {
        taskList.clear();

        try {
            SQLiteOpenHelper sql_hlp= new Database(this);
            SQLiteDatabase db= sql_hlp.getReadableDatabase();

            Cursor cursor= db.query("TASK",
                    new String[] {"_ID","TITLE","DESCRIPTION"},
                    null,
                    null,
                    null, null, null

            );
            while (cursor.moveToNext()){
                String id= cursor.getString(0);
                String titleText= cursor.getString(1);
                String descriptionText= cursor.getString(2);
                Task t= new Task(id,titleText,descriptionText);
                taskList.add(t);
            }
            cursor.close();
            db.close();
        }
        catch (SQLiteException e)
        {
            e.printStackTrace();
        }


        adapter.notifyDataSetChanged();


    }

    public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {

        public static final  String TAG="TaskAdapter";


        private Context mContext;
        ArrayList<Task> taskList;

        public TaskAdapter(Context mContext, ArrayList<Task> taskList) {
            this.mContext = mContext;
            this.taskList = taskList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_view, parent, false);
            // Log.i(TAG, "onCreateViewHolder: view is: " + view);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            Task task = taskList.get(position);
            holder.title.setText(task.getTitle());
            holder.description.setText(task.getDescription());
            holder.overflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupMenu(holder.overflow,position);
                }
            });

        }

        private void showPopupMenu(View view, int position) {
            PopupMenu popup= new PopupMenu(mContext,view);
            MenuInflater inflater= popup.getMenuInflater();
            inflater.inflate(R.menu.menu_task,popup.getMenu());
            popup.setOnMenuItemClickListener(new MyMenuClickListner(position));
            popup.show();


        }
        class MyMenuClickListner implements PopupMenu.OnMenuItemClickListener {

            public int position;

            public MyMenuClickListner(int position) {
                this.position = position;
            }

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId())
                {

                    case R.id.edit:
                        Task task1 = taskList.get(position);
                       Intent intent= new Intent(MainActivity.this,EditActivity.class);
                        intent.putExtra("id",task1.getId());
                        startActivity(intent);
                        //Toast.makeText(mContext,"edit successfully",Toast.LENGTH_SHORT).show();
                        return  true;

                    case R.id.delete:
                        try {
                            SQLiteOpenHelper sql_hlp = new Database(mContext);
                            SQLiteDatabase db = sql_hlp.getReadableDatabase();
                            Task task = taskList.get(position);
                            db.delete("TASK", "_ID=?",new String[]{String.valueOf(task.getId())});
                        }
                        catch (SQLiteException e){
                            e.printStackTrace();
                        }

                        Toast.makeText(mContext,"delete successfully",Toast.LENGTH_SHORT).show();
                        preparedTask();

                        return  true;

                }
                return false;
            }
        }

        @Override
        public int getItemCount() {
            return taskList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView title, description;
            public ImageView overflow;

            public MyViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.tv_title);
                description = (TextView) view.findViewById(R.id.tv_note);
                overflow=(ImageView)view.findViewById(R.id.overflow);

            }
        }
    }

}

