/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.example;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hp
 */
public class Tagger {

    private Messenger my_consol;
    private DB_connection dB_connection;

    public Tagger(){

    }

    public Tagger(Messenger mMy_console, DB_connection dDB_connection){
        my_consol = mMy_console;
        dB_connection = dDB_connection;
    }

    public void check_grammer_errors(ArrayList<Word> words){

        Object[] return_o = check_grammer_full_errors(words);

        words = (ArrayList<Word>)return_o[1];

        ///////////////////////////////////////////
        String taged_string = "";
        String full_sentence = "";

        for (int i = 0; i < words.size(); i++) {
            taged_string += words.get(i).get_in_string_format();
            full_sentence += words.get(i).get_word() + " ";
        }

        my_consol.write("-------------------------------------------\n" + full_sentence);

        my_consol.write("Taged string ::: " + taged_string);
        ///////////////////////////////////////////

        if((Boolean)return_o[0]){
        //    //at this point as there are no grammar errors "!!! ### all tags that can come for a matching patterns### !!!" are present for words
            simplify_n_check_grammar(words);
        }

    }

    public Object[] check_grammer_full_errors(ArrayList<Word> words){

        boolean no_gram_errors = true;

        //index of pre word
        //int gram_error_index = 0;

        //for i = 0 it will be the first pre_string
        //for i = words.size it will be the last next_word
        OUTER_lOOP: for (int i = 1; i < words.size() - 1; i++) {
            
            //if no pre_word it will be given in the array
            Word pre_word = words.get(i-1);
            Word center_word = words.get(i);
            Word next_word = words.get(i+1);

            //as tags are not present in word other than start and end tags
            for (int j = -1; j < 2; j++) {
                //skips word containing start tag and end tags
                // + skips words which alredy has been taged
                if(!words.get(i+j).were_tags_set()){
                        if(!words.get(i+j).set_tags(dB_connection.get_tags(words.get(i+j).get_word()))){
                            words.get(i+j).add_tag(Word.word_not_found);
                            break OUTER_lOOP;
                        }
                }
            }
            
            Word temp_pre_word = new Word(pre_word.get_word());
            Word temp_center_word = new Word(center_word.get_word());
            Word temp_next_word = new Word(next_word.get_word());

            //max abut: 5 loop
            for (int j = 0; j < pre_word.num_tags(); j++) {
                //max abut: 5 loop
                for (int k = 0; k < center_word.num_tags(); k++) {
                    //max abut: 5 loop
                    for (int n = 0; n < next_word.num_tags(); n++) {
                            String[] tags = {pre_word.get_tag(j), center_word.get_tag(k), next_word.get_tag(n)};

                            if(dB_connection.check_pattern(tags)){
                                //ok
                                temp_pre_word.add_tag(pre_word.get_tag(j));
                                temp_pre_word.set_word_no(pre_word.get_word_no());
                                temp_pre_word.set_pre_adv_adj(pre_word.get_pre_adv_adj());

                                temp_center_word.add_tag(center_word.get_tag(k));
                                temp_center_word.set_word_no(center_word.get_word_no());
                                temp_center_word.set_pre_adv_adj(center_word.get_pre_adv_adj());

                                temp_next_word.add_tag(next_word.get_tag(n));
                                temp_next_word.set_word_no(next_word.get_word_no());
                                temp_next_word.set_pre_adv_adj(next_word.get_pre_adv_adj());

                                //!! cannot break, should check every tag pattern inorder to eleminate unwanted tags
                            }else{
                                //words.add(new Word("bbbb"));
                                //do not remove tags
                                //  just added in above
                            }
                    } 
                }
            }

            if(temp_pre_word.has_no_tags() || temp_center_word.has_no_tags() || temp_next_word.has_no_tags()){
                
                //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                // # not needed words.add(new Word(" g e i:" + (i-1)));
                Word[] sujsted_words = giv_suj_gram_error(words.get(i-1), words.get(i), words.get(i + 1));

                words.set(i-1, sujsted_words[0]);
                words.set(i, sujsted_words[1]);
                words.set(i + 1, sujsted_words[2]);
                //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                
                no_gram_errors = false;

                //if grammer error stops looping futher
                //  :. no futher adding taggin
                break OUTER_lOOP;
            }else{
                words.set(i-1, temp_pre_word);
                words.set(i, temp_center_word);
                words.set(i+1, temp_next_word);
            }
            
        }

        //new Messenger(no_gram_errors + "");

        Object[] return_o = {no_gram_errors, words};

        return return_o;
    }
    
