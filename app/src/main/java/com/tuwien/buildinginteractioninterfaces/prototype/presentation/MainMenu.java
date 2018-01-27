package com.tuwien.buildinginteractioninterfaces.prototype.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tuwien.buildinginteractioninterfaces.prototype.R;
import com.tuwien.buildinginteractioninterfaces.prototype.domain.model.OptionsModel;

/**
 * Created by duarte on 27-01-2018.
 */

public class MainMenu extends AppCompatActivity {

    private static final String TAG_ACTIVITY =
                                   MainMenu.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);

        ((RadioGroup) findViewById(R.id.game_mode_group)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                onRadioButtonClicked(checkedId);
            }
        });

        Button button = (Button) findViewById(R.id.button_create_game);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean autoCorrect = ((CheckBox) findViewById(R.id.main_menu_autocorrect)).isChecked();

                int gameModeId = ((RadioGroup) findViewById(R.id.game_mode_group)).getCheckedRadioButtonId();
                OptionsModel.TypeGame typeGame;

                switch (gameModeId){
                    case R.id.main_menu_time:
                        typeGame = OptionsModel.TypeGame.TIME;
                        break;
                    case R.id.main_menu_num_words:
                        typeGame = OptionsModel.TypeGame.NUM_WORDS;
                        break;
                    case R.id.main_menu_errors:
                        typeGame = OptionsModel.TypeGame.NUM_ERRORS;
                        break;
                    case R.id.main_menu_correct_words:
                        typeGame = OptionsModel.TypeGame.NUM_CORRECT_WORDS;
                        break;
                    case R.id.main_menu_text:
                        typeGame = OptionsModel.TypeGame.TEXT;
                        break;
                    case R.id.main_menu_no_end:
                        typeGame = OptionsModel.TypeGame.NO_END;
                        break;
                    default:
                        typeGame = OptionsModel.TypeGame.TIME;
                        break;
                }


                OptionsModel optionsModel = new OptionsModel(typeGame,autoCorrect);


                startActivity(new Intent(MainMenu.this, PlayGame.class).putExtra("OPTIONS", optionsModel));


            }
        });

    }


    public void onRadioButtonClicked(int id) {
        View view = findViewById(id);

        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
        case R.id.main_menu_time:
             if (checked)
                 displayNumberSelection("Time (seconds)");
             break;
        case R.id.main_menu_num_words:
            if (checked)
                displayNumberSelection("Number of words");
            break;
        case R.id.main_menu_errors:
            if (checked)
                displayNumberSelection("Number of errors");
                break;
        case R.id.main_menu_correct_words:
            if (checked)
                displayNumberSelection("Number of correct words");
                break;
        case R.id.main_menu_text:
            if (checked)

                break;
        case R.id.main_menu_no_end:
            if (checked)
                findViewById(R.id.number_input_layout).setVisibility(View.GONE);

                break;

        default:

            break;
       }
    }

    private void displayNumberSelection(String text){
        TextView textView = (TextView) findViewById(R.id.label_main_menu_num);
        textView.setText(text);
        findViewById(R.id.number_input_layout).setVisibility(View.VISIBLE);
    }
}