package ru.webgrozny.fifteenpuzzle;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import ru.webgrozny.fifteenpuzzle.model.Game;
import ru.webgrozny.fifteenpuzzle.model.Puzzle;

public class MainActivity extends AppCompatActivity {
    private final static int SIZE = 4;
    private Game mGame;
    private Button[][] mButtons;
    private GridLayout mGridLayout;
    private TextView mTextView;
    private String movesText;
    private Button resetBtn;
    private boolean soundEnabled;
    SoundPool sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null){
            mGame = (Game) savedInstanceState.getSerializable("game");
        }

        mGridLayout = (GridLayout) findViewById(R.id.gridlayout);
        mGridLayout.setRowCount(SIZE);
        mGridLayout.setColumnCount(SIZE);
        mTextView = (TextView) findViewById(R.id.movesid);
        resetBtn = (Button) findViewById(R.id.resetbtn);
        movesText = getString(R.string.movescnt);

        mGame = mGame == null ? new Game(SIZE) : mGame;
        genButtons();
        draw();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            sp = new SoundPool.Builder().setMaxStreams(1).build();
        }else{
            sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }
        sp.load(this, R.raw.sound, 10);

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mGame.getMoves() > 0){
                    new AlertDialog.Builder(MainActivity.this).setMessage(R.string.resetmsg).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            shuffle();
                        }
                    }).setNegativeButton(R.string.no, null).create().show();
                }else {
                    shuffle();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("game", mGame);
    }

    private void genButtons(){
        mButtons = new Button[SIZE][SIZE];
        for(int i = 0; i < SIZE; i++){
            for(int j = 0; j < SIZE; j++){
                final int finalI = i;
                final int finalJ = j;
                Button curButton = (Button) getLayoutInflater().inflate(R.layout.btn, null);
                Display display = getWindowManager().getDefaultDisplay();
                int btnWidth = display.getWidth() / SIZE;
                mGridLayout.addView(curButton);
                curButton.getLayoutParams().width = btnWidth;
                curButton.getLayoutParams().height = btnWidth;
                curButton.setTextSize(btnWidth / 5f);
                mButtons[j][i] = curButton;
                curButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(mGame.move(new Puzzle(finalJ, finalI)) && soundEnabled){
                                sp.play(1, .3f, .3f, 10, 0, 1.0f);
                        }
                        draw();
                        if(mGame.isWin()){
                            showWinDialog();
                        }
                    }
                });
            }
        }
    }

    private void draw(){
        mTextView.setText(movesText + mGame.getMoves());
        boolean win = mGame.isWin();
        for(int i = 0; i < SIZE; i++){
            for( int j = 0; j < SIZE; j++){
                int num = mGame.getNumber(j, i);
                if(num == 0){
                    mButtons[j][i].setText("");
                    mButtons[j][i].setVisibility(View.INVISIBLE);
                }else{
                    mButtons[j][i].setEnabled(!win);
                    mButtons[j][i].setText(num + "");
                    mButtons[j][i].setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void showWinDialog(){
        String message = getString(R.string.win) + mGame.getMoves();
        new AlertDialog.Builder(this).setMessage(message).setPositiveButton(R.string.reset, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mGame.shuffle();
                draw();
            }
        }).setNegativeButton(R.string.cancel, null).create().show();
        mTextView.setText(message);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.findItem(R.id.soundenable).setChecked(soundEnabled);
        menu.findItem(R.id.soundenable).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                soundEnabled = !soundEnabled;
                menuItem.setChecked(soundEnabled);
                return false;
            }
        });
        menu.findItem(R.id.hardshuffle).setChecked(mGame.getHardShuffle());
        menu.findItem(R.id.hardshuffle).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                mGame.setHardShuffle(!mGame.getHardShuffle());
                menuItem.setChecked(mGame.getHardShuffle());
                return false;
            }
        });
        return true;
    }

    private void shuffle(){
        mGame.shuffle();
        draw();
    }
}
