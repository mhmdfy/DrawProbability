package org.mhmdfy.drawprobability;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class CalcActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);

        // Populate spinner
        Spinner spinner = (Spinner) findViewById(R.id.equality_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.equality,
                android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        View calcButton = findViewById(R.id.calculate_button);
        calcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int copies = getIntFromEditText(R.id.copies);
                int deck = getIntFromEditText(R.id.deck);
                int draws = getIntFromEditText(R.id.draws);
                int desired = getIntFromEditText(R.id.desired);

                Spinner equalitySpinner = (Spinner) findViewById(R.id.equality_spinner);
                boolean equality = equalitySpinner.getSelectedItem().toString().toLowerCase().equals("exactly");

                TextView answer = (TextView) findViewById(R.id.percent);
                double percent = Math.round(calculate(copies, deck, draws, desired, equality) * 10000)/100.0;
                answer.setText(String.valueOf(percent)+"%");
                hideSoftKeyboard();
            }
        });

    }

    private double calculate(int copies, int deck, int draws, int desired, boolean equal) {

        //Log.d("Calc", "values are:\nCopies = "+copies+"\nDeck = "+deck+"\nDraws = "
                //+draws+"\nDesired = " + desired +"\nEqual? = " + equal);

        if(equal || desired == 0)
            return hypergeometric(copies, deck, draws, desired);
        else
            return 1.0 - hypergeometric(deck-copies, deck, draws, draws-desired+1);
    }

    private double hypergeometric(int K, int N, int n, int k) {
        //Log.d("hyper", "h("+K+", "+N+", "+n+", "+k+")");
        return (choose(K, k) * choose(N-K, n-k))/choose(N, n);
    }

    private double choose(int n, int k) {
        if(n < k)
            return 0;
        else if (n == k)
            return 1;
        else
            return factorial(n)/(factorial(k) * factorial(n-k));
    }
    private double factorial(int n){
        if (n < 0)
            return 0;
        else if (n == 1 || n == 0)
            return 1;
        else
            return n * factorial(n-1);
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if(getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }

    private int getIntFromEditText(int id) {
        return Integer.parseInt(((EditText) findViewById(id)).getText().toString());
    }

}
