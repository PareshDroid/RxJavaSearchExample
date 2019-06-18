package com.example.rxjavasearchexample

object NewsModel {


    data class Result(
        val status : String,
        val copyright : String,
        val response : Response
    )

    data class Response (
        val docs : List<Docs>
    )

    data class Docs (

        val web_url : String,
        val snippet : String,
        val lead_paragraph : String,
        val abstract : String,
        val source : String,
        val multimedia : List<Multimedia>
    )

    data class Multimedia (

        val subtype : String,
        val url : String,
        val height : Int,
        val width : Int
    )
}