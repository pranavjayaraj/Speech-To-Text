package com.pranavjayaraj.intellimind.AutoComplete;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.pranavjayaraj.intellimind.MainActivity;
import com.pranavjayaraj.intellimind.Trie.Trie;

import java.util.List;

/**
 * Created by Pranav on 26/8/19.
 */

public class CustomAutoCompleteTextChangedListener implements TextWatcher {

    public static final String TAG = "CustomAutoCompleteTextChangedListener.java";
    Context context;
    AutoCompleteTextView autoCompleteTextView;

    public CustomAutoCompleteTextChangedListener(Context context, AutoCompleteTextView autoCompleteTextView) {
        this.context = context;
        this.autoCompleteTextView = autoCompleteTextView;
    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        int wordsLength = countWords(s.toString());// words.length;
        // count == 0 means a new word is going to start
        if (count == 0 && wordsLength >= 10) {
            setCharLimit(autoCompleteTextView, autoCompleteTextView.getText().length());
        } else {
            removeFilter(autoCompleteTextView);
        }
    }

    // TODO Auto-generated method stub

    @Override
    public void onTextChanged(CharSequence userInput, int start, int before, int count) {
        // if you want to see in the logcat what the user types

        MainActivity speechConversation = ((MainActivity) context);

        // query the database based on the user input
        speechConversation.item = speechConversation.getItemsFromDb();
        // Construct trie
        int i;
        Trie trie = new Trie();
        for (i = 0; i < speechConversation.item.length; i++)
            trie.insert(speechConversation.item[i]);
        List<String> result = trie.autocomplete(userInput.toString());
        // update the adapater
        speechConversation.myAdapter.notifyDataSetChanged();
        speechConversation.myAdapter = new ArrayAdapter<String>(speechConversation, android.R.layout.simple_dropdown_item_1line, result);
        speechConversation.search.setAdapter(speechConversation.myAdapter);
    }

    private int countWords(String s) {
        String trim = s.trim();
        if (trim.isEmpty())
            return 0;
        return trim.split("\\s+").length; // separate string around spaces
    }

    private InputFilter filter;

    private void setCharLimit(AutoCompleteTextView et, int max) {
        filter = new InputFilter.LengthFilter(max);
        et.setFilters(new InputFilter[]{filter});
    }

    private void removeFilter(AutoCompleteTextView et) {
        if (filter != null) {
            et.setFilters(new InputFilter[0]);
            filter = null;
        }

    }
}