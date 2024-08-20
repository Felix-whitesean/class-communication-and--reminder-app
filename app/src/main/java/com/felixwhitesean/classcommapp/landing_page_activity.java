package com.felixwhitesean.classcommapp;

    import android.app.Activity;
    import android.content.Intent;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;
    import android.widget.Button;
    import android.widget.ImageView;
    import android.widget.TextView;

    public class landing_page_activity extends Activity {


    private View _bg__landing_page_ek2;
    private View rectangle_3;
    private View rectangle_2;
    private View rectangle_6;
    private ImageView image_10;
    private ImageView image_8;
    private ImageView image_9;
    private ImageView _1_ao_1;
    private ImageView mask_group;
    private ImageView mask_group_ek1;
    private ImageView mask_group_ek2;
    private ImageView _1_ao_1_ek1;
    private ImageView _1_ao_1_ek2;
    private View ellipse_13;
    private View ellipse_14;
    private View ellipse_15;
    private View ellipse_19;
    private ImageView vector;
    private TextView local_colors;
    private ImageView vector_ek1;
    private View ellipse_21;
    private View ellipse_20;
    private View rectangle_4;
    private ImageView rectangle_29;
    private View rectangle_28;
    private TextView get_started;
    private ImageView vector_ek2;
    private View rectangle_1;
    private View rectangle_3_ek1;
    private View home_indicator;
    private View rectangle_17;
    private TextView ultimate_class;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_page);

        Button getStartedButton = findViewById(R.id.rectangle_28);
        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("acknowledgment","Getting started button clicked");
                // Navigate to authentication_page_activity
                Intent intent = new Intent(landing_page_activity.this, userInfoActivity.class);
                startActivity(intent);
            }
        });
        //custom code goes here

    }
}
	
	