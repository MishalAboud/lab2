package com.example.lab2;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseUtils;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final List<TodoItem> todoList = new ArrayList<>();
    private ListView todoListView;
    private EditText todoEditText;
    private SwitchCompat urgentSwitch;
    private Button addTodoButton;
    private TodoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Convert fields to local variables
        todoListView = findViewById(R.id.todoListView);
        todoEditText = findViewById(R.id.todoEditText);
        urgentSwitch = findViewById(R.id.urgentSwitch);
        addTodoButton = findViewById(R.id.addTodoButton);

        // Load todos from the database
        loadTodosFromDatabase();

        adapter = new TodoAdapter(todoList, this);
        todoListView.setAdapter(adapter);

        addTodoButton.setOnClickListener(v -> {
            String todoText = todoEditText.getText().toString();
            boolean isUrgent = urgentSwitch.isChecked();

            // Add the todo to the database
            addTodoToDatabase(todoText, isUrgent);

            // Add the todo to the list
            todoList.add(new TodoItem(todoText, isUrgent));
            adapter.notifyDataSetChanged();
            todoEditText.getText().clear();
        });

        todoListView.setOnItemLongClickListener((parent, view, position, id) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Do you want to delete this?");
            builder.setMessage("The selected row is: " + position);
            builder.setPositiveButton("Yes", (dialog, which) -> {
                // Delete the todo from the database
                deleteTodoFromDatabase(position);

                todoList.remove(position);
                adapter.notifyDataSetChanged();
            });
            builder.setNegativeButton("No", null);
            builder.show();
            return true;
        });

        // Print database information to the Log window for debugging
        printCursor(getAllTodosFromDatabase());
    }

    private void loadTodosFromDatabase() {
        TodoDatabaseHelper dbHelper = new TodoDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {TodoDatabaseHelper.COLUMN_TEXT, TodoDatabaseHelper.COLUMN_URGENT};
        Cursor cursor = db.query(TodoDatabaseHelper.TABLE_TODO, projection, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String todoText = cursor.getString(0);
                boolean isUrgent = cursor.getInt(1) == 1;
                todoList.add(new TodoItem(todoText, isUrgent));
                cursor.moveToNext();
            }
            cursor.close();
        }

        db.close();
    }

    private void addTodoToDatabase(String text, boolean isUrgent) {
        TodoDatabaseHelper dbHelper = new TodoDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TodoDatabaseHelper.COLUMN_TEXT, text);
        values.put(TodoDatabaseHelper.COLUMN_URGENT, isUrgent ? 1 : 0);

        db.insert(TodoDatabaseHelper.TABLE_TODO, null, values);
        db.close();
    }

    private void deleteTodoFromDatabase(int position) {
        TodoDatabaseHelper dbHelper = new TodoDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Assuming _id is the primary key starting from 1
        db.delete(TodoDatabaseHelper.TABLE_TODO, TodoDatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(position + 1)});
        db.close();
    }

    private Cursor getAllTodosFromDatabase() {
        TodoDatabaseHelper dbHelper = new TodoDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {TodoDatabaseHelper.COLUMN_ID, TodoDatabaseHelper.COLUMN_TEXT, TodoDatabaseHelper.COLUMN_URGENT};
        return db.query(TodoDatabaseHelper.TABLE_TODO, projection, null, null, null, null, null);
    }

    private void printCursor(Cursor cursor) {
        Log.d("Database Info", "Number of Results in Cursor: " + cursor.getCount());
        Log.d("Database Info", "Cursor Data:\n" + DatabaseUtils.dumpCursorToString(cursor));
        cursor.close();
    }


    private static class TodoAdapter extends BaseAdapter {
        private final List<TodoItem> list;
        private final Context context;

        public TodoAdapter(List<TodoItem> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.list_item_todo, parent, false);
            }
            TextView todoText = view.findViewById(R.id.todoTextView);
            TodoItem todoItem = list.get(position);
            todoText.setText(todoItem.getText());
            if (todoItem.isUrgent()) {
                view.setBackgroundColor(Color.RED);
                todoText.setTextColor(Color.WHITE);
            }
            return view;
        }
    }
}
