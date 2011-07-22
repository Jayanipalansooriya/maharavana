/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.example.chunked_checker;

import com.example.DB_connection;
import com.example.Messenger;
import com.example.Tagger;
import com.example.Word;
import com.example.Tag;
import java.util.ArrayList;

/**
 *
 * @author hp
 */
public class Chunker {

     private Messenger my_consol;
     private Tagger tagger;

     private ArrayList<String> last_word_tags_arr;
     private ArrayList<String> adverb_tags_arr;
     private ArrayList<String> noun_tags_arr;
     private ArrayList<String> adj_tags_arr;

     private ArrayList<Word> words;

     private int process_word_index;

     private Tag[][] tag_squ_arr;

    public Chunker(Messenger mMy_consol, Tagger tTagger, DB_connection dB_connection){
        my_consol = mMy_consol;
        tagger = tTagger;

        last_word_tags_arr = dB_connection.get_tags_for(Tag.VERB);
        adverb_tags_arr = dB_connection.get_tags_for(Tag.AD_VERBS);

        noun_tags_arr = dB_connection.get_tags_for(Tag.NOUN);
        adj_tags_arr = dB_connection.get_tags_for(Tag.ADJ);
    }

    public void process_string(ArrayList<Word> words){
        process_thread pt = new process_thread(words);
        pt.start();
    }

    private class process_thread extends Thread{
        public process_thread(ArrayList<Word> wWords){
            words = wWords;
        }

        public void run(){
            my_consol.write("cheking chunked grammar........................................");

            String output_string= "";
            for (int i = 0; i < words.size(); i++) {
                output_string += words.get(i).get_in_string_format();
            }

            my_consol.write("..:" + output_string);

            tag_squ_arr = tagger.get_tag_sqence(words);

            ArrayList<Integer> corret_tag_sequences = new ArrayList<Integer>();

            FOR_LOOP: for (int j = 0; j < tag_squ_arr.length; j++) { // v
                //Tag[] a_tag_squ_arr  = new Tag[tag_squ_arr[0].length];
                String row_res = "";
                for (int i = 0; i < tag_squ_arr[0].length; i++) {//->
                    row_res += tag_squ_arr[j][i].get_tag()+ ", ";
                    //a_tag_squ_arr[i] = new Tag(tag_squ_arr[j][i].get_tag());
                }
                my_consol.write("________________________________________________________________________");
                my_consol.write("Processing tag squ:::: " + row_res);

                if(!chunk_last_verb(/*tag_squ_arr[j] /*a_tag_squ_arr*/ j, words)){
                    break FOR_LOOP;
                }
                //if there are other words than the last verb and its adverbs then it whould have not breaked and can proceed here

                //my_consol.write(":::::::::::::::::::::::::" + tag_squ_arr[j][process_word_index].get_tag());

                int k=0;
                while(k==0){
                     if(!chunk_noun(j, words)){
                         break FOR_LOOP;
                     }
                     if(process_word_index == 0){
                         break;
                     }
                     //my_consol.write(tag_squ_arr[j][process_word_index].get_tag() + ",,,,," + process_word_index + ",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,\n");
                }

                corret_tag_sequences.add(j);

                //my_consol.write("________________________________________________________________________\n");
            }

            for (int i = 0; i < tag_squ_arr.length; i++) {
                String resuting_tag_squ = "";
                for (int j = 0; j < tag_squ_arr[0].length; j++) {
                    resuting_tag_squ += tag_squ_arr[i][j].to_string() + ", ";
                }
                my_consol.write("resuting tag squ:: " + resuting_tag_squ);
            }

            if(corret_tag_sequences.size() > 0){
                for (int i = 0; i < corret_tag_sequences.size(); i++) {
                    my_consol.write(i + " is currently a correct tag sequece ~~~~~~~~");
                }
                process_fully_cunkable_tag_squs(corret_tag_sequences);
            }else{
                my_consol.write("!!!! correct tag sequence not found");
            }
        }

        private void process_fully_cunkable_tag_squs(ArrayList<Integer> corret_tag_sequences){
            my_consol.write("+++++++++++++++++++++++++++++++++++++++++++\n processing fully chunkable tag squ\n");
            for (Word a_word : words) {
                a_word.remove_all_tags();
            }

            ArrayList<Word> chunked_words = new ArrayList<Word>();

            //Word srtn_word =  new Word(Word.strt_word, 0);
            //srtn_word.add_tag(Word.strt_tag);
            //chunked_words.add(srtn_word);

            int chunked_words_count = 0;

            ArrayList<Word> a_pre_adj_adv_arr = new ArrayList<Word>();

            for (int i = 0; i < corret_tag_sequences.size(); i++) {
                for (int j = 0; j < tag_squ_arr[corret_tag_sequences.get(i)].length; j++) {
                    Tag tag = tag_squ_arr[corret_tag_sequences.get(i)][j];
                    words.get(j).add_tag(tag.get_tag());

                    if(tag.is_an_end_of_chunk()){
                        String s="";
                        for (Word word : a_pre_adj_adv_arr) {
                            s += ", " + word.get_word();
                        }

                        my_consol.write("s>>>>>>>>" + s);

                        Word a_word = words.get(j);
                        a_word.set_pre_adv_adj(a_pre_adj_adv_arr);
                        
                        chunked_words.add(a_word);
                        chunked_words_count++;
                        
                        //my_consol.write(" ............................................................................... ");
                        
                        a_pre_adj_adv_arr.clear();
                    }else{
                        //my_consol.write(j + " ,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,, " + words.get(j).get_word());
                        a_pre_adj_adv_arr.add(words.get(j));
                    }
                }
            }

            //Word endn_word =  new Word(Word.end_word, chunked_words_count);
            //endn_word.add_tag(Word.end_tag);
            //chunked_words.add(endn_word);

            String display_words = "";
            for (Word a_word : words) {
                display_words += a_word.get_in_string_format();
            }
            my_consol.write(display_words);

            String display_chunked_words = "";
            for (Word a_chunked_word : chunked_words) {
                display_chunked_words += a_chunked_word.get_in_string_format();
            }
            my_consol.write(">>>>>>>>>>>>>>>" + display_chunked_words);
            /*
            Word srtn_word =  new Word(Word.strt_word, 0);
            srtn_word.add_tag(Word.strt_tag);
            chunked_words.add(0, srtn_word);

            Word endn_word =  new Word(Word.end_word, chunked_words_count);
            endn_word.add_tag(Word.end_tag);
            chunked_words.add(endn_word);
            */
            tagger.check_grammer_errors(chunked_words);
        }

