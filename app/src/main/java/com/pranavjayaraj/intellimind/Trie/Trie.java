package com.pranavjayaraj.intellimind.Trie;

/**
 * Created by Pranav on 27/8/19.
 */

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

class TrieNode {
    char data;
    LinkedList<TrieNode> children;
    TrieNode parent;
    boolean isEnd;

    public TrieNode(char c) {
        data = c;
        children = new LinkedList<TrieNode>();
        isEnd = false;
    }

    public TrieNode getChild(char c) {
        if (children != null)
            for (TrieNode eachChild : children)
                if (eachChild.data == c)
                    return eachChild;
        return null;
    }

    protected List<String> getWords() {
        List<String> list = new ArrayList<String>();
        if (isEnd) {
            list.add(toString());
        }

        if (children != null) {
            for (int i=0; i< children.size(); i++) {
                if (children.get(i) != null) {
                    list.addAll(children.get(i).getWords());
                }
            }
        }
        return list;
    }

    public String toString() {
        if (parent == null) {
            return "";
        } else {
            return parent.toString() + new String(new char[] {data});
        }
    }
}
public class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode(' ');
    }

    public void insert(String word) {
        if (search(word) == true)
            return;

        TrieNode current = root;
        TrieNode pre ;
        for (char ch : word.toCharArray()) {
            pre = current;
            TrieNode child = current.getChild(ch);
            if (child != null) {
                current = child;
                child.parent = pre;
            } else {
                current.children.add(new TrieNode(ch));
                current = current.getChild(ch);
                current.parent = pre;
            }
        }
        current.isEnd = true;
    }

    public boolean search(String word) {
        TrieNode current = root;
        for (char ch : word.toCharArray()) {
            if (current.getChild(ch) == null)
                return false;
            else {
                current = current.getChild(ch);
            }
        }
        if (current.isEnd == true) {
            return true;
        }
        return false;
    }

    public List<String> autocomplete(String prefix) {
        TrieNode lastNode = root;
        for (int i = 0; i< prefix.length(); i++) {
            lastNode = lastNode.getChild(prefix.charAt(i));
            if (lastNode == null)
                return new ArrayList<String>();
        }

        return lastNode.getWords();
    }
}
