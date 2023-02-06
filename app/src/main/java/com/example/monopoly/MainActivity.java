package com.example.monopoly;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;



import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.monopoly.databinding.ActivityMainBinding;
import com.example.monopoly.game.data.MonopolyDatabase;



public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private NavController navController;
    private FragmentManager fragmentManager;

    public static int SHAKE_LEVEL = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        fragmentManager = getSupportFragmentManager();

        NavHostFragment navHost = (NavHostFragment) fragmentManager
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHost.getNavController();

        MonopolyDatabase monopolyDatabase = MonopolyDatabase.getInstance(this);


    }


    @Override
    public void onBackPressed() {
        switch (navController.getCurrentDestination().getId()) {
            case R.id.gameFragment:
                new AlertDialog.Builder(this)
                        .setTitle("Quit")
                        .setMessage("Are you sure you want to quit game?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                NavDirections action = GameFragmentDirections.actionGameFragmentToHomeFragment();
                                navController.navigate(action);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                break;

            case R.id.winnerFragment:
                NavDirections action = WinnerFragmentDirections.actionWinnerFragmentToHomeFragment();
                navController.navigate(action);
                break;

            case R.id.matchHistoryFragment:
                NavDirections action2 = MatchHistoryFragmentDirections.actionMatchHistoryFragmentToHomeFragment();
                navController.navigate(action2);
                break;

            case R.id.homeFragment:
                new AlertDialog.Builder(this)
                        .setTitle("Quit")
                        .setMessage("Are you sure you want to quit Monopoly?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finishAndRemoveTask();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                break;
            default:
                super.onBackPressed();
                break;
        }
    }
}