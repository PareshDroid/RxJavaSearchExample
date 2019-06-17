package com.example.rxjavasearchexample

object NewsModel {


    data class Result(
        val status : String,
        val copyright : String,
        val section : String,
        val last_updated : String,
        val num_results : Int,
        val results : List<Results>
    )

    data class Results (
        val section : String,
        val subsection : String,
        val title : String,
        val abstract : String,
        val url : String,
        val byline : String,
        val item_type : String,
        val updated_date : String,
        val created_date : String,
        val published_date : String,
        val material_type_facet : String,
        val kicker : String,
        val multimedia : List<Multimedia>,
        val short_url : String
    )

    data class Multimedia (

        val url : String,
        val format : String,
        val height : Int,
        val width : Int,
        val type : String,
        val subtype : String,
        val caption : String,
        val copyright : String
    )

}