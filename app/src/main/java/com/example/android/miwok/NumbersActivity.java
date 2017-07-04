package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class NumbersActivity extends AppCompatActivity {
    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;
    AudioManager.OnAudioFocusChangeListener afChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || (focusChange ==
                            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK)) {
                        mMediaPlayer.pause();
                        mMediaPlayer.seekTo(0);

                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        mAudioManager.abandonAudioFocus(afChangeListener);

                        mMediaPlayer.start();

                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {

                        releaseMediaPlayer();
                    }
                }
            };
    MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText(NumbersActivity.this, "Song has stopped playing", Toast.LENGTH_SHORT).show();
            releaseMediaPlayer();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        setContentView(R.layout.words_list);
        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("one", "lutti", R.drawable.number_one, R.raw.number_one));
        words.add(new Word("two", "otiiko", R.drawable.number_two, R.raw.number_two));
        words.add(new Word("three", "tolookosu", R.drawable.number_three, R.raw.number_three));
        words.add(new Word("four", "oyyisa", R.drawable.number_four, R.raw.number_four));
        words.add(new Word("five", "massokka", R.drawable.number_five, R.raw.number_five));
        words.add(new Word("six", "temmokka", R.drawable.number_six, R.raw.number_six));
        words.add(new Word("seven", "kenekaku", R.drawable.number_seven, R.raw.number_seven));
        words.add(new Word("eight", "kawinta", R.drawable.number_eight, R.raw.number_eight));
        words.add(new Word("nine", "wo’e", R.drawable.number_nine, R.raw.number_nine));
        words.add(new Word("ten", "na’aacha", R.drawable.number_ten, R.raw.number_ten));
        WordAdapter wordAdapter = new WordAdapter(this, words, R.color.category_numbers);

        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(wordAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                releaseMediaPlayer();
                int result = mAudioManager.requestAudioFocus(afChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    Toast.makeText(NumbersActivity.this, "Audio Played", Toast.LENGTH_SHORT).show();
                    mMediaPlayer = MediaPlayer.create(NumbersActivity.this, words.get(position).getAudioResourceId());
                    mMediaPlayer.start();
                    mMediaPlayer.setOnCompletionListener(completionListener);
                }


            }
        });


    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;
            mAudioManager.abandonAudioFocus(afChangeListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(NumbersActivity.this, "Stopped", Toast.LENGTH_SHORT).show();
        releaseMediaPlayer();
    }
}
