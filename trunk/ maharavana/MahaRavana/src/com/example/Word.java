/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.example;

import java.util.ArrayList;

/**
 *
 * @author hp
 */
public class Word{

    public  static final String strt_tag = "stt";
    public  static final String strt_word = "START";
    public static final String end_tag = "end";
    public static final String end_word = "END";
    public static final String word_not_found = "WORD NOT FOUND";

    private ArrayList<String> tag_set;
    private String word;

    private ArrayList<Word> adv_adj_words;
    //private boolean adv_adj_words_set = false;

    private boolean tags_were_set;

    public Word(){
        
    }

    public Word(String wWord, ArrayList<String> tTag_set){
        tag_set = tTag_set;
        word = wWord;
        tags_were_set = true;
    }

    public Word(String wWord){
        tag_set = new ArrayList<String>();
        word = wWord;
        tags_were_set = false;
    }

    public Word(String wWord, int wWord_no){
        tag_set = new ArrayList<String>();
        word = wWord;
        tags_were_set = false;
        word_no = wWord_no;
    }

    public void remove_all_tags(){
        tags_were_set = false;
        tag_set = new ArrayList<String>();
    }

    //if tags are not set set tags
    public boolean  set_tags(ArrayList<String> tTag_set){
        if(tTag_set.isEmpty()){
            return false;
        }
        tag_set = tTag_set;
        tags_were_set = true;
        return true;
    }

    public boolean were_tags_set(){
            return tags_were_set;
    }

    public String get_word(){
        return word;
    }

    public void add_tag(String tag){
        tag_set.remove(tag);
        tag_set.add(tag);
        tags_were_set = true;
    }

    //for ambegodas /////////////////////////////////////////////////////////////////////////////////
    public int no_of_tags(){
        return tag_set.size();
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    // for simplify_n_check_grammar /////////////////////////////////////////////////////////
    private int word_no;
    
    public void set_word_no(int wWord_no){
        word_no = wWord_no;
    }

    public int get_word_no(){
        return word_no;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean has_no_tags(){
        if(tag_set.isEmpty()){
            return true;
        }else{
            return false;
        }
    }

    public String get_in_string_format(){
        String adj_adv_string ="";
        
        try{
                if(adv_adj_words.size()>0){
                    String start_of_adj_adv_string = "<";
                    String end_of_adj_adv_string = "> ";

                    for (Word a_word : adv_adj_words) {
                        adj_adv_string += a_word.get_in_string_format();
                    }

                    adj_adv_string = start_of_adj_adv_string + adj_adv_string + end_of_adj_adv_string;
                }
        }catch(NullPointerException e){
            adj_adv_string += "";
        }
        return adj_adv_string + word + tag_set.toString() + ", ";
    }

    public int num_tags(){
        return tag_set.size();
    }

    public String get_tag(int i){
        return tag_set.get(i);
    }

    public boolean has_tag(String tag){
        boolean has_tag=true;

        if(tag_set.lastIndexOf(tag) == -1){
            has_tag=false;
        }

        return has_tag;
    }

    public void set_pre_adv_adj(ArrayList<Word> aAdv_adj_words){
        try{
            adv_adj_words = new ArrayList<Word>();
            for (Word a_word : aAdv_adj_words) {
                adv_adj_words.add(a_word);
            }
            //adv_adj_words_set=true;
        }catch(NullPointerException e){
            //ok
        }
    }

    public ArrayList<Word> get_pre_adv_adj(){
        return adv_adj_words;
    }
}
