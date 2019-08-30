package com.pranavjayaraj.intellimind.AutoComplete;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;

import com.pranavjayaraj.intellimind.MainActivity;
import com.pranavjayaraj.intellimind.Trie.Trie;

import java.util.List;

/**
 * Created by kuttanz on 26/8/19.
 */

public class CustomAutoCompleteTextChangedListener implements TextWatcher {

    public static final String TAG = "CustomAutoCompleteTextChangedListener.java";
    Context context;

    public CustomAutoCompleteTextChangedListener(Context context){
        this.context = context;
    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onTextChanged(CharSequence userInput, int start, int before, int count) {
        // if you want to see in the logcat what the user types

        MainActivity speechConversation = ((MainActivity) context);

        // query the database based on the user input
        speechConversation.item = speechConversation.getItemsFromDb();
        // Construct trie
        int i;
        Trie trie = new Trie();
        for (i = 0; i < speechConversation.item.length ; i++)
            trie.insert(speechConversation.item[i]);
        List<String> result= trie.autocomplete(userInput.toString());
        // update the adapater
        speechConversation.myAdapter.notifyDataSetChanged();
        speechConversation.myAdapter = new ArrayAdapter<String>(speechConversation, android.R.layout.simple_dropdown_item_1line, result);
        speechConversation.search.setAdapter(speechConversation.myAdapter);

    }

}