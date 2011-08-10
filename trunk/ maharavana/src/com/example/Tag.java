/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.example;

/**
 *
 * @author hp
 */
public class Tag {
        public static final String VERB = "verb";
        public static final String AD_VERBS = "adverb";

        public static final String NOUN = "noun";
        public static final String ADJ = "adj";

        public static final String SHOULD_BE_A_NOUN_ERROR = "this should be a in a noun format";

        private String tag;
        
        private boolean is_a_start_of_chunk = false;
        private boolean is_an_end_of_chunk = false;
        private boolean is_an_error_pos = false;
        private String error_code = "";

        private String equvalent_tag_for_chunk = "";

        public Tag(String tTag){
            tag = tTag;
        }

        public void set_tag(String tTag){
            tag = tTag;
        }

        public String get_tag(){
            return tag;
        }

        public void set_as_a_start_of_a_chunk(String eEquvalent_tag_for_chunk){
            is_a_start_of_chunk = true;
            equvalent_tag_for_chunk = eEquvalent_tag_for_chunk;
        }

        public void set_as_an_end_of_a_chunk(){
            is_an_end_of_chunk = true;
        }

        public boolean is_a_start_of_chunk(){
            return is_a_start_of_chunk;
        }

        public boolean is_an_end_of_chunk(){
            return is_an_end_of_chunk;
        }

        public String to_string(){
            String pre_append = "";
            String append = "";
            if(is_a_start_of_chunk){
                pre_append = "[ {" + equvalent_tag_for_chunk + "}";
            }
            if(is_an_error_pos){
                pre_append += "{error pos <"+ error_code +">}";
            }
            if(is_an_end_of_chunk){
                append = "]";
            }
            return pre_append + tag + append;
        }

        public boolean is_a_start_of_a_chunk(){
            return is_a_start_of_chunk;
        }

        public String get_equvalent_tag_for_chunk(){
            return equvalent_tag_for_chunk;
        }

        public void set_error_position(String eError_code){
            error_code= eError_code;
            set_error_position();
        }

        public void set_error_position(){
            is_an_error_pos = true;
        }
}
