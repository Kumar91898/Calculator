package com.example.calculator;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextToSpeech textToSpeech;
    private double firstNum;
    private String operation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Text-to-Speech engine
        textToSpeech = new TextToSpeech(this, status -> {
            if (status != TextToSpeech.ERROR) {
                textToSpeech.setLanguage(Locale.US);
            }
        });

        // Initialize buttons and screen TextView
        Button num0 = findViewById(R.id.num0);
        Button num1 = findViewById(R.id.num1);
        Button num2 = findViewById(R.id.num2);
        Button num3 = findViewById(R.id.num3);
        Button num4 = findViewById(R.id.num4);
        Button num5 = findViewById(R.id.num5);
        Button num6 = findViewById(R.id.num6);
        Button num7 = findViewById(R.id.num7);
        Button num8 = findViewById(R.id.num8);
        Button num9 = findViewById(R.id.num9);
        Button on = findViewById(R.id.on);
        Button off = findViewById(R.id.off);
        Button ac = findViewById(R.id.ac);
        Button del = findViewById(R.id.del);
        Button div = findViewById(R.id.div);
        Button times = findViewById(R.id.times);
        Button min = findViewById(R.id.min);
        Button equal = findViewById(R.id.equal);
        Button plus = findViewById(R.id.plus);
        Button point = findViewById(R.id.point);

        TextView screen = findViewById(R.id.screen);

        // Set click listeners for number buttons
        ArrayList<Button> nums = new ArrayList<>();
        nums.add(num0);
        nums.add(num1);
        nums.add(num2);
        nums.add(num3);
        nums.add(num4);
        nums.add(num5);
        nums.add(num6);
        nums.add(num7);
        nums.add(num8);
        nums.add(num9);

        for (Button b : nums){
            b.setOnClickListener(view ->{
                String buttonText = b.getText().toString();
                if (!screen.getText().toString().equals("0")){
                    screen.setText(screen.getText().toString() + buttonText);
                } else {
                    screen.setText(buttonText);
                }
                speakOut(buttonText);
            });
        }

        // Set click listeners for operation buttons
        ArrayList<Button> opers = new ArrayList<>();
        opers.add(div);
        opers.add(times);
        opers.add(min);
        opers.add(plus);

        for (Button button : opers) {
            String opersText = button.getText().toString();
            button.setOnClickListener(view -> {
                firstNum = Double.parseDouble(screen.getText().toString());
                operation = button.getText().toString();
                screen.setText("0");
                speakOut(opersText);
            });
        }

        // Set click listener for point button
        point.setOnClickListener(view -> {
            if (!screen.getText().toString().contains(".")) {
                appendToScreen(".");
            }
        });

        // Set click listener for delete button
        del.setOnClickListener(view -> {
            String num = screen.getText().toString();
            if (num.length() > 1) {
                screen.setText(num.substring(0, num.length() - 1));
            } else if (num.length() == 1 && !num.equals("0")) {
                screen.setText("0");
            }
        });

        // Set click listener for clear (AC) button
        ac.setOnClickListener(view -> {
            firstNum = 0;
            screen.setText("0");
        });

        // Set click listener for equal button
        equal.setOnClickListener(view -> {
            double secondNum = Double.parseDouble(screen.getText().toString());
            double result;

            switch (operation) {
                case "/":
                    result = firstNum / secondNum;
                    break;
                case "X":
                    result = firstNum * secondNum;
                    break;
                case "+":
                    result = firstNum + secondNum;
                    break;
                default:
                    result = firstNum - secondNum;
                    break;
            }

            screen.setText(String.valueOf(result));
            firstNum = result;

            String result2 = Double.toString(result);
            speakOut(result2);
        });

        // Set click listener for ON button
        on.setOnClickListener(view -> {
            screen.setVisibility(View.VISIBLE);
            screen.setText("0");
        });

        // Set click listener for OFF button
        off.setOnClickListener(view -> screen.setVisibility(View.GONE));
    }

    // Method to append text to the screen
    private void appendToScreen(String text) {
        TextView screen = findViewById(R.id.screen);
        screen.append(text);
    }

    @Override
    protected void onDestroy() {
        // Shutdown Text-to-Speech engine when the activity is destroyed
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    private void speakOut(String text) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }
}
