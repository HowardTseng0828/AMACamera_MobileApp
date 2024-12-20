package com.example.Mobile.activitys;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.Mobile.adapter.RecordAdapter;
import com.example.Mobile.databinding.ActivityRecordBinding;
import com.example.Mobile.viewmodels.RecordViewModel;

import java.util.Calendar;

public class RecordActivity extends AppCompatActivity {

    private ActivityRecordBinding binding;
    private RecordAdapter recordAdapter;
    private RecordViewModel recordViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recordViewModel = new ViewModelProvider(this).get(RecordViewModel.class);

        setupRecyclerView();
        setupDelayerButton();

        recordViewModel.getRecordsLiveData().observe(this, options -> {
            if (recordAdapter == null) {
                recordAdapter = new RecordAdapter(options);
                binding.recyclerview.setAdapter(recordAdapter);
            } else {
                recordAdapter.updateOptions(options);
            }
        });
    }

    private void setupRecyclerView() {
        binding.recyclerview.setHasFixedSize(true);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupDelayerButton() {
        binding.btnDelayer.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            final DatePickerDialog.OnDateSetListener onDateSetListener = (view1, y, mn, d) -> {
                final TimePickerDialog.OnTimeSetListener onTimeSetListener = (view12, h, m) -> {
                    String delayer = y + "-" + (mn + 1) + "-" + d + "\t" + h + ":" + m + ":" + "00";
                    recordViewModel.setDelayer(delayer);
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(RecordActivity.this, onTimeSetListener, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true);
                timePickerDialog.setTitle("暫停通知至所選時間");
                timePickerDialog.show();
            };
            DatePickerDialog datePickerDialog = new DatePickerDialog(RecordActivity.this, onDateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            datePickerDialog.setTitle("暫停通知至所選時間");
            datePickerDialog.show();
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (recordAdapter != null) {
            recordAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (recordAdapter != null) {
            recordAdapter.stopListening();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
