package ca.dal.cs.csci3130.groupproject;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class PaySalaryActivity extends AppCompatActivity {

    private static final String TAG = PaySalaryActivity.class.getName();
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private PayPalConfiguration payPalConfiguration;
    private EditText enterAmtET;
    private Button payNowBtn;
    private TextView paymentStatusTV;
    private String salary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_salary);

        this.getValues();

        init();
        configPayPal();
        initActivityLauncher();
        setListeners();

    }

    protected void getValues(){
        SharedPreferences salaryPreference = getSharedPreferences("salary", Context.MODE_PRIVATE);
        salary = salaryPreference.getString("Salary", "");
    }

    private void init(){
        enterAmtET = findViewById(R.id.edtAmount);
        payNowBtn = findViewById(R.id.btnPayNow);
        paymentStatusTV = findViewById(R.id.idTVStatus);
    }

    private void configPayPal(){
        payPalConfiguration = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(BuildConfig.PAYPAL_CLIENT_ID);
    }

    private void initActivityLauncher(){
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        final PaymentConfirmation confirmation = result.getData().getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                        if (confirmation != null) {
                            try {
                                // Getting the payment details
                                String paymentDetails = confirmation.toJSONObject().toString(4);
                                // on below line we are extracting json response and displaying it in a text view.
                                JSONObject payObj = new JSONObject(paymentDetails);
                                String payID = payObj.getJSONObject("response").getString("id");
                                String state = payObj.getJSONObject("response").getString("state");
                                paymentStatusTV.setText("Payment " + state + "\n with payment id is " + payID);
                            } catch (JSONException e) {
                                // handling json exception on below line
                                Log.e("Error", "an extremely unlikely failure occurred: ", e);
                            }
                        }
                    } else if (result.getResultCode() == PaymentActivity.RESULT_EXTRAS_INVALID){
                        Log.d(TAG,"Launcher Result Invalid");
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        Log.d(TAG, "Launcher Result Cancelled");
                    }

                });
    }

    private void setListeners(){
        payNowBtn.setOnClickListener(v -> processPayment());
    }

    private void processPayment(){
        // Getting amount from editText
        final String amount = enterAmtET.getText().toString();

        if (amount.isEmpty()){
            enterAmtET.setError(getResources().getString(R.string.EMPTY_ENTER_AMOUNT_TO_PAID).trim());
            enterAmtET.requestFocus();
            return;
        }

        if (Integer.parseInt(amount) < Integer.parseInt(salary)){
            enterAmtET.setError("The salary you entered is less than what you promised, the salary you promised is " + salary + " CAD");
            enterAmtET.requestFocus();
            return;
        }
        // Creating Paypal payment
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(amount),"CAD","Purchase Goods",PayPalPayment.PAYMENT_INTENT_SALE);
        // Creating Paypal Payment activity intent
        Intent intent = new Intent(this, PaymentActivity.class);
        // Adding paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,payPalConfiguration);
        // Adding paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        // Starting Activity Request launcher
        activityResultLauncher.launch(intent);

    }
}