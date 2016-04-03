package com.example.richard.notetoself;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private NoteAdapter mNoteAdapter;
    private boolean mSound;
    private int mAnimOption;
    private SharedPreferences mPrefs;
    private Animation mAnimFlash;
    private Animation mFadeIn;

    private int mIdBeep = -1;
    SoundPool mSp;

    public class NoteAdapter extends BaseAdapter
    {
        List<Note> noteList = new ArrayList<Note>();
        private JSONSerializer mSerializer;

        public NoteAdapter()
        {
            super();
            mSerializer = new JSONSerializer("NoteToSelf.json", MainActivity.this.getApplicationContext());

            try
            {
                noteList = mSerializer.load();
            }
            catch (Exception e)
            {
                noteList = new ArrayList<Note>();
            }
        }

        public void saveNotes()
        {
            try
            {
                mSerializer.save(noteList);
            }
            catch (Exception e)
            {
            }
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

            if (tempNote.isImportant() && mAnimOption != SettingsActivity.NONE)
            {
                convertView.setAnimation(mAnimFlash);
            }
            else
            {
                convertView.setAnimation(mFadeIn);
            }

            if (!tempNote.isImportant())
            {
                ivImportant.setVisibility(View.GONE);
            }
            else
            {
                ivImportant.setVisibility(View.VISIBLE);
            }
            if (!tempNote.isTodo())
            {
                ivTodo.setVisibility(View.GONE);
            }
            else
            {
                ivTodo.setVisibility(View.VISIBLE);
            }
            if (!tempNote.isIdea())
            {
                ivIdea.setVisibility(View.GONE);
            }
            else
            {
                ivIdea.setVisibility(View.VISIBLE);
            }


            txtTitle.setText(tempNote.getTitle());
            txtDescription.setText(tempNote.getDescription());

            return convertView;
        }

        public void addNote(Note n)
        {
            noteList.add(n);
            notifyDataSetChanged();
        }

        public void deleteNote(int n)
        {
            noteList.remove(n);
            notifyDataSetChanged();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //For sound
        // Instantiate our sound pool dependent upon which version of Android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            mSp = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build();
        }

        try
        {
            // Create objects of the 2 required classes
            AssetManager assetManager = this.getAssets();
            AssetFileDescriptor descriptor;

            // Load our fx in memory ready for use
            descriptor = assetManager.openFd("beep.ogg");
            mIdBeep = mSp.load(descriptor, 0);
        }
        catch(IOException e)
        {
        }

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
                if (mSound)
                {
                    mSp.play(mIdBeep, 1, 1, 0, 0, 1);
                }
                Note tempNote = (Note) mNoteAdapter.getItem(position);
                DialogShowNote dialog = new DialogShowNote();
                dialog.sendNoteSelected(tempNote);
                dialog.show(getFragmentManager(), "");
            }
        });

        listNote.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                mNoteAdapter.deleteNote(position);
                return true;
            }
        });

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

        mAnimFlash = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.flash);
        mFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);

        if (mAnimOption == SettingsActivity.FAST)
        {
            mAnimFlash.setDuration(100);
        }
        else if (mAnimOption == SettingsActivity.SLOW)
        {
            mAnimFlash.setDuration(1000);
        }

        mNoteAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mNoteAdapter.saveNotes();
    }

    public void createNewNote(Note n)
    {
        mNoteAdapter.addNote(n);
    }
}
