
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

public class PhrasesActivity extends AppCompatActivity {
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
            Toast.makeText(PhrasesActivity.this, "Song has stopped playing", Toast.LENGTH_SHORT).show();
            releaseMediaPlayer();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        setContentView(R.layout.words_list);
        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("Where are you going?", "minto wuksus", R.raw.phrase_where_are_you_going));
        words.add(new Word("What is your name?", "tinnә oyaase'nә", R.raw.phrase_what_is_your_name));
        words.add(new Word("My name is", "oyaaset", R.raw.phrase_my_name_is));
        words.add(new Word("How are you feeling?", "michәksәs?", R.raw.phrase_how_are_you_feeling));
        words.add(new Word("I’m feeling good.", "kuchi achit", R.raw.phrase_im_feeling_good));
        words.add(new Word("Are you coming?", "әәnәs'aa?", R.raw.phrase_are_you_coming));
        words.add(new Word("Yes, I’m coming.", "hәә’ әәnәm", R.raw.phrase_yes_im_coming));
        words.add(new Word("I’m coming.", "әәnәm", R.raw.phrase_im_coming));
        words.add(new Word("Let’s go.", "yoowutis", R.raw.phrase_lets_go));
        words.add(new Word("Come here.", "әnni'nem", R.raw.phrase_come_here));

        WordAdapter wordAdapter = new WordAdapter(this, words, R.color.category_phrases);

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
                    Toast.makeText(PhrasesActivity.this, "Audio Played", Toast.LENGTH_SHORT).show();

                    mMediaPlayer = MediaPlayer.create(PhrasesActivity.this, words.get(position).getAudioResourceId());
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
        Toast.makeText(PhrasesActivity.this, "Stopped", Toast.LENGTH_SHORT).show();
        releaseMediaPlayer();
    }
}
