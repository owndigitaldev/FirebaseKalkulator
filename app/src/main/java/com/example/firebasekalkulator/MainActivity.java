package com.example.firebasekalkulator;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ArrayAdapter<String> historyAdapter;
    private FirebaseFirestore db;
    //RecyclerView recyclerView;
    //AdapterHistory adapterHistory;
    //List<History> historyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        historyAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, new ArrayList<String>());
        ListView historyView = findViewById(R.id.historyList);
        historyView.setAdapter(historyAdapter);
        db = FirebaseFirestore.getInstance();
    }

    public void hitung(View view) {
        EditText firstNumberInput = findViewById(R.id.angka1);
        EditText secondNumberInput = findViewById(R.id.angka2);
        float angkaSatu = Float.parseFloat(firstNumberInput.getText().toString());
        float angkaDua = Float.parseFloat(secondNumberInput.getText().toString());
        RadioButton add = findViewById(R.id.tambahBut);
        RadioButton min = findViewById(R.id.kurangBut);
        RadioButton dob = findViewById(R.id.kaliBut);
        RadioButton div = findViewById(R.id.bagiBut);

        float result = 0;

        if (add.isChecked()){
            result = angkaSatu + angkaDua;
        } else if (min.isChecked()){
            result = angkaSatu - angkaDua;
        } else if (dob.isChecked()){
            result = angkaSatu * angkaDua;
        } else if (div.isChecked()){
            result = angkaSatu / angkaDua;
        }

        TextView hasilField = findViewById(R.id.resultField);
        hasilField.setText(" " + result);
        Map<String, Object> his = new HashMap<>();

        if (add.isChecked()){
            his.put("t"," "+angkaSatu+" + "+angkaDua+" = "+result);
        } else if (min.isChecked()){
            his.put("t"," "+angkaSatu+" - "+angkaDua+" = "+result);
        } else if (dob.isChecked()){
            his.put("t"," "+angkaSatu+" x "+angkaDua+" = "+result);
        } else if (div.isChecked()){
            his.put("t"," "+angkaSatu+" / "+angkaDua+" = "+result);
        }

        db.collection("data_his")
                .add(his)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getBaseContext(),
                                "Input Data Sukses",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("firebase-error", e.getMessage());
                    }
                });

        db.collection("data_his")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        String teks = "";
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot doc: task.getResult()){
                                teks += doc.getData().get("t")+"\n";
                                historyAdapter.add(teks);
                            }
                        }else {

                        }
                    }
                });

//        if (add.isChecked()){
//            historyAdapter.add(
//                    " "+angkaSatu+" + "+angkaDua+" = "+result
//            );
//        } else if (min.isChecked()){
//            historyAdapter.add(
//                    " "+angkaSatu+" - "+angkaDua+" = "+result
//            );
//        } else if (dob.isChecked()){
//            historyAdapter.add(
//                    " "+angkaSatu+" x "+angkaDua+" = "+result
//            );
//        } else if (div.isChecked()){
//            historyAdapter.add(
//                    " "+angkaSatu+" / "+angkaDua+" = "+result
//            );
//        }
//        adapterHistory = new AdapterHistory(this,historyList);
//        recyclerView.setAdapter(adapterHistory);

    }
}
