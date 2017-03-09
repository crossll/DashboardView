package cross.com.dashboardview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import java.util.Random;

import cross.com.dashboardview.ui.DashboarsView;

public class MainActivity extends Activity {
private DashboarsView dashboard,dashboard1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dashboard=(DashboarsView)findViewById(R.id.dashboard);
        dashboard1=(DashboarsView)findViewById(R.id.dashboard1);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dashboard.setAnimator(70,1000);
            }
        });
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dashboard1.setAnimator(70,1000);
            }
        });
        dashboard.addPrecentChange(new DashboarsView.addPrecentChange() {
            @Override
            public void PrecentChange(float percent) {
                int p=(int)percent;
                dashboard.setTextValue(p+"");
            }
        });




        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                dashboard.setTextValue(progress+"");
                dashboard.setAnimator(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        SeekBar seekBar1 = (SeekBar) findViewById(R.id.seekBar1);
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                dashboard1.setTextValue(progress+"℃");
                dashboard1.setAnimator(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        dashboard1.addPrecentChange(new DashboarsView.addPrecentChange() {
            @Override
            public void PrecentChange(float percent) {
                int p=(int)percent;
                dashboard1.setTextValue(p+"℃");
            }
        });
        draw();
        dashboard1.DrawBackgrond(R.drawable.ico_dable,0);
        dashboard1.DrawPointerPicture(270,180,0.02,0.4,R.drawable.ico_pointer);
        dashboard1.DrawTextValue(0.1,0xff00A797);
    }
    private void draw(){
        dashboard.DrawArc(0.45,240,240,0.1,0xffb0b9c8);
        float[][] angle=new float[6][2];
        int[] color=new int[6];
        for(int i=0;i<6;i++){
            Random random = new Random();
            int ranColor = 0xff000000 | random.nextInt(0x00ffffff);
            angle[i][0]=240+40*i;
            angle[i][1]=40;
            color[i]=ranColor;
        }
        dashboard.DrawArcs(0.25,angle,0.3,color);
        String [] scale={"A","B","C","D","E","F"};
        dashboard.DrawTextScale(0.45,260,200,0.05,0xffff0000,scale);
        dashboard.DrawScale(0.1,0.5,190,160,0.01,0xffffffff,5);
        dashboard.DrawPointer(240,240,0xff000000);
        dashboard.DrawTextValue(0.1,0xffff0000);
    }
}