        private boolean chunk_noun(int row_num_tag_seq, ArrayList<Word> words){
            //my_consol.write("----------------------------------------");
            if(noun_tags_arr.contains(tag_squ_arr[row_num_tag_seq][process_word_index].get_tag())){
                my_consol.write("~~~~~~ processing noun:: " + words.get(process_word_index).get_word()
                                            + " ("+ tag_squ_arr[row_num_tag_seq][process_word_index].get_tag() +")" + " ~~~~~~");
            }else{
                tag_squ_arr[row_num_tag_seq][process_word_index].set_error_position(Tag.SHOULD_BE_A_NOUN_ERROR);
                my_consol.write("!!!! error ::" + Tag.SHOULD_BE_A_NOUN_ERROR);
                return false;
            }
            //my_consol.write("----------------------------------------\n");

            tag_squ_arr[row_num_tag_seq][process_word_index].set_as_an_end_of_a_chunk();

            return chunk_advadjs("noun", "noun", adj_tags_arr, row_num_tag_seq, words, true);
        }

        private boolean chunk_last_verb(int row_num_tag_seq/*Tag[] a_tag_squ_arr*/, ArrayList<Word> words){
            my_consol.write("");

            //-2 as the end word is there
            process_word_index = /*a_tag_squ_arr*/tag_squ_arr[row_num_tag_seq].length -2;

            if(last_word_tags_arr.contains(tag_squ_arr[row_num_tag_seq][process_word_index].get_tag())){
                my_consol.write("Last word of the cunk has a verb tag");
            }else{
                my_consol.write("!!!! Last word of the cunk doesn't have a verb tag");
                return false;
            }
            my_consol.write("");

            tag_squ_arr[row_num_tag_seq][process_word_index].set_as_an_end_of_a_chunk();

            return chunk_advadjs("last word", "verb", adverb_tags_arr, row_num_tag_seq, words, false);
        }

        private boolean chunk_advadjs(String process_name
                                                            , String general_tag_name
                                                            , ArrayList<String> advadj_tags_arr
                                                            , int row_num_tag_seq
                                                            , ArrayList<Word> words
                                                            , boolean can_be_in_front){

            String tag_for_the_chunk = tag_squ_arr[row_num_tag_seq][process_word_index].get_tag();
            my_consol.write("~~~~~~ processing advadjs of " + process_name + " (" + tag_for_the_chunk + ") ~~~~~~ ");

            if(!decrease_process_word_index_by_one()){
                //my_consol.write("process_word_index::::::: " + process_word_index);
                if(!can_be_in_front){
                    my_consol.write("!!!!!!..... Grammar error::: Incomplete Sentence Contains only the last word ");
                    tag_squ_arr[row_num_tag_seq][process_word_index].set_error_position();
                    return false;
                }else{
                     tag_squ_arr[row_num_tag_seq][process_word_index + 1].set_as_a_start_of_a_chunk(tag_for_the_chunk);
                    return true;
                }
            }

            String advadjs= "";
            for (String string : advadj_tags_arr) {
                advadjs += string + ", ";
            }

            my_consol.write("advadjs::: " + advadjs);

            while(process_word_index!= -10){
                    my_consol.write("Processing word:: "
                                                + words.get(process_word_index).get_word()
                                                + ",, for the tag:: "
                                                + tag_squ_arr[row_num_tag_seq][process_word_index].get_tag());

                    if(advadj_tags_arr.contains(tag_squ_arr[row_num_tag_seq][process_word_index].get_tag())){
                        //break;
                        if(!decrease_process_word_index_by_one()){
                            //my_consol.write("can be in font:: " + can_be_in_front);
                            if(can_be_in_front){
                                my_consol.write("front reached ...........");
                                break;
                            }else{
                                my_consol.write("!!!!!!..... Grammar error::: Incomplete Sentence (only a " + general_tag_name + " and its advadjs are found)");
                                tag_squ_arr[row_num_tag_seq][process_word_index].set_error_position();
                                return false;
                            }
                        }
                    }else{
                        if(process_word_index == 0/*!decrease_process_word_index_by_one()*/){
                            my_consol.write("!!!!!!___ Grammar error::: Incomplete Sentence (only a " + general_tag_name + " and its advadjs are found)");
                            tag_squ_arr[row_num_tag_seq][process_word_index].set_error_position();
                            return false;
                        }
                        break;
                    }
            }

            // +2 :: as process_word_index has been decreased once afer processing the word befor the last advadj
            tag_squ_arr[row_num_tag_seq][process_word_index + 1].set_as_a_start_of_a_chunk(tag_for_the_chunk);

            my_consol.write("End processing "+ process_name +" \n");
            return true;
        }

        private boolean decrease_process_word_index_by_one(){
            process_word_index--;
            if(process_word_index == 0){
                return false;
            }
            return true;
        }
    }
}
