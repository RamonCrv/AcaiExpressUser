package com.example.gs.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class sheet extends BottomSheetDialogFragment {

    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contenView = View.inflate(getContext(),R.layout.activity_sheet,null);
        dialog.setContentView(contenView);


    }
}
