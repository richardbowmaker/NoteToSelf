package com.example.richard.notetoself;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private NoteAdapter mNoteAdapter;
    private boolean mSound;
    private int mAnimOption;
    private SharedPreferences mPrefs;

    public class NoteAdapter extends BaseAdapter
    {
        List<Note> noteList = new ArrayList<Note>();

        public NoteAdapter()
        {
            super();
        }

        @Override
        public int getCount()
        {
            return noteList.size();
        }

        @Override
        public Object getItem(int position)
        {
            return noteList.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null)
            {
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listitem, parent, false);
            }

            TextView txtTitle = (TextView)convertView.findViewById(R.id.txtTitle);
            TextView txtDescription = (TextView)convertView.findViewById(R.id.txtDescription);
            ImageView ivImportant = (ImageView)convertView.findViewById(R.id.imageViewImportant);
            ImageView ivTodo = (ImageView)convertView.findViewById(R.id.imageViewTodo);
            ImageView ivIdea = (ImageView)convertView.findViewById(R.id.imageViewIdea);

            Note tempNote = noteList.get(position);

            if (!tempNote.isImportant()) ivImportant.setVisibility(View.GONE);
            if (!tempNote.isTodo()) ivTodo.setVisibility(View.GONE);
            if (!tempNote.isIdea()) ivIdea.setVisibility(View.GONE);


            txtTitle.setText(tempNote.getTitle());
            txtDescription.setText(tempNote.getDescription());

            return convertView;
        }

        public void addNote(Note n)
        {
            noteList.add(n);
            notifyDataSetChanged();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setContentView(R.layout.relative_tryout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mNoteAdapter = new NoteAdapter();
        ListView listNote = (ListView)findViewById(R.id.listView);
        listNote.setAdapter(mNoteAdapter);

        listNote.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Note tempNote = (Note)mNoteAdapter.getItem(position);
                DialogShowNote dialog = new DialogShowNote();
                dialog.sendNoteSelected(tempNote);
                dialog.show(getFragmentManager(), "");
            }
        });



//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_add)
        {
            DialogNewNote dialog = new DialogNewNote();
            dialog.show(getFragmentManager(), "");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        mPrefs = getSharedPreferences("Note to self", MODE_PRIVATE);
        mSound = mPrefs.getBoolean("sound", true);
        mAnimOption = mPrefs.getInt("anim option", SettingsActivity.FAST);
    }

    public void createNewNote(Note n)
    {
        mNoteAdapter.addNote(n);
    }
}
