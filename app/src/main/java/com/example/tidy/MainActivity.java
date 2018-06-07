package com.example.tidy;

import android.animation.Animator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.example.tidy.adapters.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    FloatingActionButton fab, fab2, fab3, fab4;
    LinearLayout layoutFab2, layoutFab3, layoutFab4;

    private Animation fab_open,fab_close,rotate_forward,rotate_backward;

    boolean isFABOpen=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(TasksFragment.getInstance(), "Tasks");
        adapter.addFragment(NotesFragment.getInstance(), "Notes");

        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab4 = (FloatingActionButton) findViewById(R.id.fab4);
        layoutFab2 = (LinearLayout) findViewById(R.id.layout_fab2);
        layoutFab3 = (LinearLayout) findViewById(R.id.layout_fab3);
        layoutFab4 = (LinearLayout) findViewById(R.id.layout_fab4);

        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    showFABMenu();
                    fab.startAnimation(rotate_forward);
                }else{
                    closeFABMenu();
                    fab.startAnimation(rotate_backward);
                }
            }
        });
    }

    private void showFABMenu(){
        isFABOpen=true;

        layoutFab2.setVisibility(View.VISIBLE);
        layoutFab3.setVisibility(View.VISIBLE);
        layoutFab4.setVisibility(View.VISIBLE);

        layoutFab2.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        layoutFab3.animate().translationY(-getResources().getDimension(R.dimen.standard_100));
        layoutFab4.animate().translationY(-getResources().getDimension(R.dimen.standard_145));
    }

    private void closeFABMenu(){
        isFABOpen=false;

        layoutFab2.animate().translationY(0);
        layoutFab3.animate().translationY(0);
        layoutFab4.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (!isFABOpen) {
                    layoutFab2.setVisibility(View.GONE);
                    layoutFab3.setVisibility(View.GONE);
                    layoutFab4.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }
}
