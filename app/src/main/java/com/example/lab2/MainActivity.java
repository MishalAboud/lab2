package com.example.lab2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat; // Import SwitchCompat
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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

        adapter = new TodoAdapter(todoList, this);
        todoListView.setAdapter(adapter);

        addTodoButton.setOnClickListener(v -> {
            String todoText = todoEditText.getText().toString();
            boolean isUrgent = urgentSwitch.isChecked();
            todoList.add(new TodoItem(todoText, isUrgent));
            adapter.notifyDataSetChanged();
            todoEditText.getText().clear();
        });

        todoListView.setOnItemLongClickListener((parent, view, position, id) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Do you want to delete this?");
            builder.setMessage("The selected row is: " + position);
            builder.setPositiveButton("Yes", (dialog, which) -> {
                todoList.remove(position);
                adapter.notifyDataSetChanged();
            });
            builder.setNegativeButton("No", null);
            builder.show();
            return true;
        });
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