    private void simplify_n_check_grammar(ArrayList<Word> words){
        // new Messenger("1");

        ArrayList<Word> sim_sentence_words = new ArrayList<Word>();

        //String sizes = "## ";
        
        for (int i = 0; i < words.size(); i++) {
            //words.get(i).set_word_no(i);
            
            for (int j = 0; j < words.get(i).num_tags(); j++) {
                if(dB_connection.check_tags_in_sim_set(words.get(i).get_tag(j))){
                    sim_sentence_words.add(words.get(i));
                    //same word will not be added for multipel times
                    break;
                }
            }
        }

        //my_consol.write(sizes);
        /*
        String sim_words ="";

        for (int i = 0; i < sim_sentence_words.size(); i++) {
            sim_words += sim_sentence_words.get(i).get_in_string_format();
        }
        */
        //my_consol.write(sim_words);

        Object[] returned_o = check_grammer_full_errors(sim_sentence_words);

        ArrayList<Word> sim_words_arr = (ArrayList<Word>) returned_o[1];
        /*
        String taged_string = "";
        for (int i = 0; i < sim_words_arr.size(); i++) {
            taged_string += sim_words_arr.get(i).get_in_string_format() + "(" + sim_words_arr.get(i).get_word_no() + ")";
        }
        */

        String taged_string = "";
        int sim_word_pointer = 0;
        for (int i = 0; i < words.size(); i++) {

            if(sim_words_arr.get(sim_word_pointer).get_word_no() == i){
                words.set(i, sim_words_arr.get(sim_word_pointer));
                sim_word_pointer++;
            }

            taged_string += words.get(i).get_in_string_format();

            //taged_string += sim_words_arr.get(i).get_in_string_format() + "(" + sim_words_arr.get(i).get_word_no() + ")";
        }

        my_consol.write("Taged string (simlified)::: " + taged_string);
    }

    private Word[] giv_suj_gram_error(Word pre_word, Word center_word, Word next_word){
        
        ArrayList<Word> related_words  = dB_connection.get_related_words(next_word.get_word());
        ArrayList<Word> possible_words = new ArrayList<Word>();

        for (int i = 0; i < pre_word.num_tags(); i++) {
                for (int j = 0; j < center_word.num_tags(); j++) {
                       ArrayList<String> possible_tags_after =  dB_connection.get_possible_tags_for_tag_after(pre_word.get_tag(i), center_word.get_tag(j));

                       //new Messenger(possible_tags_after.toString());

                       //select words from related_words who has possible_tags_after
                        for (int k = 0; k < related_words.size(); k++) {
                                Word related_word = related_words.get(k);
                                int no_maching_tags = 0;

                               // new Messenger(related_word.get_word());

                                for (int l = 0; l < possible_tags_after.size(); l++) {
                                    if(related_word.has_tag(possible_tags_after.get(l))){
                                          no_maching_tags++;

                                          //not nessasary to proceed\
                                          //    as atleast one tag is present in the word
                                          break;
                                    }
                                }

                                if(no_maching_tags > 0){
                                    //related_word should be suggested
                                    // if related_word is not in possible_words add it
                                    if(possible_words.lastIndexOf(related_word) == -1){
                                            possible_words.add(related_word);
                                    }
                                }
                        }
                       
                }
        }

        //pre_word.add_tag(" error 1");
        //center_word.add_tag(" error 2");

        String sugg_words_string = "";

        for (int i = 0; i < possible_words.size(); i++) {
            sugg_words_string += "{" +possible_words.get(i).get_word() + "}";
        }
        

        next_word.add_tag(" error!! suggestions:" + sugg_words_string);

        Word[] sujsted_words= {pre_word, center_word, next_word};
        
        return sujsted_words;
    }

    public Tag[][] get_tag_sqence(ArrayList<Word> words_obj_array){
        my_consol.write("creating tag squence...............................");

         int no_of_tag_sqences = 1;

         for (int i = 0; i < words_obj_array.size(); i++) {
            no_of_tag_sqences *= words_obj_array.get(i).no_of_tags();
        }

         my_consol.write("no_of_tag_sqences: " + no_of_tag_sqences);
                                                        //->                              v
         Tag[][] tag_array = new Tag[no_of_tag_sqences][words_obj_array.size()];

         for (int i = 0; i < words_obj_array.size(); i++) {

             int tag_seq_no = 0;

             for (int j = 0; j < no_of_tag_sqences/words_obj_array.get(i).num_tags(); j++) {
                 for (int k = 0; k < words_obj_array.get(i).num_tags(); k++) {
                    tag_array[tag_seq_no][i] = new Tag(words_obj_array.get(i).get_tag(k));
                    tag_seq_no++;
                 }
             }
        }
         return tag_array;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
}




