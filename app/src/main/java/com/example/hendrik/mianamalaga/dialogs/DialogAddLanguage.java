package com.example.hendrik.mianamalaga.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.hendrik.mianamalaga.activities.ActivityBase;
import com.example.hendrik.mianamalaga.utilities.Utils;
import com.example.hendrik.mianamalaga.R;
import com.example.hendrik.mianamalaga.activities.ActivityLanguageChoice;
import com.example.hendrik.mianamalaga.adapter.AdapterLanguageChoice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DialogAddLanguage extends AppCompatDialogFragment {

    TextView mLanguageTextView;
    RecyclerView mLanguageRecyclerView;
    AdapterLanguageChoice mLanguageAdapter;
    ArrayList<Locale> mKnownLocalesArrayList;
    Locale mLanguageLocale;


    public static DialogAddLanguage newInstance(String fullDirectory){
        DialogAddLanguage dialogAddLanguage = new DialogAddLanguage();
        Bundle args = new Bundle();
        args.putString("Full Directory Name",fullDirectory);
        dialogAddLanguage.setArguments(args);
        return dialogAddLanguage;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String fullDirectory;

        if(getArguments() != null){
            fullDirectory = getArguments().getString("Full Directory Name");
        } else {
            fullDirectory = null;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View baseView = inflater.inflate(R.layout.dialog_add_language, null);

        mLanguageTextView = baseView.findViewById(R.id.dialog_add_language_language_text_view);
        mLanguageRecyclerView = baseView.findViewById(R.id.dialog_add_language_language_recycler_view);

        mKnownLocalesArrayList = new ArrayList<>();
        String[] languages = Locale.getISOLanguages();
        Map<String, Locale> localeMap = new HashMap<>(languages.length);
        for (String language : languages) {
            Locale locale = new Locale(language);
            localeMap.put(locale.getLanguage(), locale);
        }

        for (Locale locale : localeMap.values()){
            mKnownLocalesArrayList.add( locale );
        }

        Utils.sortLocales( mKnownLocalesArrayList );

        mLanguageAdapter = new AdapterLanguageChoice( mKnownLocalesArrayList, R.layout.list_element_language_dialog );
        mLanguageAdapter.setOnItemClickListener((position, viewHolder) -> {
            mLanguageLocale =  mKnownLocalesArrayList.get( position );
            mLanguageTextView.setText( "Add language:      " + mLanguageLocale.getDisplayName() );
        });
        mLanguageRecyclerView.setAdapter(mLanguageAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager( this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLanguageRecyclerView.setLayoutManager(layoutManager);

        mLanguageAdapter.notifyDataSetChanged();
        builder.setView(baseView);
        builder.setCancelable(true);

        builder.setPositiveButton(R.string.Add, (dialog, which) -> {
            if( mLanguageLocale != null){
                ((ActivityBase)getActivity()).onDialogAddLanguagePosClick( mLanguageLocale, fullDirectory );
            } else {
                mLanguageTextView.setText("You need to choose a language!");
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            ;
        });

        return builder.create();
    }
}
